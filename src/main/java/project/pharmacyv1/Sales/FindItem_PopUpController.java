package project.pharmacyv1.Sales;

import Classes.RequestHandler;
import Database.DB;
import Database.DBConfig;
import Utilities.BaseHandler;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import project.pharmacyv1.Dashboard.DashboardController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FindItem_PopUpController {

    @FXML
    private ComboBox<String> choice1;
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
    private ComboBox<String> UnitComboBox;
    @FXML
    private Label TotalBalancevalue;
    @FXML
    private Button confirm;

    private SalesInvoiceController salesInvoiceController;

    public String notification;
    public int state;
    DB db = new DB();

    // Chain Responsibility Concrete Classes
    private class SelectionValidationHandler extends BaseHandler {
        @Override
        public boolean handle(Map<String, Object> selectedRow, double requestedAmount) {
            if (selectedRow == null || AmountValue.getText().isEmpty() || requestedAmount <= 0) {
                showAlert("Error", "Error", "Please select an item and enter a valid amount");
                return false;
            }
            return processNext(selectedRow, requestedAmount);
        }
    }
    private class LimitValidationHandler extends BaseHandler {
        @Override
        public boolean handle(Map<String, Object> selectedRow, double requestedAmount) {
            Object limitedValueObj = selectedRow.get("Limited");
            double limitedValue = (limitedValueObj != null && !limitedValueObj.toString().isEmpty())
                    ? Double.parseDouble(limitedValueObj.toString())
                    : Double.MAX_VALUE;

            String currentUsername = DashboardController.miniUserNam;
            boolean isAdmin = currentUsername.equalsIgnoreCase("admin");

            if (requestedAmount > limitedValue && !isAdmin) {
                notification = "AmountValue: " + AmountValue.getText() + "\n" +
                        "ItemAmount: " + ItemAmount.getText() + "\n" +
                        "Would you approve this requested quantity?";
                state = 1;
                insertAlertIntoDB(state, notification);

                showAlert("Limit Exceeded",
                        "This quantity cannot be dispensed.\n",
                        "Maximum allowed quantity:\n" + limitedValue + "\nPlease refer to the manager");
                return false;
            }
            return processNext(selectedRow, requestedAmount);
        }
    }
    private class ProcessingHandler extends BaseHandler {
        @Override
        public boolean handle(Map<String, Object> selectedRow, double requestedAmount) {
            try {
                Map<String, Object> processedRow = processRow(selectedRow);

                if (salesInvoiceController != null) {
                    salesInvoiceController.setSales1BigTable(processedRow);
                }
                return true;
            } finally {
                CloseCurrentStage();
            }
        }

        private Map<String, Object> processRow(Map<String, Object> selectedRow) {
            Map<String, Object> processedRow = new HashMap<>(selectedRow);

            // Remove unnecessary fields
            String[] fieldsToRemove = {
                    "InternationalCode", "ActiveIngredient", "Manufacturer",
                    "ExpiryDate", "PurchasePrice", "ReorderLevel", "Quantity"
            };

            for (String field : fieldsToRemove) {
                processedRow.remove(field);
            }

            // Add required fields
            processedRow.put("Unit", UnitComboBox.getValue());
            processedRow.put("Amount", AmountValue.getText());
            processedRow.put("Total Price", TotalBalancevalue.getText());

            return processedRow;
        }
    }
    // ========== END OF CHAIN RESPONSIBILITY CONCRETE CLASSES ==========

    // Helper method to show alerts
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void addSelectedRowToSalesInvoice() {
        try {
            Map<String, Object> selectedRow = FindTable.getSelectionModel().getSelectedItem();
            double requestedAmount = AmountValue.getText().isEmpty() ? 0 : Double.parseDouble(AmountValue.getText());

            // Setup and execute the chain
            RequestHandler selectionValidator = new SelectionValidationHandler();
            RequestHandler limitValidator = new LimitValidationHandler();
            RequestHandler processor = new ProcessingHandler();

            selectionValidator.setNextHandler(limitValidator);
            limitValidator.setNextHandler(processor);

            selectionValidator.handle(selectedRow, requestedAmount);

        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Amount", "Please enter a valid number for amount");
        } catch (Exception e) {
            showAlert("Error", "Unexpected Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void insertAlertIntoDB(int seenByAdmin, String message) {
        String query = "INSERT INTO admin_notification (seen_by_admin, message) VALUES (?, ?)";

        try (Connection conn = DBConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, seenByAdmin);
            stmt.setString(2, message);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void CloseCurrentStage() {
        Stage stage = (Stage) choice1.getScene().getWindow();
        stage.close();
    }

    private String getSearchTextField() {
        return SearchTextField.getText();
    }

    @FXML
    public void RefreshButtonAction() {
        ObservableList<Map<String, Object>> data1 = getData("medications");
        ObservableList<Map<String, Object>> data2 = getData("products");

        processData(data1, "MedicationID", "MedicationType", "MedicationBarcode");
        processData(data2, "ProductID", "ProductType", "ProductBarcode");

        fillTable(FindTable, data1, data2);
    }

    private ObservableList<Map<String, Object>> getData(String tableName) {
        if (getSearchTextField().isEmpty()) {
            return (ObservableList<Map<String, Object>>) db.SelectQuery(tableName);
        }

        int selectedIndex = choice1.getSelectionModel().getSelectedIndex();
        String columnName = getSearchColumn(selectedIndex);

        return columnName != null ?
                (ObservableList<Map<String, Object>>) db.SelectQuery(tableName, columnName, getSearchTextField()) :
                FXCollections.observableArrayList();
    }

    private String getSearchColumn(int selectedIndex) {
        switch (selectedIndex) {
            case 0: return "EnglishName";
            case 1: return "ArabicName";
            case 2: return "Manufacturer";
            default: return null;
        }
    }

    private void processData(ObservableList<Map<String, Object>> data, String idField, String typeField, String barcodeField) {
        if (data != null) {
            for (Map<String, Object> row : data) {
                row.put("ItemID", row.remove(idField));
                row.put("ItemType", row.remove(typeField));
                row.put("ItemBarcode", row.remove(barcodeField));
            }
        }
    }

    public void fillTable(TableView<Map<String, Object>> tableView, ObservableList<Map<String, Object>> dataList1, ObservableList<Map<String, Object>> dataList2) {
        tableView.getColumns().clear();

        List<String> orderedKeys = Arrays.asList(
                "ItemID", "ItemBarcode", "EnglishName", "ArabicName", "Manufacturer",
                "ExpiryDate", "Unit", "SellingPrice", "PurchasePrice", "ReorderLevel", "ItemType", "Quantity"
        );

        orderedKeys.forEach(columnName -> {
            TableColumn<Map<String, Object>, String> column = new TableColumn<>(columnName);
            column.setCellValueFactory(data ->
                    new SimpleObjectProperty<>(data.getValue().getOrDefault(columnName, "").toString()));
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            tableView.getColumns().add(column);
        });

        ObservableList<Map<String, Object>> mergedData = FXCollections.observableArrayList();
        if (dataList1 != null) mergedData.addAll(dataList1);
        if (dataList2 != null) mergedData.addAll(dataList2);
        tableView.setItems(mergedData);
    }

    private void initializeListeners() {
        // Search and refresh listeners
        SearchTextField.textProperty().addListener((obs, oldVal, newVal) -> RefreshButtonAction());
        choice1.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> RefreshButtonAction());

        // Table selection listener
        FindTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateValues(newVal);
            confirm.setDisable(newVal == null);
        });

        // Amount calculation listener
        AmountValue.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                if (!newVal.isEmpty() && Double.parseDouble(newVal) >= 0) {
                    double price = Double.parseDouble(ItemSellingPrice.getText());
                    TotalBalancevalue.setText(String.valueOf(price * Double.parseDouble(newVal)));
                } else {
                    TotalBalancevalue.setText("0");
                }
            } catch (NumberFormatException e) {
                TotalBalancevalue.setText("0");
            }
        });
    }

    private void updateValues(Map<String, Object> selectedRow) {
        if (selectedRow != null) {
            BarcodeValue.setText(getStringValue(selectedRow, "ItemBarcode"));
            ItemSellingPrice.setText(getStringValue(selectedRow, "SellingPrice"));
            ItemAmount.setText(getStringValue(selectedRow, "Quantity"));

            if (AmountValue.getText().isEmpty() || Double.parseDouble(AmountValue.getText()) < 0) {
                AmountValue.setText("1");
            }

            updateTotalBalance();
            updateUnitComboBox(selectedRow);
        }
    }

    private String getStringValue(Map<String, Object> row, String key) {
        return row.get(key) != null ? row.get(key).toString() : "";
    }

    private void updateTotalBalance() {
        try {
            double price = Double.parseDouble(ItemSellingPrice.getText());
            double amount = Double.parseDouble(AmountValue.getText());
            TotalBalancevalue.setText(String.valueOf(price * amount));
        } catch (NumberFormatException e) {
            TotalBalancevalue.setText("0");
        }
    }

    private void updateUnitComboBox(Map<String, Object> selectedRow) {
        Set<String> distinctUnits = new HashSet<>();

        FindTable.getItems().forEach(row -> {
            String unit = getStringValue(row, "Unit");
            if (!unit.isEmpty()) {
                distinctUnits.add(unit);
            }
        });

        UnitComboBox.setItems(FXCollections.observableArrayList(distinctUnits));
        UnitComboBox.setValue(getStringValue(selectedRow, "Unit").isEmpty() ?
                "Box" : getStringValue(selectedRow, "Unit"));
    }

    public void initialize() {
        // Initialize UI components
        choice1.setItems(FXCollections.observableArrayList(
                "Medication English Name", "Medication Arabic Name", "Manufacturing Company"));
        choice1.getSelectionModel().select(0);

        // Initialize listeners
        initializeListeners();

        // Set initial state
        confirm.setDisable(true);

        // Explicitly set the button action (important fix)
        confirm.setOnAction(event -> addSelectedRowToSalesInvoice());

        // Initial data load
        RefreshButtonAction();
    }

    // Setter for the sales invoice controller
    public void setSalesInvoiceController(SalesInvoiceController controller) {
        this.salesInvoiceController = controller;
    }
}