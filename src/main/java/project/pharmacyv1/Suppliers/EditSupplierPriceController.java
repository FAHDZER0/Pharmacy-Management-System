package project.pharmacyv1.Suppliers;

import Config.LanguageSetter;
import Database.DB;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import project.pharmacyv1.DashboardController;
import project.pharmacyv1.LogWriter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditSupplierPriceController {

    @FXML
    private Label Categories3Title;
    @FXML
    private ComboBox choice1;
    @FXML
    private BorderPane SecondaryMainBorderPane;
    @FXML
    private TableView<Map<String, Object>> ItemListTableView;
    @FXML
    private TextField SearchTextField;

    DB db = new DB();
    LogWriter log = new LogWriter();

    private String getSearchTextField() {
        return SearchTextField.getText();
    }

    public void RefreshButtonAction() {
        // Refresh the table
        String searchField = "";
        if (choice1.getSelectionModel().getSelectedIndex() == 0) { // Medication English Name
            searchField = "medications.MedicationID";
        } else if (choice1.getSelectionModel().getSelectedIndex() == 1) { // Medication Arabic Name
            searchField = "suppliers.SupplierID";
        } else if (choice1.getSelectionModel().getSelectedIndex() == 2) { // Manufacturing Company
            searchField = "suppliers.ParentCompany";
        }

        List<String> columnsToRetain = Arrays.asList("SupplierID", "SupplierName", "ParentCompany", "PurchasePrice", "MedicationID");

        ObservableList<Map<String, Object>> data = db.searchMedicationsAndSuppliers(searchField, getSearchTextField(), columnsToRetain);

        fillTable(ItemListTableView, data);
    }

    public void fillTable(TableView<Map<String, Object>> tableView, ObservableList<Map<String, Object>> dataList) {
        // Clear existing columns
        tableView.getColumns().clear();

        // Define the list of editable columns
        List<String> editableColumns = Arrays.asList("PurchasePrice"); // replace with your actual column names

        // Add columns dynamically based on the keys of the first map in the list
        if (!dataList.isEmpty()) {
            Map<String, Object> firstRow = dataList.get(0);
            for (String columnName : firstRow.keySet()) {
                TableColumn<Map<String, Object>, String> column = new TableColumn<>(columnName);
                column.setCellValueFactory(data -> {
                    Object value = data.getValue().get(columnName);
                    return new SimpleObjectProperty<>(value != null ? value.toString() : null);
                });

                // Set the cell factory to TextFieldTableCell for editing only if the column is in the list of editable columns
                if (editableColumns.contains(columnName)) {
                    column.setCellFactory(TextFieldTableCell.forTableColumn());

                    // Handle onEditCommit event
                    column.setOnEditCommit(event -> {
                        Map<String, Object> selectedRow = event.getTableView().getItems().get(event.getTablePosition().getRow());
                        selectedRow.put(columnName, event.getNewValue());
                    });
                }

                tableView.getColumns().add(column);
            }
        }

        // Set the data to the table
        tableView.setItems(dataList);

        // Make the TableView editable
        tableView.setEditable(true);
    }

    DashboardController DC = new DashboardController();

    private void searchEvents(){
        RefreshButtonAction();
        SearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            RefreshButtonAction();
        });
        choice1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            RefreshButtonAction();
        });
    }

    @FXML
    private void EditSelectedRow() {
        // Edit the selected row from the database
        Map<String, Object> selectedItem = ItemListTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Create a copy of the selected item map
            Map<String, Object> updateValues = new HashMap<>();
            updateValues.put("PurchasePrice", selectedItem.get("PurchasePrice"));
            db.UpdateMedicationPrice("medications", updateValues, "MedicationID", selectedItem.get("MedicationID").toString());            // Update the selected row
            RefreshButtonAction();

            String text = "Medication " + selectedItem.get("MedicationID").toString() ;

            log.EditItem(db.logedInUser, text);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("No Row Selected");
            alert.setContentText("Please select a row to edit");
            alert.showAndWait();
        }
    }

    public void initialize() {

        searchEvents();

        LanguageSetter LS = new LanguageSetter();

        ItemListTableView.setOnMouseClicked(event -> {
            if (ItemListTableView.getSelectionModel().getSelectedIndex() != -1) {
                if (ItemListTableView.getSelectionModel().getSelectedCells().get(0).getColumn() != 1) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("No Edit Allowed");
                    alert.setContentText("You can't edit this column");
                    alert.showAndWait();
                    ItemListTableView.getSelectionModel().clearSelection();
                }
            }
        });


        if(DC.Language.equals("en")){
            Categories3Title.setText(LS.il8n("supplier3","en"));
        } else if(DC.Language.equals("ar")){
            Categories3Title.setText(LS.il8n("supplier3","ar"));
        }

        choice1.setItems(FXCollections.observableArrayList("MedicationID" , "SupplierID", "ParentCompany"));
        choice1.getSelectionModel().select(0);

    }

}
