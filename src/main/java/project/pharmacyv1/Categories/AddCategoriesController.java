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

public class AddCategoriesController {

    @FXML
    private TextField ItemCode;
    @FXML
    private TextField internationalCode;
    @FXML
    private TextField medicationBarcode;
    @FXML
    private TextField itemNameArabic;
    @FXML
    private TextField itemNameEnglish;
    @FXML
    private TextField ActiveIngrid;
    @FXML
    private TextField Manufacturer;
    @FXML
    private DatePicker ExpDate;
    @FXML
    private TextField Unit;
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
    private Label itemscodelabel;
    @FXML
    private Label internationalcode;
    @FXML
    private Label barcode;
    @FXML
    private Label arabicname;
    @FXML
    private Label englishname;
    @FXML
    private Label activeingrid;
    @FXML
    private Label manu;
    @FXML
    private Label expdate;
    @FXML
    private Label unit;
    @FXML
    private Label sellingprice;
    @FXML
    private Label purchaseprice;
    @FXML
    private Label reorderlevel;
    @FXML
    private Label medtype;
    @FXML
    private Label addnewmed;
    @FXML
    private Label bigtitle;
    @FXML
    private Button newbutton;
    @FXML
    private Button savebutton;
    @FXML
    private Button canceladd;


    DB db = new DB();
    LogWriter log = new LogWriter();

    public void AddItemButtonAction() {
        // Add a new item to the database
        if (ItemCode.getText().isEmpty() || internationalCode.getText().isEmpty() || medicationBarcode.getText().isEmpty() || itemNameArabic.getText().isEmpty() || itemNameEnglish.getText().isEmpty() || ActiveIngrid.getText().isEmpty() || Manufacturer.getText().isEmpty() || ExpDate.getValue() == null || Unit.getText().isEmpty() || SellingPrice.getText().isEmpty() || PurchasePrice.getText().isEmpty() || ReorderLevel.getText().isEmpty() || MedicationType.getText().isEmpty()) {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Data");
            alert.setContentText("Please enter needed data correctly");
            alert.showAndWait();
        } else {
            Map<String, Object> values = new HashMap<>();
            values.put("MedicationID", ItemCode.getText());
            values.put("InternationalCode", internationalCode.getText());
            values.put("MedicationBarcode", medicationBarcode.getText());
            values.put("ArabicName", itemNameArabic.getText());
            values.put("EnglishName", itemNameEnglish.getText());
            values.put("ActiveIngredient", ActiveIngrid.getText());
            values.put("Manufacturer", Manufacturer.getText());
            values.put("ExpiryDate", ExpDate.getValue().toString());
            values.put("Unit", Unit.getText());
            values.put("SellingPrice", SellingPrice.getText());
            values.put("PurchasePrice", PurchasePrice.getText());
            values.put("ReorderLevel", ReorderLevel.getText());
            values.put("MedicationType", MedicationType.getText());
            values.put("Quantity", "0");

            db.InsertQuery("medications", values);
            log.AddItem(db.logedInID, itemNameEnglish.getText());
            setInCenter();
        }
    }

    @FXML
    public void setInCenter() {
        System.out.println("Add Categories");
        SecondaryMainBorderPane.setCenter(null);
        // Extract substring before the underscore character, if exists
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/pharmacyv1/Categories/ListOfItem.fxml"));

            BorderPane secondaryContent = loader.load();
            SecondaryMainBorderPane.setCenter(secondaryContent);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error

        }
    }

    @FXML
    private void resetFields() {
        internationalCode.setText("");
        medicationBarcode.setText("");
        itemNameArabic.setText("");
        itemNameEnglish.setText("");
        ActiveIngrid.setText("");
        Manufacturer.setText("");
        ExpDate.setValue(null);
        Unit.setText("");
        SellingPrice.setText("");
        PurchasePrice.setText("");
        ReorderLevel.setText("");
        MedicationType.setText("");
    }

    public void initialize() {

        List<Map<String, Object>> allItems = db.SelectQuery("medications");
        int lastItemId = allItems.isEmpty() ? 0 : Integer.parseInt(allItems.get(allItems.size() - 1).get("MedicationID").toString());
        ItemCode.setText(Integer.toString(lastItemId + 1));

        DashboardController DC = new DashboardController();

        if (DC.Language.equals("en")) {
            itemscodelabel.setText("Item Code");
            internationalcode.setText("International Code");
            barcode.setText("Barcode");
            arabicname.setText("Arabic Name");
            englishname.setText("English Name");
            activeingrid.setText("Active Ingredient");
            manu.setText("Manufacturer");
            expdate.setText("Expiry Date");
            unit.setText("Unit");
            sellingprice.setText("Selling Price");
            purchaseprice.setText("Purchase Price");
            reorderlevel.setText("Reorder Level");
            medtype.setText("Medication Type");
            addnewmed.setText("Add New Medication");
            bigtitle.setText("Add New Medication");
            canceladd.setText("Cancel");
            savebutton.setText("Save");
            newbutton.setText("New");
        } else if (DC.Language.equals("ar")) {
            itemscodelabel.setText("كود الصنف");
            internationalcode.setText("الكود الدولي");
            barcode.setText("الباركود");
            arabicname.setText("الاسم بالعربي");
            englishname.setText("الاسم بالانجليزي");
            activeingrid.setText("المادة الفعالة");
            manu.setText("الشركة المصنعة");
            expdate.setText("تاريخ الانتهاء");
            unit.setText("الوحدة");
            sellingprice.setText("سعر البيع");
            purchaseprice.setText("سعر الشراء");
            reorderlevel.setText("الحد الادنى");
            medtype.setText("نوع الدواء");
            addnewmed.setText("اضافة دواء جديد");
            bigtitle.setText("اضافة صنف جديد");
            canceladd.setText("الغاء");
            savebutton.setText("حفظ");
            newbutton.setText("جديد");
        }


    }

}