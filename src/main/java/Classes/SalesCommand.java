package Classes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.pharmacyv1.Sales.FindItem_PopUpController;
import project.pharmacyv1.Sales.SalesInvoiceController;

import java.io.IOException;

public class SalesCommand implements Command {
    private final SalesInvoiceController invoiceController;

    public SalesCommand(SalesInvoiceController invoiceController) {
        this.invoiceController = invoiceController;
    }

    @Override
    public void execute() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/project/pharmacyv1/Sales/FindItem_PopUp.fxml")
            );
            VBox secondaryContent = loader.load();


            FindItem_PopUpController controller = loader.getController();
            controller.setSalesInvoiceController(invoiceController);

            Scene findScene = new Scene(secondaryContent, 830, 666);
            Stage findStage = new Stage();
            findStage.setResizable(false);
            findStage.setTitle("Search An Item");
            findStage.setScene(findScene);
            findStage.initModality(Modality.APPLICATION_MODAL);
            findStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

