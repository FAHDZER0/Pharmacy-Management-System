package project.pharmacyv1.Customers;

import Config.LanguageSetter;
import Database.DB;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.layout.BorderPane;
import project.pharmacyv1.DashboardController;
import project.pharmacyv1.LogWriter;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfCustomerController {

    @FXML
    private Label Categories1Title;
    @FXML
    private ComboBox choice1;
    @FXML
    private BorderPane SecondaryMainBorderPane;
    @FXML
    private TableView<Map<String, Object>> ItemListTableView;
    @FXML
    private TextField SearchTextField;
    @FXML
    private Label NumberOfItems;

    DB db = new DB();
    LogWriter log = new LogWriter();

    private String getSearchTextField() {
        return SearchTextField.getText();
    }

    public void RefreshButtonAction() {
        // Refresh the table
        ObservableList<Map<String, Object>> data = null;
        if (getSearchTextField().isEmpty()) {
            data = (ObservableList<Map<String, Object>>) db.SelectQuery("customers");
        } else {
            if (choice1.getSelectionModel().getSelectedIndex() == 0) { // Customer ID
                data = (ObservableList<Map<String, Object>>) db.SelectQuery("customers", "CustomerID", getSearchTextField());
            } else if (choice1.getSelectionModel().getSelectedIndex() == 1) { // Customer Name
                data = (ObservableList<Map<String, Object>>) db.SelectQuery("customers", "CustomerName", getSearchTextField());
            } else if (choice1.getSelectionModel().getSelectedIndex() == 2) { // Pharmacy Association
                data = (ObservableList<Map<String, Object>>) db.SelectQuery("customers", "PharmacyAssociation", getSearchTextField());
            } else if (choice1.getSelectionModel().getSelectedIndex() == 3) { // Personal Phone Number
                data = (ObservableList<Map<String, Object>>) db.SelectQuery("customers", "PersonalPhoneNumber", getSearchTextField());
            }
        }

        // Count the number of items
        int numberOfItems = (data != null) ? data.size() : 0;
        NumberOfItems.setText(String.valueOf(numberOfItems));


        fillTable(ItemListTableView, data);
    }

    public void fillTable(TableView<Map<String, Object>> tableView, ObservableList<Map<String, Object>> dataList) {
        // Clear existing columns
        tableView.getColumns().clear();

        // Define the list of editable columns
        List<String> editableColumns = Arrays.asList("PharmacyAssociation" , "CustomerAddress" , "PersonalPhoneNumber" , "CustomerDebt" );

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

    @FXML
    private void DeleteSelectedRow() {
        // Delete the selected row from the database
        Map<String, Object> selectedItem = ItemListTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            db.DeleteQuery("customers", "CustomerID", selectedItem.get("CustomerID").toString());
            tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
            AnimationType type = AnimationType.POPUP;
            tray.setAnimationType(type);
            tray.setTitle("Success");
            tray.setMessage("Customer Deleted Successfully");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(javafx.util.Duration.seconds(2));

            RefreshButtonAction();
            log.RemoveItem(db.logedInUser,selectedItem.get("CustomerName").toString());
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("No Row Selected");
            alert.setContentText("Please select a row to delete");
            alert.showAndWait();
        }
    }

    DashboardController DC = new DashboardController();

    @FXML
    public void setInCenter(ActionEvent event) {

        String MenuItemName = null;
        String temp = null;

        SecondaryMainBorderPane.setCenter(null);
        // Extract substring before the underscore character, if exists
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/pharmacyv1/Customer/AddNewCustomer.fxml"));

            BorderPane secondaryContent = loader.load();
            SecondaryMainBorderPane.setCenter(secondaryContent);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error

        }
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

    @FXML
    private void EditSelectedRow() {
        // Edit the selected row from the database
        Map<String, Object> selectedItem = ItemListTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Create a copy of the selected item map
            Map<String, Object> updateItem = new HashMap<>(selectedItem);
            // Remove the ID from the update item map
            updateItem.remove("CustomerID");

            // Update the selected row
            db.UpdateQuery("customers", updateItem, "CustomerID", selectedItem.get("CustomerID").toString());
            RefreshButtonAction();
            log.EditItem(db.logedInUser,selectedItem.get("CustomerName").toString());

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("No Row Selected");
            alert.setContentText("Please select a row to edit");
            alert.showAndWait();
        }
    }

    public void initialize() {

        ItemListTableView.widthProperty().addListener((source, oldWidth, newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) ItemListTableView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });

        searchEvents();

        LanguageSetter LS = new LanguageSetter();

//        ItemListTableView.setOnMouseClicked(event -> {
//            if (ItemListTableView.getSelectionModel().getSelectedIndex() != -1) {
//                if (ItemListTableView.getSelectionModel().getSelectedCells().get(0).getColumn() != 1 && ItemListTableView.getSelectionModel().getSelectedCells().get(0).getColumn() != 2 && ItemListTableView.getSelectionModel().getSelectedCells().get(0).getColumn() != 3 && ItemListTableView.getSelectionModel().getSelectedCells().get(0).getColumn() != 5 && ItemListTableView.getSelectionModel().getSelectedCells().get(0).getColumn() != 8 && ItemListTableView.getSelectionModel().getSelectedCells().get(0).getColumn() != 11) {
//                    Alert alert = new Alert(Alert.AlertType.WARNING);
//                    alert.setTitle("Warning Dialog");
//                    alert.setHeaderText("ID Column");
//                    alert.setContentText("You can't edit this column");
//                    alert.showAndWait();
//                    ItemListTableView.getSelectionModel().clearSelection();
//                }
//            }
//        });

        if(DC.Language.equals("en")){
            Categories1Title.setText(LS.il8n("customer1","en"));
        } else if(DC.Language.equals("ar")){
            Categories1Title.setText(LS.il8n("customer1","ar"));
        }

        choice1.setItems(FXCollections.observableArrayList("Customer ID", "Customer Name" ,"Pharmacy Association","Personal Phone Number"));
        choice1.getSelectionModel().select(0);

    }

}
