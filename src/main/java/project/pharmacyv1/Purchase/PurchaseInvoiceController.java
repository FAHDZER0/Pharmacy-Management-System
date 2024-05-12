
package project.pharmacyv1.Purchase;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import Config.LanguageSetter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.pharmacyv1.DashboardController;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class PurchaseInvoiceController implements Initializable {

    @FXML
    public TextField SupplierCodeTextField;
    @FXML
    public Label SupplierNameLabel;

    @FXML
    public void doublClick(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/pharmacyv1/Suppliers/FindSupplier_PopUp.fxml"));
                    AnchorPane secondaryContent = loader.load();

                    Scene FindScene = new Scene(secondaryContent,524,611);

                    Stage SupplierListStage = new Stage();
                    SupplierListStage.setResizable(false);
                    SupplierListStage.setTitle("Search Supplier");
                    SupplierListStage.setScene(FindScene);
                    SupplierListStage.initModality(Modality.APPLICATION_MODAL);
                    SupplierListStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle error
                }

            }
        }
    }

    @FXML
    private Label PurchaseTitle;

    public void setSupplier(Map<String, Object> Supplier){
//        this.SupplierCodeTextField.setText(Supplier.get("SupplierID").toString());

        this.SupplierNameLabel.setText(Supplier.get("SupplierName").toString());
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {



        DashboardController DC = new DashboardController();
        LanguageSetter LS = new LanguageSetter();

        if(DC.Language.equals("en")){
            PurchaseTitle.setText(LS.il8n("PurchaseTitle","en"));
        } else if(DC.Language.equals("ar")){
            PurchaseTitle.setText(LS.il8n("PurchaseTitle","ar"));
        }

    }

}
