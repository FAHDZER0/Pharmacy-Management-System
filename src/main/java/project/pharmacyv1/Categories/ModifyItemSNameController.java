package project.pharmacyv1.Categories;

import Config.LanguageSetter;
import Database.DB;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifyItemSNameController {

    @FXML
    private Label Categories3Title;
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
    private Button saveedit1;
    @FXML
    private Button saveedit2;

    DB db = new DB();
    LogWriter log = new LogWriter();

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
        data.forEach(row -> row.remove("Quantity"));
        data.forEach(row -> row.remove("Manufacturer"));
        data.forEach(row -> row.remove("ProductType"));
        data.forEach(row -> row.remove("ExpiryDate"));
        data.forEach(row -> row.remove("ReorderLevel"));

        fillTable(ItemListTableView, data);
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
        data.forEach(row -> row.remove("Quantity"));
        data.forEach(row -> row.remove("Manufacturer"));
        data.forEach(row -> row.remove("MedicationType"));
        data.forEach(row -> row.remove("ExpiryDate"));
        data.forEach(row -> row.remove("ReorderLevel"));
        data.forEach(row -> row.remove("InternationalCode"));
        data.forEach(row -> row.remove("ActiveIngredient"));
        data.forEach(row -> row.remove("Unit"));
        fillTable(ItemListTableView1, data);
    }

public void fillTable(TableView<Map<String, Object>> tableView, ObservableList<Map<String, Object>> dataList) {
    // Clear existing columns
    tableView.getColumns().clear();

    // Define the list of editable columns
    List<String> editableColumns = Arrays.asList("SellingPrice" , "PurchasePrice"); // replace with your actual column names

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

    @FXML
    private void EditSelectedRow() {
        // Edit the selected row from the database
        Map<String, Object> selectedItem = ItemListTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Create a copy of the selected item map
            Map<String, Object> updateItem = new HashMap<>(selectedItem);
            // Remove the ID from the update item map
            updateItem.remove("ProductID");

            // Update the selected row
            db.UpdateQuery("products", updateItem, "ProductID", selectedItem.get("ProductID").toString());

            RefreshButtonAction();
            log.EditItem(db.logedInUser, selectedItem.get("EnglishName").toString());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("No Row Selected");
            alert.setContentText("Please select a row to edit");
            alert.showAndWait();
        }
    }

    @FXML
    private void EditSelectedRow1() {
        // Edit the selected row from the database
        Map<String, Object> selectedItem = ItemListTableView1.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Create a copy of the selected item map
            Map<String, Object> updateItem = new HashMap<>(selectedItem);
            // Remove the ID from the update item map
            updateItem.remove("MedicationID");

            // Update the selected row
            db.UpdateQuery("medications", updateItem, "MedicationID", selectedItem.get("MedicationID").toString());


            RefreshButtonAction2();
            log.EditItem(db.logedInUser, selectedItem.get("EnglishName").toString());

            // fixing the id column that it doesn't change its width
            ItemListTableView1.getColumns().get(0).setPrefWidth(50);
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

        ItemListTableView1.widthProperty().addListener((source, oldWidth, newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) ItemListTableView1.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });

        searchEvents();

        LanguageSetter LS = new LanguageSetter();

        ItemListTableView.setOnMouseClicked(event -> {
            if (ItemListTableView.getSelectionModel().getSelectedIndex() != -1) {
                if (ItemListTableView.getSelectionModel().getSelectedCells().get(0).getColumn() <= 2) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("No Edit Allowed");
                    alert.setContentText("You can't edit this column");
                    alert.showAndWait();
                    ItemListTableView.getSelectionModel().clearSelection();
                }
            }
        });

        ItemListTableView1.setOnMouseClicked(event -> {
            if (ItemListTableView1.getSelectionModel().getSelectedIndex() != -1) {
                if (ItemListTableView1.getSelectionModel().getSelectedCells().get(0).getColumn() <= 2) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("No Edit Allowed");
                    alert.setContentText("You can't edit this column");
                    alert.showAndWait();
                    ItemListTableView1.getSelectionModel().clearSelection();
                }
            }
        });

        if(DC.Language.equals("en")){
            Categories3Title.setText(LS.il8n("Categories5","en"));
            choice1.setItems(FXCollections.observableArrayList("Product English Name", "Product Arabic Name" ,"Manufacturing Company"));
            choice2.setItems(FXCollections.observableArrayList("Medication English Name", "Medication Arabic Name" ,"Manufacturing Company","Active Ingredient"));
            saveedit1.setText("Save Edit");
            saveedit2.setText("Save Edit");

        } else if(DC.Language.equals("ar")){
            Categories3Title.setText(LS.il8n("Categories5","ar"));
            choice1.setItems(FXCollections.observableArrayList("اسم المنتج بالانجليزي", "اسم المنتج بالعربي" ,"شركة التصنيع"));
            choice2.setItems(FXCollections.observableArrayList("اسم الدواء بالانجليزي", "اسم الدواء بالعربي" ,"شركة التصنيع","المادة الفعالة"));
            saveedit1.setText("حفظ التعديل");
            saveedit2.setText("حفظ التعديل");
        }

        choice1.getSelectionModel().select(0);
        choice2.getSelectionModel().select(0);

    }

}
