package Classes;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import java.util.Map;

public class SupplierUiBuilder {
    private TabPane tabPane;
    private Button deleteButton;
    private Button saveButton;
    private Button saveEditButton;
    private Button newButton;
    private TableView<Map<String, Object>> suppliersTable;
    private TextField supplierName;
    private TextField supplierCode;
    private TextField supplierAddress;
    private TextField phone;
    private TextField maximumLimit;
    private TextField currentBalance;
    private TextArea notes;
    private TextArea returnPolicy;
    private TextField supplierEmail;
    private TextField parentCompany;


    private SupplierUiBuilder() {}


    private void onTabChanged(int newIndex) {
        boolean firstTab = (newIndex == 0);
        deleteButton.setDisable(firstTab);
        saveButton.setDisable(firstTab);
        saveEditButton.setDisable(firstTab);
        newButton.setDisable(firstTab);
    }


    private void onSupplierSelected(Map<String, Object> data) {
        if (data == null) return;

        tabPane.getSelectionModel().select(1);

        supplierName.setText(data.get("SupplierName").toString());
        supplierCode.setText(data.get("SupplierID").toString());
        supplierAddress.setText(data.get("SupplierAddress").toString());
        phone.setText(data.get("SupplierPhone").toString());
        maximumLimit.setText(data.get("MaximumLimit").toString());
        currentBalance.setText(data.get("CurrentBalance").toString());
        notes.setText(data.get("Notes").toString());
        returnPolicy.setText(data.get("ReturnsPolicy").toString());
        supplierEmail.setText(data.get("SupplierEmail").toString());
        parentCompany.setText(data.get("ParentCompany").toString());
    }


    public void fillSupplierDetails() {
        tabPane.getSelectionModel()
                .selectedIndexProperty()
                .addListener((ObservableValue<? extends Number> obs, Number oldVal, Number newVal) ->
                        onTabChanged(newVal.intValue())
                );

        suppliersTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((ObservableValue<? extends Map<String, Object>> obs, Map<String, Object> oldVal, Map<String, Object> newVal) ->
                        onSupplierSelected(newVal)
                );
    }


    public static class Builder {
        private SupplierUiBuilder inst = new SupplierUiBuilder();

        public Builder tabPane(TabPane tp) {
            inst.tabPane = tp;
            return this;
        }

        public Builder deleteButton(Button b) {
            inst.deleteButton = b;
            return this;
        }

        public Builder saveButton(Button b) {
            inst.saveButton = b;
            return this;
        }

        public Builder saveEditButton(Button b) {
            inst.saveEditButton = b;
            return this;
        }

        public Builder newButton(Button b) {
            inst.newButton = b;
            return this;
        }

        public Builder suppliersTable(TableView<Map<String, Object>> table) {
            inst.suppliersTable = table;
            return this;
        }

        public Builder supplierName(TextField tf) {
            inst.supplierName = tf;
            return this;
        }

        public Builder supplierCode(TextField tf) {
            inst.supplierCode = tf;
            return this;
        }

        public Builder supplierAddress(TextField tf) {
            inst.supplierAddress = tf;
            return this;
        }

        public Builder phone(TextField tf) {
            inst.phone = tf;
            return this;
        }

        public Builder maximumLimit(TextField tf) {
            inst.maximumLimit = tf;
            return this;
        }

        public Builder currentBalance(TextField tf) {
            inst.currentBalance = tf;
            return this;
        }

        public Builder notes(TextArea ta) {
            inst.notes = ta;
            return this;
        }

        public Builder returnPolicy(TextArea ta) {
            inst.returnPolicy = ta;
            return this;
        }

        public Builder supplierEmail(TextField tf) {
            inst.supplierEmail = tf;
            return this;
        }

        public Builder parentCompany(TextField tf) {
            inst.parentCompany = tf;
            return this;
        }

        public SupplierUiBuilder build() {
            return inst;
        }
    }
}
