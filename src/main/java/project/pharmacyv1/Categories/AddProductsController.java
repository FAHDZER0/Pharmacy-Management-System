package project.pharmacyv1.Categories;

import Classes.Product;
import DAOs.ProductDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import project.pharmacyv1.DashboardController;
import project.pharmacyv1.LogWriter;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

import java.io.IOException;
import java.time.LocalDate;

public class AddProductsController {

    @FXML
    private TextField ItemCode;
    @FXML
    private TextField medicationBarcode;
    @FXML
    private TextField itemNameArabic;
    @FXML
    private TextField itemNameEnglish;
    @FXML
    private TextField Manufacturer;
    @FXML
    private DatePicker ExpDate;
    @FXML
    private TextField SellingPrice;
    @FXML
    private TextField PurchasePrice;
    @FXML
    private TextField ReorderLevel;
    @FXML
    private TextField MedicationType;
    @FXML
    private BorderPane SecondaryMainBorderPane;
    @FXML
    private Label barcode;
    @FXML
    private Label arabicname;
    @FXML
    private Label englishname;
    @FXML
    private Label manu;
    @FXML
    private Label expdate;
    @FXML
    private Label sellingprice;
    @FXML
    private Label purchaseprice;
    @FXML
    private Label reorderlevel;
    @FXML
    private Label bigtitle;
    @FXML
    private Button canceladd;
    @FXML
    private Button savebutton;
    @FXML
    private Button newbutton;
    @FXML
    private Label title;
    @FXML
    private Label producttype;

    private final ProductDAO productDAO = new ProductDAO(); // Add ProductDAO instance
    LogWriter log = new LogWriter();
    DashboardController DC = new DashboardController();

    @FXML
    public void AddItemButtonAction() {
        if (!validateInputs()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields.");
            return;
        }

        try {
            Product product = new Product.ProductBuilder()
                    .barcode(medicationBarcode.getText())
                    .arabicName(itemNameArabic.getText())
                    .englishName(itemNameEnglish.getText())
                    .manufacturer(Manufacturer.getText())
                    .expiryDate(ExpDate.getValue() != null ? ExpDate.getValue() : LocalDate.now())
                    .quantity(0.0) // Default quantity to 0
                    .sellingPrice(Double.parseDouble(SellingPrice.getText()))
                    .purchasePrice(Double.parseDouble(PurchasePrice.getText()))
                    .reorderLevel(Double.parseDouble(ReorderLevel.getText()))
                    .productType(producttype.getText())
                    .build();

            productDAO.save(product);
            setInCenter();
        } catch (NumberFormatException e) {
            showNotification("Error", "Invalid number format.", NotificationType.ERROR);
        } catch (Exception e) {
            showNotification("Error", "Failed to save product.", NotificationType.ERROR);
        }
    }

    private boolean validateInputs() {
        return !medicationBarcode.getText().isEmpty() &&
                !itemNameArabic.getText().isEmpty() &&
                !itemNameEnglish.getText().isEmpty() &&
                !Manufacturer.getText().isEmpty() &&
                ExpDate.getValue() != null &&
                !SellingPrice.getText().isEmpty() &&
                !PurchasePrice.getText().isEmpty() &&
                !ReorderLevel.getText().isEmpty() &&
                !MedicationType.getText().isEmpty();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showNotification(String title, String message, NotificationType type) {
        tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotificationType(type);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(javafx.util.Duration.seconds(2));
    }

    @FXML
    private void resetFields() {
        medicationBarcode.clear();
        itemNameArabic.clear();
        itemNameEnglish.clear();
        Manufacturer.clear();
        ExpDate.setValue(null);
        SellingPrice.clear();
        PurchasePrice.clear();
        ReorderLevel.clear();
        MedicationType.clear();
    }


    @FXML
    public void setInCenter() {
        System.out.println("Add Categories");
        SecondaryMainBorderPane.setCenter(null);
        // Extract substring before the underscore character, if exists
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/pharmacyv1/Categories/ListOfProducts.fxml"));

            BorderPane secondaryContent = loader.load();
            SecondaryMainBorderPane.setCenter(secondaryContent);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error

        }
    }

    public void initialize() {

        if (DC.Language.equals("en")) {
            barcode.setText("Barcode");
            arabicname.setText("Arabic Name");
            englishname.setText("English Name");
            manu.setText("Manufacturer");
            expdate.setText("Expiry Date");
            sellingprice.setText("Selling Price");
            purchaseprice.setText("Purchase Price");
            reorderlevel.setText("Reorder Level");
            bigtitle.setText("Add New Item");
            title.setText("Add New Product");
            canceladd.setText("Cancel");
            savebutton.setText("Save");
            newbutton.setText("New");
            producttype.setText("Product Type");

        } else if (DC.Language.equals("ar")) {
            barcode.setText("الباركود");
            arabicname.setText("الاسم بالعربي");
            englishname.setText("الاسم بالانجليزي");
            manu.setText("الشركة المصنعة");
            expdate.setText("تاريخ الانتهاء");
            sellingprice.setText("سعر البيع");
            purchaseprice.setText("سعر الشراء");
            reorderlevel.setText("الحد الادنى");
            bigtitle.setText("اضافة صنف جديد");
            title.setText("اضافة منتج جديد");
            canceladd.setText("الغاء");
            savebutton.setText("حفظ");
            newbutton.setText("جديد");
            producttype.setText("نوع المنتج");


        }

    }

}
