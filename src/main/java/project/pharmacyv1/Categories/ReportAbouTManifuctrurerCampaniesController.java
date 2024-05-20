package project.pharmacyv1.Categories;

import Config.LanguageSetter;
import Config.PDFprinterController;
import Database.DB;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import project.pharmacyv1.DashboardController;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class ReportAbouTManifuctrurerCampaniesController {

    @FXML
    private Label Categories1Title;
    @FXML
    private ComboBox choice1;
    @FXML
    private TableView<Map<String, Object>> ItemListTableView;
    @FXML
    private PieChart ManufacturerPiechart;
    @FXML
    private Button PrintButton;
    private boolean displayAsPercentage = false;

    DB db = new DB();

    public void RefreshButtonAction() {
        // Refresh the table
        ObservableList<Map<String, Object>> data = null;
        //searching the medications and Products table for the manufacturer
        data = (ObservableList<Map<String, Object>>) db.SelectQuery("medications", "Manufacturer", choice1.getSelectionModel().getSelectedItem().toString());
        data.addAll((ObservableList<Map<String, Object>>) db.SelectQuery("products", "Manufacturer", choice1.getSelectionModel().getSelectedItem().toString()));

        // removing all the columns from the data except the manufacturer , id, Ename and Aname columns
        for (Map<String, Object> row : data) {
            row.keySet().retainAll(FXCollections.observableArrayList("Manufacturer", "ProductID", "MedicationID", "EnglishName", "ArabicName", "Quantity"));
        }

        fillTable(ItemListTableView, data);
    }

    public void fillTable(TableView<Map<String, Object>> tableView, ObservableList<Map<String, Object>> dataList) {
        // Clear existing columns
        tableView.getColumns().clear();

        // Add columns dynamically based on the keys of the first map in the list
        if (!dataList.isEmpty()) {
            Map<String, Object> firstRow = dataList.get(0);
            for (String columnName : firstRow.keySet()) {
                TableColumn<Map<String, Object>, String> column = new TableColumn<>(columnName);
                column.setCellValueFactory(data -> {
                    Object value = data.getValue().get(columnName);
                    return new SimpleObjectProperty<>(value != null ? value.toString() : null);
                });

                tableView.getColumns().add(column);
            }
        }

        // Set the data to the table
        tableView.setItems(dataList);
    }
    DashboardController DC = new DashboardController();

    public void setPieChartData(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            //setting the pie chart data
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            db.SelectQuery("medications").forEach(row -> {
                Object quantityObj = row.get("Quantity");
                Object labelObj = row.get("EnglishName");
                if (quantityObj != null && labelObj != null) {
                    int quantity = Integer.parseInt(quantityObj.toString());
                    String label = labelObj.toString();
                    if (displayAsPercentage) {
                        double totalQuantity = db.SelectQuerySum("medications", "Quantity");
                        double percentage = (quantity / totalQuantity) * 100;
                        label += " (" + String.format("%.2f", percentage) + "%)";
                    } else {
                        label += " (" + quantity + ")";
                    }
                    pieChartData.add(new PieChart.Data(label, quantity));
                }
            });
            db.SelectQuery("products").forEach(row -> {
                Object quantityObj = row.get("Quantity");
                Object labelObj = row.get("EnglishName");
                if (quantityObj != null && labelObj != null) {
                    int quantity = Integer.parseInt(quantityObj.toString());
                    String label = labelObj.toString();
                    if (displayAsPercentage) {
                        double totalQuantity = db.SelectQuerySum("products", "Quantity");
                        double percentage = (quantity / totalQuantity) * 100;
                        label += " (" + String.format("%.2f", percentage) + "%)";
                    } else {
                        label += " (" + quantity + ")";
                    }
                    pieChartData.add(new PieChart.Data(label, quantity));
                }
            });
            ManufacturerPiechart.setData(pieChartData);
            displayAsPercentage = !displayAsPercentage;
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // printing the report onto a pdf file
    public void PrintButtonAction() {

        PDFprinterController pdfPrinter = new PDFprinterController();
        pdfPrinter.printTableIntoPDF(ItemListTableView.getItems());
    }

    public void initialize() {

        //making the choice1 have all the manufacturers in the medication and products tables
        ObservableList<String> manufacturers = FXCollections.observableArrayList();
        db.SelectQuery("medications").forEach(row -> {
            if (!manufacturers.contains(row.get("Manufacturer").toString())) {
                manufacturers.add(row.get("Manufacturer").toString());
            }
        });
        db.SelectQuery("products").forEach(row -> {
            if (!manufacturers.contains(row.get("Manufacturer").toString())) {
                manufacturers.add(row.get("Manufacturer").toString());
            }
        });
        choice1.setItems(manufacturers);
        choice1.getSelectionModel().select(0);
        choice1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            RefreshButtonAction();
        });
        setPieChartData();
        RefreshButtonAction();

        LanguageSetter LS = new LanguageSetter();

        if(DC.Language.equals("en")){
            Categories1Title.setText(LS.il8n("Categories7","en"));
            PrintButton.setText("Print");
        } else if(DC.Language.equals("ar")){
            Categories1Title.setText(LS.il8n("Categories7","ar"));
            PrintButton.setText("طباعة");
        }

    }

}
