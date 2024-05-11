package project.pharmacyv1.Suppliers;

import Config.LanguageSetter;
import Database.DB;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import project.pharmacyv1.DashboardController;
import project.pharmacyv1.LogWriter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import tray.notification.TrayNotification;


import java.util.HashMap;
import java.util.Map;


public class SuppliersListController {

    @FXML
    private TextField MaximumLimit;
    @FXML
    private TextArea Notes;
    @FXML
    private TextField Phone;
    @FXML
    private TextArea ReturnPolicy;
    @FXML
    private TextField SupplierAddress;
    @FXML
    private TextField SupplierCode;
    @FXML
    private TextField SupplierName;
    @FXML
    private Label Suppliers1Title;
    @FXML
    private TextField SuppliersCurrentBalance;
    @FXML
    private TextField SupplierEmail;
    @FXML
    private TextField ParentCompany;
    @FXML
    private TableView<Map<String, Object>> SuppliersTableView;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField name_codeTextField;
    @FXML
    private Button saveButton;
    @FXML
    private Button saveEditButton;
    @FXML
    private Button NewButton;
    @FXML
    private TabPane tabPane;

    DB db = new DB();
    LogWriter log = new LogWriter();
    DashboardController DC = new DashboardController();
    LanguageSetter LS = new LanguageSetter();


    private String getName_codeTextField() {
        return name_codeTextField.getText();
    }

    public void NewButtonAction() {
        // Clear the text fields
        Notes.clear();
        Phone.clear();
        ReturnPolicy.clear();
        SupplierName.clear();
        MaximumLimit.clear();
        SuppliersCurrentBalance.clear();
        SupplierAddress.clear();
        SupplierEmail.clear();
        ParentCompany.clear();
        // Generate a new code
        ObservableList<Map<String, Object>> data = db.SelectQuery("suppliers");
        int max = 0;
        for (Map<String, Object> row : data) {
            int code = Integer.parseInt(row.get("SupplierID").toString());
            if (code > max) {
                max = code;
            }
        }
        SupplierCode.setText(String.valueOf(max + 1));
    }

    public void AddSupplierButtonAction() {
        // Gather the supplier data from the text fields
        String supplierName = SupplierName.getText();
        String supplierCode = SupplierCode.getText();
        String supplierAddress = SupplierAddress.getText();
        String phone = Phone.getText();
        String maximumLimit = MaximumLimit.getText();
        String CurrentBalance = SuppliersCurrentBalance.getText();
        String notes = Notes.getText();
        String returnPolicy = ReturnPolicy.getText();
        String supplierEmail = SupplierEmail.getText();
        String parentCompany = ParentCompany.getText();


        // Validate the data
        if (supplierName.isEmpty() || supplierCode.isEmpty() || supplierAddress.isEmpty() || phone.isEmpty() || maximumLimit.isEmpty() || CurrentBalance.isEmpty() || notes.isEmpty() || returnPolicy.isEmpty() || supplierEmail.isEmpty() || parentCompany.isEmpty()){
            // Show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Data");
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        }

        // Prepare the data for the insert query
        Map<String, Object> columnValue = new HashMap<>();
        columnValue.put("SupplierName", supplierName);
        columnValue.put("SupplierID", supplierCode);
        columnValue.put("SupplierAddress", supplierAddress);
        columnValue.put("SupplierPhone", phone);
        columnValue.put("MaximumLimit", maximumLimit);
        columnValue.put("CurrentBalance", CurrentBalance);
        columnValue.put("Notes", notes);
        columnValue.put("ReturnsPolicy", returnPolicy);
        columnValue.put("SupplierEmail", supplierEmail);
        columnValue.put("ParentCompany", parentCompany);

        // Add the new supplier to the database
        db.InsertQuery("suppliers", columnValue);

        String text = "Supplier " + supplierCode;

        // Log the action
        log.AddItem(DB.logedInUser, text);

        // Refresh the table
        RefreshButtonAction();
        tabPane.getSelectionModel().select(0);
    }

    public void EditSupplierButtonAction() {
        // Get the selected supplier
        Map<String, Object> selectedSupplier = SuppliersTableView.getSelectionModel().getSelectedItem();

        // Validate the selection
        if (selectedSupplier == null) {
            // Show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Selection");
            alert.setContentText("Please select a supplier to edit.");
            alert.showAndWait();
            return;
        }

        // Gather the supplier data from the text fields
        String supplierName = SupplierName.getText();
        String supplierCode = SupplierCode.getText();
        String supplierAddress = SupplierAddress.getText();
        String phone = Phone.getText();
        String maximumLimit = MaximumLimit.getText();
        String currentBalance = SuppliersCurrentBalance.getText();
        String notes = Notes.getText();
        String returnPolicy = ReturnPolicy.getText();
        String supplierEmail = SupplierEmail.getText();
        String parentCompany = ParentCompany.getText();

        // Validate the data
        if (supplierName.isEmpty() || supplierCode.isEmpty() || supplierAddress.isEmpty() || phone.isEmpty() ||
            maximumLimit.isEmpty() || currentBalance.isEmpty() || notes.isEmpty() || returnPolicy.isEmpty() ||
            supplierEmail.isEmpty() || parentCompany.isEmpty()) {
            // Show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Data");
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        }

        // Prepare the data for the update query
        Map<String, Object> columnValue = new HashMap<>();
        columnValue.put("SupplierName", supplierName);
        columnValue.put("SupplierAddress", supplierAddress);
        columnValue.put("SupplierPhone", phone);
        columnValue.put("MaximumLimit", maximumLimit);
        columnValue.put("CurrentBalance", currentBalance);
        columnValue.put("Notes", notes);
        columnValue.put("ReturnsPolicy", returnPolicy);
        columnValue.put("SupplierEmail", supplierEmail);
        columnValue.put("ParentCompany", parentCompany);

        // Update the supplier in the database
        db.UpdateQuery("suppliers", columnValue, "SupplierID", supplierCode);

        String text = "Supplier " + supplierCode;
        // Log the action
        log.EditItem(DB.logedInUser, text);

        // Refresh the table
        RefreshButtonAction();
        tabPane.getSelectionModel().select(0);
    }

    public void DeleteSupplierButtonAction() {

        // Get the supplier code
        String supplierCode = SupplierCode.getText();

        if (supplierCode.isEmpty() || db.SelectQuery("suppliers", "SupplierID", supplierCode).isEmpty()) {
            // Show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Data");
            alert.setContentText("Please select a supplier to delete");
            alert.showAndWait();
            return;
        }
        // Delete the supplier from the database
        db.DeleteQuery("suppliers", "SupplierID", supplierCode);

        String text = "Supplier " + supplierCode;
        // Log the action
        log.RemoveItem(DB.logedInUser, text);

        // Refresh the table
        RefreshButtonAction();
        NewButtonAction();
        tabPane.getSelectionModel().select(0);
        TrayNotification tray = new TrayNotification();
        tray.setTitle("Success");
        tray.setMessage("Supplier Deleted Successfully");
        tray.showAndDismiss(javafx.util.Duration.seconds(2));
    }

    public void RefreshButtonAction() {
        // Refresh the table
        ObservableList<Map<String, Object>> data ;
        if (getName_codeTextField().isEmpty()) {
            data = (ObservableList<Map<String, Object>>) db.SelectQuery("suppliers");
        }else {
            data = (ObservableList<Map<String, Object>>) db.SelectQuery("suppliers", "SupplierID", getName_codeTextField());
        }

        fillTable(SuppliersTableView, data);
    }

    public void fillTable(TableView<Map<String, Object>> tableView, ObservableList<Map<String, Object>> dataList) {
        // Clear existing columns
        tableView.getColumns().clear();

        // Add columns dynamically based on the keys of the first map in the list
        if (!dataList.isEmpty()) {
            Map<String, Object> firstRow = dataList.get(0);
            for (String columnName : firstRow.keySet()) {
                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
                column.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().get(columnName)));
                tableView.getColumns().add(column);
            }
        }
        // Set the data to the table
        tableView.setItems(dataList);
    }

    public void initialize() {
        RefreshButtonAction();

        name_codeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            RefreshButtonAction();
        });

        // Add a listener to the selected index property of the tab pane
        tabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Check if the first tab is selected
                if (newValue.intValue() == 0) {
                    // Disable the buttons
                    deleteButton.setDisable(true);
                    saveButton.setDisable(true);
                    saveEditButton.setDisable(true);
                    NewButton.setDisable(true);
                } else {
                    // Enable the buttons
                    deleteButton.setDisable(false);
                    saveButton.setDisable(false);
                    saveEditButton.setDisable(false);
                    NewButton.setDisable(false);
                }
            }
        });

        // Add a listener to the selected item property of the table view
        SuppliersTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Map<String, Object>>() {
            @Override
            public void changed(ObservableValue<? extends Map<String, Object>> observable, Map<String, Object> oldValue, Map<String, Object> newValue) {
                // Check if a row is selected
                if (newValue != null) {
                    // Switch to the second tab
                    tabPane.getSelectionModel().select(1);

                    // Fill the text fields with the supplier data
                    SupplierName.setText(newValue.get("SupplierName").toString());
                    SupplierCode.setText(newValue.get("SupplierID").toString());
                    SupplierAddress.setText(newValue.get("SupplierAddress").toString());
                    Phone.setText(newValue.get("SupplierPhone").toString());
                    MaximumLimit.setText(newValue.get("MaximumLimit").toString());
                    SuppliersCurrentBalance.setText(newValue.get("CurrentBalance").toString());
                    Notes.setText(newValue.get("Notes").toString());
                    ReturnPolicy.setText(newValue.get("ReturnsPolicy").toString());
                    SupplierEmail.setText(newValue.get("SupplierEmail").toString());
                    ParentCompany.setText(newValue.get("ParentCompany").toString());
                }
            }
        });

        NewButton.setOnAction(event -> {
            NewButtonAction();
        });
        saveButton.setOnAction(event -> {
            AddSupplierButtonAction();
        });
        saveEditButton.setOnAction(event -> {
            EditSupplierButtonAction();
        });
        deleteButton.setOnAction(event -> {
            DeleteSupplierButtonAction();
        });

        if(DC.Language.equals("en")){
            Suppliers1Title.setText(LS.il8n("supplier1","en"));
        } else if(DC.Language.equals("ar")){
            Suppliers1Title.setText(LS.il8n("supplier1","ar"));
        }
    }

}