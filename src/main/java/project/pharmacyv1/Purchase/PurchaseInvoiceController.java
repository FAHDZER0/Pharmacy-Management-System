package project.pharmacyv1.Purchase;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import Config.LanguageSetter;
import Database.DB;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import project.pharmacyv1.DashboardController;
import project.pharmacyv1.Suppliers.FindSupplier_PopUpController;
import javafx.scene.layout.AnchorPane;

public class PurchaseInvoiceController implements Initializable {

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab Invoice_Items;
    @FXML
    private Tab Invoice_Data;
    @FXML
    public ComboBox choice211;
    @FXML
    public TextField SupplierCodeTextField;
    @FXML
    public Label SupplierNameLabel;
    @FXML
    public Label SupplierParentCompany;
    @FXML
    public TableView<Map<String, Object>> PurchaseInvoiceTable;
    @FXML
    private Button AddItemButton;
    @FXML
    private Button NewItemButton;
    @FXML
    private Button NewRowButton;
    @FXML
    private Button PrintButton;
    @FXML
    private Button DeleteRowButton;
    @FXML
    private Button SaveButton;
    @FXML
    private Button incompleteInvoiceButton;
    @FXML
    private Button invoiceCommentButton;
    @FXML
    private Button newInvoiceButton;

    private Button currentButton; //Reference to the currently pressed button


    DB db = new DB();

    private void setSideHoverEffect(Control control){
        control.setOnMouseEntered(event -> {
            control.setStyle("-fx-background-color:  #a1d6e2; -fx-background-radius: 100px; -fx-border-color: #f1f1f2; -fx-border-radius: 100px; -fx-border-width: 3px;");
        });

        control.setOnMouseExited(event -> {
            // If the button is not currently pressed, keep it blue and big
            if (control != currentButton) {
                control.setStyle("-fx-background-color:  #1996aa; -fx-background-radius: 100px; -fx-border-color: #f1f1f2; -fx-border-radius: 100px; -fx-border-width: 3px;");
            }
        });

    }

    public void setPurchaseTable(Map<String, Object> data) {
        List<String> orderedKeys = Arrays.asList("ItemBarcode", "ItemID", "EnglishName", "ArabicName", "ItemType", "Unit", "Quantity", "ExpiryDate", "Bonus", "SellingPrice", "TaxPercentage", "TaxValue", "DiscountPercentage", "DiscountValue");
        List<String> editableColumns = Arrays.asList("Quantity", "ExpiryDate", "Bonus", "SellingPrice", "TaxPercentage", "TaxValue", "DiscountPercentage", "DiscountValue");
        addColumnsToTable(orderedKeys, editableColumns);
        PurchaseInvoiceTable.getItems().add(data);
        System.out.println(data);
    }

    private void addColumnsToTable(List<String> orderedKeys, List<String> editableColumns) {
        for (String columnName : orderedKeys) {
            if (PurchaseInvoiceTable.getColumns().stream().noneMatch(c -> c.getText().equals(columnName))) {
                TableColumn<Map<String, Object>, Object> col = new TableColumn<>(columnName);
                col.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().get(columnName)));
                if (editableColumns.contains(columnName)) {
                    col.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Object>() {
                        @Override
                        public String toString(Object object) {
                            return object != null ? object.toString() : "";
                        }

                        @Override
                        public Object fromString(String string) {
                            return string;
                        }
                    }));
                    col.setOnEditCommit(event -> {
                        Map<String, Object> row = event.getRowValue();
                        row.put(columnName, event.getNewValue());
                    });
                }
                PurchaseInvoiceTable.getColumns().add(col);
            }
        }
        PurchaseInvoiceTable.setStyle("-fx-font-size: 12");
        PurchaseInvoiceTable.setEditable(true);
    }

    @FXML
    public void doubleClick(MouseEvent event) {

        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/pharmacyv1/Suppliers/FindSupplier_PopUp.fxml"));
                AnchorPane root1 = (AnchorPane) fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Find Supplier");
                stage.setScene(new Scene(root1));
                stage.show();
                FindSupplier_PopUpController controller = fxmlLoader.getController();
                controller.setPurchaseInvoiceController(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void addItemShortcutAction() {
        tabPane.getSelectionModel().select(Invoice_Items);
        openFindItemPopup();
    }

    @FXML
    public void doubleClickFindItem(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            openFindItemPopup();
        }
    }

    private void openFindItemPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/pharmacyv1/Purchase/FindItem_PopUp.fxml"));
            VBox root1 = (VBox) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Find Item");
            stage.setScene(new Scene(root1));
            stage.show();
            FindItem_PopUpController controller = fxmlLoader.getController();
            controller.setPurchaseInvoiceController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this method is for displaying the commented Invoices in a separate stage
    @FXML
    public void CommentedInvoices() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/pharmacyv1/Purchase/CommentedInvoices.fxml"));
            VBox root1 = (VBox) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Commented Invoices");
            stage.setScene(new Scene(root1));
            stage.show();
            CommentedInvoicesController controller = fxmlLoader.getController();
            controller.setPurchaseInvoiceController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // this method is for Invoice Commenting so the user can continue the invoice later


    @FXML
    private Label PurchaseTitle;

    @FXML
    public void setSupplier(Map<String, Object> Supplier){
        SupplierCodeTextField.setText(Supplier.get("SupplierID").toString());
    }

    private Tooltip createCustomTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setFont(new Font("Arial", 15));
        tooltip.setShowDelay(Duration.millis(100));
        return tooltip;
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
            newInvoiceButton.setTooltip(createCustomTooltip("New Invoice"));
            AddItemButton.setTooltip(createCustomTooltip("Add Item"));
            NewItemButton.setTooltip(createCustomTooltip("New Item"));
            NewRowButton.setTooltip(createCustomTooltip("New Row"));
            PrintButton.setTooltip(createCustomTooltip("Print"));
            DeleteRowButton.setTooltip(createCustomTooltip("Delete Row"));
            SaveButton.setTooltip(createCustomTooltip("Save Invoice"));
            incompleteInvoiceButton.setTooltip(createCustomTooltip("Incomplete Invoice"));
            invoiceCommentButton.setTooltip(createCustomTooltip("Invoice Comment"));

        } else if(DC.Language.equals("ar")){
            PurchaseTitle.setText(LS.il8n("PurchaseTitle","ar"));
            newInvoiceButton.setTooltip(createCustomTooltip("فاتورة جديدة"));
            AddItemButton.setTooltip(createCustomTooltip("إضافة عنصر"));
            NewItemButton.setTooltip(createCustomTooltip("عنصر جديد"));
            NewRowButton.setTooltip(createCustomTooltip("صف جديد"));
            PrintButton.setTooltip(createCustomTooltip("طباعة"));
            DeleteRowButton.setTooltip(createCustomTooltip("حذف صف"));
            SaveButton.setTooltip(createCustomTooltip("حفظ فاتورة"));
            incompleteInvoiceButton.setTooltip(createCustomTooltip("فاتورة غير مكتملة"));
            invoiceCommentButton.setTooltip(createCustomTooltip("فاتورة معلقة"));

        }

        setSideHoverEffect(newInvoiceButton);
        setSideHoverEffect(AddItemButton);
        setSideHoverEffect(NewItemButton);
        setSideHoverEffect(NewRowButton);
        setSideHoverEffect(PrintButton);
        setSideHoverEffect(DeleteRowButton);
        setSideHoverEffect(SaveButton);
        setSideHoverEffect(incompleteInvoiceButton);
        setSideHoverEffect(invoiceCommentButton);

        choice211.getItems().add("Cash");
        choice211.getItems().add("Credit");
        choice211.getItems().add("On Account");
        choice211.getItems().add("Visa");
        choice211.getItems().add("Other");
        choice211.getSelectionModel().select(0);


    }
}