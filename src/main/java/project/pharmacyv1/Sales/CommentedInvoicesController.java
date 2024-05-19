package project.pharmacyv1.Sales;

import Database.DB;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import project.pharmacyv1.Purchase.PurchaseInvoiceController;

import java.util.List;
import java.util.Map;

public class CommentedInvoicesController {

    @FXML
    private TableView FindTable;

    @FXML
    private TextField SearchTextField;

    @FXML
    private ComboBox<?> choice1;

    @FXML
    private Button confirm;

    @FXML
    private Button confirm1;

    DB db = new DB();

    @FXML
    void CloseCurrentStage(ActionEvent event) {

    }

    public void setSalesInvoiceController(SalesInvoiceController salesInvoiceController) {

    }

    //create a method to fill the table FindTable with the data from the database in table PurchaseInvoices where the InvoiceStatus is equal to on hold
    public void fillTable() {
        // Fetch the data from the database
        List<Map<String, Object>> invoices = db.SelectQuery("salesinvoices" , "SaleStatus" , "on hold");

        System.out.println(invoices);
        // Create an ObservableList to hold the data
        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList(invoices);

        // Clear the existing columns
        FindTable.getColumns().clear();

        // If there's at least one invoice, create the columns based on the keys in the first invoice
        if (!invoices.isEmpty()) {
            Map<String, Object> firstInvoice = invoices.get(0);
            for (String key : firstInvoice.keySet()) {
                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(key);
                column.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().get(key)));
                FindTable.getColumns().add(column);
            }
        }

        // Set the items property of the FindTable to the data
        FindTable.setItems(data);
    }

    @FXML
    public void initialize() {
        fillTable();
    }

}
