package project.pharmacyv1.Categories;

import Database.DB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import project.pharmacyv1.DashboardController;
import project.pharmacyv1.LogWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Label productcode;
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

    DB db = new DB();
    LogWriter log = new LogWriter();
    DashboardController DC = new DashboardController();

    public void AddItemButtonAction() {
        // Add a new item to the database
        if (ItemCode.getText().isEmpty() || medicationBarcode.getText().isEmpty() || itemNameArabic.getText().isEmpty() || itemNameEnglish.getText().isEmpty() || Manufacturer.getText().isEmpty() || ExpDate.getValue() == null || SellingPrice.getText().isEmpty() || PurchasePrice.getText().isEmpty() || ReorderLevel.getText().isEmpty() || MedicationType.getText().isEmpty()) {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Data");
            alert.setContentText("Please enter needed data correctly");
            alert.showAndWait();
        } else {
            Map<String, Object> values = new HashMap<>();
            values.put("ProductID", ItemCode.getText());
            values.put("ProductBarcode", medicationBarcode.getText());
            values.put("ArabicName", itemNameArabic.getText());
            values.put("EnglishName", itemNameEnglish.getText());
            values.put("Manufacturer", Manufacturer.getText());
            values.put("ExpiryDate", ExpDate.getValue().toString());
            values.put("SellingPrice", SellingPrice.getText());
            values.put("PurchasePrice", PurchasePrice.getText());
            values.put("ReorderLevel", ReorderLevel.getText());
            values.put("ProductType", MedicationType.getText());
            values.put("Quantity", "0");


            db.InsertQuery("products", values);
            log.AddItem(db.logedInUser, itemNameEnglish.getText());
            setInCenter();
        }
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

    @FXML
    private void resetFields() {
        medicationBarcode.setText("");
        itemNameArabic.setText("");
        itemNameEnglish.setText("");
        Manufacturer.setText("");
        ExpDate.setValue(null);
        SellingPrice.setText("");
        PurchasePrice.setText("");
        ReorderLevel.setText("");
        MedicationType.setText("");
    }

    public void initialize() {

        List<Map<String, Object>> allItems = db.SelectQuery("products");
        int lastItemId = allItems.isEmpty() ? 0 : Integer.parseInt(allItems.get(allItems.size() - 1).get("ProductID").toString());
        ItemCode.setText(Integer.toString(lastItemId + 1));

        if (DC.Language.equals("en")) {
            productcode.setText("Product Code");
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
            productcode.setText("كود المنتج");
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
