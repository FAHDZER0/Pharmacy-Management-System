package project.pharmacyv1.Suppliers;

import Config.LanguageSetter;
import Database.DB;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import project.pharmacyv1.DashboardController;
import project.pharmacyv1.LogWriter;
import project.pharmacyv1.Purchase.PurchaseInvoiceController;

import java.util.Map;

public class FindSupplier_PopUpController {


        @FXML
        private TableView<Map<String, Object>> SuppliersTableView;
        @FXML
        private TextField SupplierCodeTextField;
        @FXML
        private Button confirm;
        @FXML
        private Button cancel;

        private PurchaseInvoiceController PIC = new PurchaseInvoiceController();

        DB db = new DB();
        LogWriter log = new LogWriter();
        DashboardController DC = new DashboardController();
        LanguageSetter LS = new LanguageSetter();

        private String getSupplierCodeTextField() {
                return SupplierCodeTextField.getText();
        }

        public void RefreshButtonAction() {
                // Refresh the table
                ObservableList<Map<String, Object>> data ;
                if (getSupplierCodeTextField().isEmpty()) {
                        data = (ObservableList<Map<String, Object>>) db.SelectQuery("suppliers");
                }else {
                        data = (ObservableList<Map<String, Object>>) db.SelectQuery("suppliers", "SupplierID", getSupplierCodeTextField());
                }

                for (Map<String, Object> row : data) {
                        row.remove("SupplierPhone");
                        row.remove("SupplierAddress");
                        row.remove("SupplierEmail");
                        row.remove("CurrentBalance");
                        row.remove("MaximumLimit");
                        row.remove("ReturnsPolicy");
                        row.remove("Notes");
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

        public void confirmButtonAction(ActionEvent actionEvent) {

                // getting the selected row from the table view and passing it to the parent controller
                Map<String, Object> selectedRow = SuppliersTableView.getSelectionModel().getSelectedItem();
                PIC.setSupplier(selectedRow);
                confirm.getScene().getWindow().hide();
        }

        public void setPurchaseInvoiceController(PurchaseInvoiceController PIC) {
                this.PIC = PIC;
        }

        public void initialize() {
                RefreshButtonAction();

                confirm.setDisable(true);
                SuppliersTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                                confirm.setDisable(false);
                        }
                });

                confirm.setDefaultButton(true);
                cancel.setCancelButton(true);

                confirm.setOnAction(this::confirmButtonAction);
                cancel.setOnAction(event -> cancel.getScene().getWindow().hide());

                SupplierCodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                        RefreshButtonAction();
                });


//                if(DC.Language.equals("en")){
//                        Suppliers1Title.setText(LS.il8n("supplier2","en"));
//                } else if(DC.Language.equals("ar")){employees
//                        Suppliers1Title.setText(LS.il8n("supplier2","ar"));
//                }

        }


        public FindSupplier_PopUpController() {

        }

        public void findSupplier(ActionEvent actionEvent) {

        }

}
