package project.pharmacyv1.Purchase;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import Config.LanguageSetter;
import Database.DB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.pharmacyv1.DashboardController;
import project.pharmacyv1.Suppliers.FindSupplier_PopUpController;
import javafx.scene.layout.AnchorPane;

public class PurchaseInvoiceController implements Initializable {

    @FXML
    public TextField SupplierCodeTextField;
    @FXML
    public Label SupplierNameLabel;
    @FXML
    public Label SupplierParentCompany;

    DB db = new DB();

    @FXML
    public void doublClick(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/pharmacyv1/Suppliers/FindSupplier_PopUp.fxml"));
                    AnchorPane secondaryContent = loader.load();

                    // Get the controller and set the PurchaseInvoiceController instance
                    FindSupplier_PopUpController controller = loader.getController();
                    controller.setPurchaseInvoiceController(this);

                    Scene FindScene = new Scene(secondaryContent,524,611);

                    Stage FindStage = new Stage();
                    FindStage.setResizable(false);
                    FindStage.setTitle("Search Supplier");
                    FindStage.setScene(FindScene);
                    FindStage.initModality(Modality.APPLICATION_MODAL);
                    FindStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private Label PurchaseTitle;



    @FXML
    public void setSupplier(Map<String, Object> Supplier){
        SupplierCodeTextField.setText(Supplier.get("SupplierID").toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        SupplierCodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()){
                SupplierNameLabel.setText("");
                SupplierParentCompany.setText("");
            }else{
                try {
                    Map<String, Object> Supplier = db.SelectQuery("suppliers", "SupplierID", newValue).get(0);
                    SupplierNameLabel.setText(Supplier.get("SupplierName").toString());
                    SupplierParentCompany.setText(Supplier.get("ParentCompany").toString());
                    // make the color black
                    SupplierParentCompany.setStyle("-fx-text-fill: black; -fx-border-color: grey; -fx-background-color:  lightgrey;");
                    SupplierNameLabel.setStyle("-fx-text-fill: black; -fx-border-color: grey; -fx-background-color:  lightgrey;");
                } catch (Exception e) {
                    SupplierNameLabel.setText("Supplier Not Found");
                    SupplierParentCompany.setText("Supplier Not Found");
                    // make the color red
                    SupplierParentCompany.setStyle("-fx-text-fill: red; -fx-border-color: grey; -fx-background-color:  lightgrey;");
                    SupplierNameLabel.setStyle("-fx-text-fill: red; -fx-border-color: grey; -fx-background-color:  lightgrey;");
                }
            }
        });


        DashboardController DC = new DashboardController();
        LanguageSetter LS = new LanguageSetter();

        if(DC.Language.equals("en")){
            PurchaseTitle.setText(LS.il8n("PurchaseTitle","en"));
        } else if(DC.Language.equals("ar")){
            PurchaseTitle.setText(LS.il8n("PurchaseTitle","ar"));
        }
    }
}