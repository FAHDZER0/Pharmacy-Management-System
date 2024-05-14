package project.pharmacyv1.Customers;

import Database.DB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewCustomerController {

    @FXML
    private TextField HealthInsuranceNumber;
    @FXML
    private TextField CustomerCode;
    @FXML
    private TextField CurrentDebt;
    @FXML
    private BorderPane SecondaryMainBorderPane;
    @FXML
    private Button canceladd;
    @FXML
    private TextField CustomerName;
    @FXML
    private TextField CustomerAddress;
    @FXML
    private TextField PhoneNumber;
    @FXML
    private TextField PharmacyAssociation;

    DB db = new DB();

    @FXML
    void AddItemButtonAction(ActionEvent event) {

            if (CustomerCode.getText().isEmpty() || CustomerName.getText().isEmpty() || CustomerAddress.getText().isEmpty() || PhoneNumber.getText().isEmpty() || PharmacyAssociation.getText().isEmpty() || HealthInsuranceNumber.getText().isEmpty() || CurrentDebt.getText().isEmpty()) {
                // Show error message
                System.out.println("Please enter needed data correctly");
            } else {
                // Add a new item to the database
                Map<String, Object> data = new HashMap<>();
                data.put("CustomerID", CustomerCode.getText());
                data.put("CustomerName", CustomerName.getText());
                data.put("CustomerAddress", CustomerAddress.getText());
                data.put("PhoneNumber", PhoneNumber.getText());
                data.put("PharmacyAssociation", PharmacyAssociation.getText());
                data.put("HealthInsuranceNumber", HealthInsuranceNumber.getText());
                data.put("CurrentDebt", CurrentDebt.getText());

                db.InsertQuery("customers", data);
            }
    }

    @FXML
    void resetFields(ActionEvent event) {

        CustomerName.setText("");
        CustomerAddress.setText("");
        PhoneNumber.setText("");
        PharmacyAssociation.setText("");
        HealthInsuranceNumber.setText("");
        CurrentDebt.setText("");

    }

    @FXML
    void setInCenter(ActionEvent event) {

    }

    public void initialize() {

        List<Map<String, Object>> allItems = db.SelectQuery("customers");
        int lastItemId = allItems.isEmpty() ? 0 : Integer.parseInt(allItems.get(allItems.size() - 1).get("CustomerID").toString());
        CustomerCode.setText(Integer.toString(lastItemId + 1));
    }

}
