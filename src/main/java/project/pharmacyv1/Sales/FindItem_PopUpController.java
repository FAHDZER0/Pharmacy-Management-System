package project.pharmacyv1.Sales;

import Database.DB;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FindItem_PopUpController {

    @FXML
    private ComboBox choice1;
    @FXML
    private TextField SearchTextField;
    @FXML
    private TableView<Map<String, Object>> FindTable;
    @FXML
    private TextField AmountValue;
    @FXML
    private Label ItemAmount;
    @FXML
    private Label BarcodeValue;
    @FXML
    private Label ItemSellingPrice;
    @FXML
    private ComboBox UnitComboBox;
    @FXML
    private Label TotalBalancevalue;
    @FXML
    private Button confirm;

    private SalesInvoiceController salesInvoiceController;

    // Add a setter method
    public void setSalesInvoiceController(SalesInvoiceController controller) {
        this.salesInvoiceController = controller;
    }

    @FXML
    void addSelectedRowToSalesInvoice() {
        if (FindTable.getSelectionModel().getSelectedItem() == null || AmountValue.getText().isEmpty() || Double.parseDouble(AmountValue.getText()) <= 0){
//            display alert message to the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please select an item and enter a valid amount");
            alert.showAndWait();
            return;
        }

        // Get the selected row
        Map<String, Object> selectedRow = FindTable.getSelectionModel().getSelectedItem();

        //remove these columns from the selected row before adding it to the SalesInvoice table "internationalCode" , "ActiveIngredient","Manufacturer","ExpiryDate","PurchasePrice","ReorderLevel","Quantity"
        selectedRow.remove("InternationalCode");
        selectedRow.remove("ActiveIngredient");
        selectedRow.remove("Manufacturer");
        selectedRow.remove("ExpiryDate");
        selectedRow.remove("ReorderLevel");
        selectedRow.remove("Quantity");

        // Add the selected unit to the selected row
        selectedRow.put("Unit", UnitComboBox.getValue());

        // Add the selected amount to the selected row
        selectedRow.put("Amount", AmountValue.getText());

        // Add the selected total balance to the selected row
        selectedRow.put("Total Price", TotalBalancevalue.getText());

        //

        // Add the selected row to the SalesInvoice table
        if (salesInvoiceController != null) {
            salesInvoiceController.setSales1BigTable(selectedRow);
        }

        // Close the current stage
        CloseCurrentStage();
    }

    @FXML
    public void CloseCurrentStage() {
        Stage stage = (Stage) choice1.getScene().getWindow();
        stage.close();
    }

    DB db = new DB();

    private String getSearchTextField() {
        return SearchTextField.getText();
    }

    @FXML
    public void RefreshButtonAction() {
        // Refresh the table
        ObservableList<Map<String, Object>> data1 = null;
        ObservableList<Map<String, Object>> data2 = null;


        if (getSearchTextField().isEmpty()) {
            data1 = (ObservableList<Map<String, Object>>) db.SelectQuery("medications");
            data2 = (ObservableList<Map<String, Object>>) db.SelectQuery("products");
        } else {
            if (choice1.getSelectionModel().getSelectedIndex() == 0) { // Medication English Name
                data1 = (ObservableList<Map<String, Object>>) db.SelectQuery("medications", "EnglishName", getSearchTextField());
                data2 = (ObservableList<Map<String, Object>>) db.SelectQuery("products", "EnglishName", getSearchTextField());
            } else if (choice1.getSelectionModel().getSelectedIndex() == 1) { // Medication Arabic Name
                data1 = (ObservableList<Map<String, Object>>) db.SelectQuery("medications", "ArabicName", getSearchTextField());
                data2 = (ObservableList<Map<String, Object>>) db.SelectQuery("products", "ArabicName", getSearchTextField());
            } else if (choice1.getSelectionModel().getSelectedIndex() == 2) { // Manufacturing Company
                data1 = (ObservableList<Map<String, Object>>) db.SelectQuery("medications", "Manufacturer", getSearchTextField());
                data2 = (ObservableList<Map<String, Object>>) db.SelectQuery("products", "Manufacturer", getSearchTextField());
            }

        }


        // Count the number of items
        int numberOfItems1 = (data1 != null) ? data1.size() : 0;
        int numberOfItems2 = (data2 != null) ? data2.size() : 0;
        int totalItems = numberOfItems1 + numberOfItems2;

        if (data1 != null) {
            for (Map<String, Object> row : data1) {
                Object value = row.remove("MedicationID");
                row.put("ItemID", value);
                value = row.remove("MedicationType");
                row.put("ItemType", value);
                value = row.remove("MedicationBarcode");
                row.put("ItemBarcode", value);
            }
        }
        if (data2 != null) {
            for (Map<String, Object> row : data2) {
                Object value = row.remove("ProductID");
                row.put("ItemID", value);
                value = row.remove("ProductType");
                row.put("ItemType", value);
                value = row.remove("ProductBarcode");
                row.put("ItemBarcode", value);

            }
        }

        fillTable(FindTable, data1, data2);
    }

    public void fillTable(TableView<Map<String, Object>> tableView, ObservableList<Map<String, Object>> dataList1, ObservableList<Map<String, Object>> dataList2) {
        // Clear existing columns
        tableView.getColumns().clear();

        // Define the order of columns
        List<String> orderedKeys = Arrays.asList("ItemID", "ItemBarcode", "EnglishName", "ArabicName", "Manufacturer", "ExpiryDate", "Unit", "SellingPrice", "PurchasePrice", "ReorderLevel", "ItemType", "Quantity");

        // Add columns dynamically based on orderedKeys
        for (String columnName : orderedKeys) {
            TableColumn<Map<String, Object>, String> column = new TableColumn<>(columnName);
            column.setCellValueFactory(data -> {
                Object value = data.getValue().get(columnName);
                return new SimpleObjectProperty<>(value != null ? value.toString() : null);
            });

            // Set the cell factory to TextFieldTableCell for editing
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            tableView.getColumns().add(column);
        }

        // Merge dataList1 and dataList2 into one dataList
        ObservableList<Map<String, Object>> dataList = FXCollections.observableArrayList();
        dataList.addAll(dataList1);
        dataList.addAll(dataList2);

        // Set the data to the table
        tableView.setItems(dataList);
    }

    private void searchEvents(){
        RefreshButtonAction();
        SearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            RefreshButtonAction();
        });
        choice1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            RefreshButtonAction();
        });
    }

    private void rowListener(){
        FindTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Map<String, Object>>() {
            @Override
            public void changed(ObservableValue<? extends Map<String, Object>> observable, Map<String, Object> oldValue, Map<String, Object> newValue) {
                updateValues(newValue);
            }
        });

        AmountValue.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && Double.parseDouble(newValue) >= 0) {
                double trueItemSellingPrice = Double.parseDouble(ItemSellingPrice.getText());
                TotalBalancevalue.setText(trueItemSellingPrice * Double.parseDouble(newValue) + "");
            } else {
                TotalBalancevalue.setText("0");
            }
        });
    }

    private void updateValues(Map<String, Object> selectedRow) {
        if (selectedRow != null) {
            // Get the selected row's values
            String itemBarcode = selectedRow.get("ItemBarcode") != null ? selectedRow.get("ItemBarcode").toString() : "";
            String itemSellingPrice = selectedRow.get("SellingPrice") != null ? selectedRow.get("SellingPrice").toString() : "";
            String itemUnit = selectedRow.get("Unit") != null ? selectedRow.get("Unit").toString() : "";
            String itemAmount = selectedRow.get("Quantity") != null ? selectedRow.get("Quantity").toString() : "";
            double trueItemSellingPrice = Double.parseDouble(itemSellingPrice);
            // Set the values to the labels
            BarcodeValue.setText(itemBarcode);
            ItemSellingPrice.setText(itemSellingPrice);
            ItemAmount.setText(itemAmount);
            if (AmountValue.getText().isEmpty() || Double.parseDouble(AmountValue.getText()) < 0)
                AmountValue.setText("1");
            TotalBalancevalue.setText(trueItemSellingPrice * Double.parseDouble(AmountValue.getText()) + "");

            // Get distinct unit values from the table
            Set<String> distinctUnits = new HashSet<>();
            for (Map<String, Object> row : FindTable.getItems()) {
                String unit = row.get("Unit") != null ? row.get("Unit").toString() : "";
                if (!unit.isEmpty()) {
                    distinctUnits.add(unit);
                }
            }

            // Set the distinct unit values to the UnitComboBox
            UnitComboBox.setItems(FXCollections.observableArrayList(distinctUnits));

            // Set the selected unit
            itemUnit = itemUnit.equals("") ? "Box" : itemUnit;
            UnitComboBox.setValue(itemUnit);
        }
    }

    public void initialize() {

        RefreshButtonAction();
        searchEvents();

        rowListener();



        choice1.setItems(FXCollections.observableArrayList("Medication English Name", "Medication Arabic Name" ,"Manufacturing Company"));
        choice1.getSelectionModel().select(0);

        confirm.setOnAction(event -> addSelectedRowToSalesInvoice());

    }


}
