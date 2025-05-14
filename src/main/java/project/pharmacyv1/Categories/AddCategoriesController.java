package project.pharmacyv1.Categories;

import Classes.Medicine;
import DAOs.MedicineDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import project.pharmacyv1.Dashboard.DashboardController;
import project.pharmacyv1.LogWriter;


public class AddCategoriesController {

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
    private Button canceladd;
    @FXML
    private Button savebutton;
    @FXML
    private Button newbutton;

    LogWriter log = LogWriter.getInstance();
    private final MedicineDAO medicineDAO = new MedicineDAO(); // DAO instance for database interaction

    /**
     * Handles the action when the "Add New Item" button is clicked.
     */
    @FXML
    public void AddItemButtonAction() {
        try {
            // Validate the required fields
            if (!validateInputs()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill out all required fields.");
                return;
            }

            // Build a new Medicine object using the Builder pattern
            Medicine newMedicine = new Medicine.MedicineBuilder()
                    .barcode(medicationBarcode.getText())
                    .englishName(itemNameEnglish.getText())
                    .arabicName(itemNameArabic.getText())
                    .internationalCode(internationalCode.getText())
                    .activeIngredient(ActiveIngrid.getText())
                    .manufacturer(Manufacturer.getText())
                    .expiryDate(ExpDate.getValue())
                    .unit(Unit.getText())
                    .sellingPrice(Double.parseDouble(SellingPrice.getText()))
                    .purchasePrice(Double.parseDouble(PurchasePrice.getText()))
                    .reorderLevel(Double.parseDouble(ReorderLevel.getText()))
                    .medicationType(MedicationType.getText())
                    .build();

            // Save the new medicine to the database using MedicineDAO
            medicineDAO.save(newMedicine);

            // Show success notification
            showAlert(Alert.AlertType.INFORMATION, "Success", "New medicine added successfully.");
            resetFields();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the new medicine. Please try again.");
        }
    }

    /**
     * Resets all input fields to their default state.
     */
    @FXML
    private void resetFields() {
        internationalCode.clear();
        medicationBarcode.clear();
        itemNameArabic.clear();
        itemNameEnglish.clear();
        ActiveIngrid.clear();
        Manufacturer.clear();
        ExpDate.setValue(null);
        Unit.clear();
        SellingPrice.clear();
        PurchasePrice.clear();
        ReorderLevel.clear();
        MedicationType.clear();
    }

    /**
     * Validates the input fields to ensure no required field is empty.
     *
     * @return true if all required fields are filled, false otherwise.
     */
    private boolean validateInputs() {
        return !(
                medicationBarcode.getText().isEmpty() ||
                        itemNameEnglish.getText().isEmpty() ||
                        itemNameArabic.getText().isEmpty() ||
                        ActiveIngrid.getText().isEmpty() ||
                        Manufacturer.getText().isEmpty() ||
                        ExpDate.getValue() == null ||
                        Unit.getText().isEmpty() ||
                        SellingPrice.getText().isEmpty() ||
                        PurchasePrice.getText().isEmpty() ||
                        ReorderLevel.getText().isEmpty() ||
                        MedicationType.getText().isEmpty()
        );
    }

    /**
     * Displays an alert dialog.
     *
     * @param alertType The type of alert to display (e.g., INFORMATION, WARNING, ERROR).
     * @param title     The title of the alert dialog.
     * @param content   The content message of the alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void initialize() {

        DashboardController DC = new DashboardController();

        if (DC.Language.equals("en")) {
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