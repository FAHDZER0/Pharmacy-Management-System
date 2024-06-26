package project.pharmacyv1.Categories;

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

public class ListOfItemController {

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
    @FXML
    private Label Categories1Title1;
    @FXML
    private Button saveeditbutton;
    @FXML
    private Button deletebutton;
    @FXML
    private Button addbutton;


    DB db = new DB();
    LogWriter log = new LogWriter();

    private String getSearchTextField() {
        return SearchTextField.getText();
    }

    public void RefreshButtonAction() {
        // Refresh the table
        ObservableList<Map<String, Object>> data = null;
        if (getSearchTextField().isEmpty()) {
            data = (ObservableList<Map<String, Object>>) db.SelectQuery("medications");
        } else {
            if (choice1.getSelectionModel().getSelectedIndex() == 0) { // Medication English Name
                data = (ObservableList<Map<String, Object>>) db.SelectQuery("medications", "EnglishName", getSearchTextField());
            } else if (choice1.getSelectionModel().getSelectedIndex() == 1) { // Medication Arabic Name
                data = (ObservableList<Map<String, Object>>) db.SelectQuery("medications", "ArabicName", getSearchTextField());
            } else if (choice1.getSelectionModel().getSelectedIndex() == 2) { // Manufacturing Company
                data = (ObservableList<Map<String, Object>>) db.SelectQuery("medications", "Manufacturer", getSearchTextField());
            } else if (choice1.getSelectionModel().getSelectedIndex() == 3) { // Active Ingredient
                data = (ObservableList<Map<String, Object>>) db.SelectQuery("medications", "ActiveIngredient", getSearchTextField());
            }
        }

        // Count the number of items
        int numberOfItems = (data != null) ? data.size() : 0;
        NumberOfItems.setText(String.valueOf(numberOfItems));

        // removing the quantity column from the table
        data.forEach(row -> row.remove("Quantity"));
        fillTable(ItemListTableView, data);
    }

    public void fillTable(TableView<Map<String, Object>> tableView, ObservableList<Map<String, Object>> dataList) {
        // Clear existing columns
        tableView.getColumns().clear();

        // Define the list of editable columns
        List<String> editableColumns = Arrays.asList("MedicationBarcode", "ArabicName", "EnglishName" , "InternationalCode" , "Unit" , "ReorderLevel" ); // replace with your actual column names

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
            db.DeleteQuery("medications", "MedicationID", selectedItem.get("MedicationID").toString());
            tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
            AnimationType type = AnimationType.POPUP;
            tray.setAnimationType(type);
            tray.setTitle("Success");
            tray.setMessage("Medication Deleted Successfully");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(javafx.util.Duration.seconds(2));

            RefreshButtonAction();
            log.RemoveItem(db.logedInUser,selectedItem.get("EnglishName").toString());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/pharmacyv1/Categories/AddCategories.fxml"));

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
            updateItem.remove("MedicationID");

            // Update the selected row
            db.UpdateQuery("medications", updateItem, "MedicationID", selectedItem.get("MedicationID").toString());
            RefreshButtonAction();
            log.EditItem(db.logedInUser,selectedItem.get("EnglishName").toString());

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

        ItemListTableView.setOnMouseClicked(event -> {
            if (ItemListTableView.getSelectionModel().getSelectedIndex() != -1) {
                if (ItemListTableView.getSelectionModel().getSelectedCells().get(0).getColumn() != 1 && ItemListTableView.getSelectionModel().getSelectedCells().get(0).getColumn() != 2 && ItemListTableView.getSelectionModel().getSelectedCells().get(0).getColumn() != 3 && ItemListTableView.getSelectionModel().getSelectedCells().get(0).getColumn() != 5 && ItemListTableView.getSelectionModel().getSelectedCells().get(0).getColumn() != 8 && ItemListTableView.getSelectionModel().getSelectedCells().get(0).getColumn() != 11) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("ID Column");
                    alert.setContentText("You can't edit this column");
                    alert.showAndWait();
                    ItemListTableView.getSelectionModel().clearSelection();
                }
            }
        });

        if(DC.Language.equals("en")){
            Categories1Title.setText(LS.il8n("Categories1","en"));
            deletebutton.setText("Delete");
            saveeditbutton.setText("Save Edit");
            addbutton.setText("Add");
            Categories1Title1.setText("Number of Items:");
            choice1.setItems(FXCollections.observableArrayList("Medication English Name", "Medication Arabic Name" ,"Manufacturing Company","Active Ingredient"));
        } else if(DC.Language.equals("ar")){
            Categories1Title.setText(LS.il8n("Categories1","ar"));
            deletebutton.setText("حذف");
            saveeditbutton.setText("حفظ التعديل");
            addbutton.setText("إضافة");
            Categories1Title1.setText("عدد العناصر:");
            choice1.setItems(FXCollections.observableArrayList("اسم الدواء بالإنجليزية", "اسم الدواء بالعربية" ,"شركة التصنيع","المادة الفعالة"));

        }

        choice1.getSelectionModel().select(0);

    }

}
