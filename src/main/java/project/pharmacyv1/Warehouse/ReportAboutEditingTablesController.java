package project.pharmacyv1.Warehouse;

import Config.LanguageSetter;
import Config.PDFprinterController;
import Database.DB;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import project.pharmacyv1.DashboardController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javafx.scene.chart.XYChart;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class ReportAboutEditingTablesController {

    @FXML
    private Label Warehouse1Title;
    @FXML
    private TableView<Map<String, Object>> ItemListTableView;
    @FXML
    private BarChart Barchart;
    @FXML
    private Button PrintButton;
    private boolean displayAsPercentage = false;

    DB db = new DB();

    public void RefreshButtonAction() {
        // Refresh the table
        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

        // Read the data from the "Log" text file
        Path path = Paths.get("F:\\Pharmacy Backup\\Pharmacy-Management-System\\src\\main\\java\\Database\\Log");
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> {
                Pattern pattern = Pattern.compile("User (.*?) edited (.*?) at (.*?)$");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("User Name", matcher.group(1));
                    row.put("Item Modified", matcher.group(2));
                    row.put("Date Modified", matcher.group(3));
                    data.add(row);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
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

    public void setBarChartData(){
        // Create a map to store the counts
        Map<String, Integer> employeeEditCounts = new HashMap<>();

        // Read the data from the "Log" text file
        Path path = Paths.get("F:\\Pharmacy Backup\\Pharmacy-Management-System\\src\\main\\java\\Database\\Log");
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> {
                Pattern pattern = Pattern.compile("User (.*?) edited (.*?) at (.*?)$");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    // Get the employee name
                    String employeeName = matcher.group(1);

                    // Increment the count for this employee
                    employeeEditCounts.put(employeeName, employeeEditCounts.getOrDefault(employeeName, 0) + 1);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //setting the bar chart data
        ObservableList<XYChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Add the counts to the series
        for (Map.Entry<String, Integer> entry : employeeEditCounts.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChartData.add(series);
        Barchart.setData(barChartData);
    }
    // printing the report onto a pdf file
    @FXML
    public void PrintButtonAction() {
        PDFprinterController pdfPrinter = new PDFprinterController();
        pdfPrinter.printTableIntoPDF(ItemListTableView.getItems() , false);
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

        setBarChartData();
        RefreshButtonAction();

        LanguageSetter LS = new LanguageSetter();

        if(DC.Language.equals("en")){
            Warehouse1Title.setText(LS.il8n("Warehouse4","en"));
            PrintButton.setText("Print");
        } else if(DC.Language.equals("ar")){
            Warehouse1Title.setText(LS.il8n("Warehouse4","ar"));
            PrintButton.setText("طباعة");
        }

    }

}
