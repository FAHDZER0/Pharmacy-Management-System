package project.pharmacyv1.Suppliers;

import Config.LanguageSetter;
import Config.PDFprinterController;
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


public class ReportAboutSuppliersController {

    @FXML
    private Label Suppliers1Title;
    @FXML
    private TableView<Map<String, Object>> SuppliersTableView;
    @FXML
    private TextField name_codeTextField;
    @FXML
    private Button printsupplierinfo;
    @FXML
    private Label entercode;



    DB db = new DB();
    LogWriter log = new LogWriter();
    DashboardController DC = new DashboardController();
    LanguageSetter LS = new LanguageSetter();

    private String getName_codeTextField() {
        return name_codeTextField.getText();
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

    public void printsupplierinfo() {
        PDFprinterController pdf = new PDFprinterController();
        pdf.printTableIntoPDF(SuppliersTableView.getItems() , true);

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


        if(DC.Language.equals("en")){
            Suppliers1Title.setText(LS.il8n("supplier2","en"));
            printsupplierinfo.setText("Print Supplier Info");
            entercode.setText("Enter Supplier Code");
        } else if(DC.Language.equals("ar")){
            Suppliers1Title.setText(LS.il8n("supplier2","ar"));
            printsupplierinfo.setText("طباعة بيانات المورد");
            entercode.setText("ادخل كود المورد");
        }

    }

}