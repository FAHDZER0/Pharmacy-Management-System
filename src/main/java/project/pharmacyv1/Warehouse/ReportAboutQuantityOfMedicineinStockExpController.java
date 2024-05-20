package project.pharmacyv1.Warehouse;

import Config.LanguageSetter;
import Database.DB;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.layout.BorderPane;
import project.pharmacyv1.DashboardController;
import project.pharmacyv1.LogWriter;

import java.util.Map;

public class ReportAboutQuantityOfMedicineinStockExpController {

    @FXML
    private Label Warehouse4Title;
    @FXML
    private ComboBox choice1;
    @FXML
    private ComboBox choice2;
    @FXML
    private BorderPane SecondaryMainBorderPane;
    @FXML
    private TableView<Map<String, Object>> ItemListTableView;
    @FXML
    private TableView<Map<String, Object>> ItemListTableView1;
    @FXML
    private TextField SearchTextField;
    @FXML
    private TextField SearchTextField2;
    @FXML
    private Label MedicationTotal;
    @FXML
    private Label ProductTotal;

    DB db = new DB();
    LogWriter LW = new LogWriter();

    private String getSearchTextField() {
        return SearchTextField.getText();
    }
    private String getSearchTextField2() {
        return SearchTextField2.getText();
    }

    public void RefreshButtonAction() {
        // Refresh the table
        ObservableList<Map<String, Object>> data = null;
        if (getSearchTextField().isEmpty()) {
            data = (ObservableList<Map<String, Object>>) db.SelectQuery("products");
        } else {
            if (choice1.getSelectionModel().getSelectedIndex() == 0) { // Medication English Name
                data = (ObservableList<Map<String, Object>>) db.SelectQuery("products", "EnglishName", getSearchTextField());
            } else if (choice1.getSelectionModel().getSelectedIndex() == 1) { // Medication Arabic Name
                data = (ObservableList<Map<String, Object>>) db.SelectQuery("products", "ArabicName", getSearchTextField());
            } else if (choice1.getSelectionModel().getSelectedIndex() == 2) { // Manufacturing Company
                data = (ObservableList<Map<String, Object>>) db.SelectQuery("products", "Manufacturer", getSearchTextField());
            }
        }
        // Remove the Quantity column from the data
        data.forEach(row -> row.remove("ProductBarcode"));
        data.forEach(row -> row.remove("Manufacturer"));
        data.forEach(row -> row.remove("ProductType"));
        data.forEach(row -> row.remove("ReorderLevel"));
        data.forEach(row -> row.remove("SellingPrice"));
        data.forEach(row -> row.remove("PurchasePrice"));

        data.removeIf(row -> Integer.parseInt(row.get("Quantity").toString()) == 0);

        //setting the total of the products by summing the quantity of all products
        int total = 0;
        for (Map<String, Object> row : data) {
            total += Integer.parseInt(row.get("Quantity").toString());
        }
        ProductTotal.setText("Total: " + total);
        fillTable(ItemListTableView, data);
        if (ItemListTableView.getColumns().size() >= 4){

            ItemListTableView.getSortOrder().add(ItemListTableView.getColumns().get(4));
        }

    }
    public void RefreshButtonAction2() {
        // Refresh the table
        ObservableList<Map<String, Object>> data = null;
        if (getSearchTextField2().isEmpty()) {
            data = (ObservableList<Map<String, Object>>) db.SelectQuery("medications");
        } else {
            if (choice2.getSelectionModel().getSelectedIndex() == 0) { // Medication English Name
                data = (ObservableList<Map<String, Object>>) db.SelectQuery("medications", "EnglishName", getSearchTextField2());
            } else if (choice2.getSelectionModel().getSelectedIndex() == 1) { // Medication Arabic Name
                data = (ObservableList<Map<String, Object>>) db.SelectQuery("medications", "ArabicName", getSearchTextField2());
            } else if (choice2.getSelectionModel().getSelectedIndex() == 2) { // Manufacturing Company
                data = (ObservableList<Map<String, Object>>) db.SelectQuery("medications", "Manufacturer", getSearchTextField2());
            } else if (choice2.getSelectionModel().getSelectedIndex() == 3) { // Active Ingredient
                data = (ObservableList<Map<String, Object>>) db.SelectQuery("medications", "ActiveIngredient", getSearchTextField2());
            }
        }
        // Remove the Quantity column from the data
        data.forEach(row -> row.remove("MedicationBarcode"));
        data.forEach(row -> row.remove("SellingPrice"));
        data.forEach(row -> row.remove("PurchasePrice"));
        data.forEach(row -> row.remove("Manufacturer"));
        data.forEach(row -> row.remove("MedicationType"));
        data.forEach(row -> row.remove("ReorderLevel"));
        data.forEach(row -> row.remove("InternationalCode"));
        data.forEach(row -> row.remove("ActiveIngredient"));
        data.forEach(row -> row.remove("Unit"));

        data.removeIf(row -> Integer.parseInt(row.get("Quantity").toString()) == 0);

        //setting the total of the medications by summing the quantity of all medications
        int total = 0;
        for (Map<String, Object> row : data) {
            total += Integer.parseInt(row.get("Quantity").toString());
        }
        if (DC.Language.equals("en"))
            MedicationTotal.setText("Total: " + total);
        else if (DC.Language.equals("ar"))
            MedicationTotal.setText("المجموع: " + total);
        fillTable(ItemListTableView1, data);

        if (ItemListTableView1.getColumns().size() >= 4){
            TableColumn<Map<String, Object>, ?> temp = ItemListTableView1.getColumns().get(3);
            TableColumn<Map<String, Object>, ?> temp2 = ItemListTableView1.getColumns().get(4);
            ItemListTableView1.getColumns().remove(3);
            ItemListTableView1.getColumns().remove(3);
            ItemListTableView1.getColumns().add(temp2);
            ItemListTableView1.getColumns().add(temp);
            ItemListTableView1.getSortOrder().add(ItemListTableView1.getColumns().get(4));
        }


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

    private void searchEvents(){
        RefreshButtonAction();
        RefreshButtonAction2();
        SearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            RefreshButtonAction();
        });
        SearchTextField2.textProperty().addListener((observable, oldValue, newValue) -> {
            RefreshButtonAction2();
        });
        choice1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            RefreshButtonAction();
        });
        choice2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            RefreshButtonAction2();
        });
    }

    private void print(){

    }

    public void initialize() {

        searchEvents();

        Platform.runLater(() -> {
            SearchTextField.requestFocus();
        });

        

        LanguageSetter LS = new LanguageSetter();

        if(DC.Language.equals("en")){
            Warehouse4Title.setText(LS.il8n("Warehouse5","en"));
            MedicationTotal.setText("Total:");
            ProductTotal.setText("Total:");
            choice2.setItems(FXCollections.observableArrayList("Medication English Name", "Medication Arabic Name" ,"Manufacturing Company","Active Ingredient"));
            choice1.setItems(FXCollections.observableArrayList("Product English Name", "Product Arabic Name" ,"Manufacturing Company"));
        } else if(DC.Language.equals("ar")){
            Warehouse4Title.setText(LS.il8n("Warehouse5","ar"));
            MedicationTotal.setText("المجموع:");
            ProductTotal.setText("المجموع:");
            choice2.setItems(FXCollections.observableArrayList("اسم الدواء بالانجليزي", "اسم الدواء بالعربي" ,"الشركة المصنعة","المادة الفعالة"));
            choice1.setItems(FXCollections.observableArrayList("اسم المنتج بالانجليزي", "اسم المنتج بالعربي" ,"الشركة المصنعة"));
        }

        choice1.getSelectionModel().select(0);
        choice2.getSelectionModel().select(0);

    }

}
