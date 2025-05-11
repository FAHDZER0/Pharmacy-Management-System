package project.pharmacyv1.Categories;

import Config.LanguageSetter;
import Classes.Medicine;
import DOAs.MedicineDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import project.pharmacyv1.DashboardController;
import project.pharmacyv1.LogWriter;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ListOfItemController {

    @FXML
    private Label Categories1Title;
    @FXML
    private ComboBox<String> choice1;
    @FXML
    private BorderPane SecondaryMainBorderPane;
    @FXML
    private TableView<Medicine> ItemListTableView;
    @FXML
    private TextField SearchTextField;
    @FXML
    private Label NumberOfItems;
    @FXML
    private Label Categories1Title1;
    @FXML
    private Button saveeditbutton;
    @FXML
    private Button deletebutton;
    @FXML
    private Button addbutton;

    LogWriter log = new LogWriter();
    DashboardController DC = new DashboardController();
    LanguageSetter LS = new LanguageSetter();

    private String getSearchTextField() {
        return SearchTextField.getText();
    }

    private final MedicineDAO medicineDAO = new MedicineDAO(); // Use DAO for database interactions
    private ObservableList<Medicine> medicineObservableList = FXCollections.observableArrayList();

    public void initialize() {
        // Set up table columns
        setupTableColumns();

        // Add listeners to search field and filters
        searchEvents();

        // Load initial data
        loadMedicines();

        if(DC.Language.equals("en")){
            Categories1Title.setText(LS.il8n("Categories1","en"));
            deletebutton.setText("Delete");
            saveeditbutton.setText("Save Edit");
            addbutton.setText("Add");
            Categories1Title1.setText("Number of Items:");
            choice1.setItems(FXCollections.observableArrayList("Medication English Name", "Medication Arabic Name" ,"Manufacturing Company","Active Ingredient"));
        } else if(DC.Language.equals("ar")){
            Categories1Title.setText(LS.il8n("Categories1","ar"));
            deletebutton.setText("حذف");
            saveeditbutton.setText("حفظ التعديل");
            addbutton.setText("إضافة");
            Categories1Title1.setText("عدد العناصر:");
            choice1.setItems(FXCollections.observableArrayList("اسم الدواء بالإنجليزية", "اسم الدواء بالعربية" ,"شركة التصنيع","المادة الفعالة"));

        }

    }

    private void setupTableColumns() {
        ItemListTableView.getColumns().clear();

        String[] columnNames = {
                "Barcode", "English Name", "Arabic Name", "International Code",
                "Active Ingredient", "Manufacturer", "Expiry Date", "Unit",
                "Quantity", "Selling Price", "Purchase Price", "Reorder Level",
                "Medication Type"
        };

        for (String columnName : columnNames) {
            TableColumn<Medicine, String> column = new TableColumn<>(columnName);

            column.setCellValueFactory(data -> {
                Medicine medicine = data.getValue();
                return switch (columnName) {
                    case "Barcode" -> new SimpleStringProperty(medicine.getBarcode());
                    case "English Name" -> new SimpleStringProperty(medicine.getEnglishName());
                    case "Arabic Name" -> new SimpleStringProperty(medicine.getArabicName());
                    case "International Code" -> new SimpleStringProperty(medicine.getInternationalCode());
                    case "Active Ingredient" -> new SimpleStringProperty(medicine.getActiveIngredient());
                    case "Manufacturer" -> new SimpleStringProperty(medicine.getManufacturer());
                    case "Expiry Date" -> new SimpleStringProperty(medicine.getExpiryDate().toString());
                    case "Unit" -> new SimpleStringProperty(medicine.getUnit());
                    case "Quantity" -> new SimpleStringProperty(String.valueOf(medicine.getQuantity()));
                    case "Selling Price" -> new SimpleStringProperty(String.valueOf(medicine.getSellingPrice()));
                    case "Purchase Price" -> new SimpleStringProperty(String.valueOf(medicine.getPurchasePrice()));
                    case "Reorder Level" -> new SimpleStringProperty(String.valueOf(medicine.getReorderLevel()));
                    case "Medication Type" -> new SimpleStringProperty(medicine.getMedicationType());
                    default -> null;
                };
            });

            if (columnName.equals("English Name") || columnName.equals("Arabic Name")) {
                column.setCellFactory(TextFieldTableCell.forTableColumn());
                column.setOnEditCommit(event -> {
                    Medicine selectedMedicine = event.getRowValue();
                    if (columnName.equals("English Name")) {
                        selectedMedicine.setEnglishName(event.getNewValue());
                    } else if (columnName.equals("Arabic Name")) {
                        selectedMedicine.setArabicName(event.getNewValue());
                    }
                    medicineDAO.update(selectedMedicine);
                });
            }

            ItemListTableView.getColumns().add(column);
        }

        // Enable table editing
        ItemListTableView.setEditable(true);
    }

    private void loadMedicines() {
        // Fetch all medicines from the database using MedicineDAO
        List<Medicine> medicines = medicineDAO.findAll();
        medicineObservableList.setAll(medicines);
        ItemListTableView.setItems(medicineObservableList);

        // Update the number of items label
        NumberOfItems.setText(String.valueOf(medicineObservableList.size()));
    }

    @FXML
    private void RefreshButtonAction() {
        // Refresh the table by reloading data
        loadMedicines();
    }

    @FXML
    private void EditSelectedRow() {
        Medicine selectedMedicine = ItemListTableView.getSelectionModel().getSelectedItem();
        if (selectedMedicine != null) {
            // Save edited medicine to the database
            medicineDAO.update(selectedMedicine);
            RefreshButtonAction();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Row Selected", "Please select a row to edit.");
        }
    }

    @FXML
    private void DeleteSelectedRow() {
        Medicine selectedMedicine = ItemListTableView.getSelectionModel().getSelectedItem();
        if (selectedMedicine != null) {
            // Delete the medicine from the database
            medicineDAO.delete(selectedMedicine.getId());
            RefreshButtonAction();
            tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
            AnimationType type = AnimationType.POPUP;
            tray.setAnimationType(type);
            tray.setTitle("Success");
            tray.setMessage("Medication Deleted Successfully");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(javafx.util.Duration.seconds(2));

        } else {
            showAlert(Alert.AlertType.WARNING, "No Row Selected", "Please select a row to delete.");
        }
    }

    @FXML
    public void setInCenter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/pharmacyv1/Categories/AddCategories.fxml"));
            BorderPane secondaryContent = loader.load();
            SecondaryMainBorderPane.setCenter(secondaryContent);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the Add Categories screen.");
        }
    }

    private void searchEvents() {
        SearchTextField.textProperty().addListener((observable, oldValue, newValue) -> filterMedicines());
        choice1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterMedicines());
    }

    private void filterMedicines() {
        String searchText = SearchTextField.getText().toLowerCase();
        String filterOption = choice1.getValue();

        List<Medicine> filteredList = medicineDAO.findAll().stream()
                .filter(medicine -> {
                    if (filterOption == null || searchText.isEmpty()) {
                        return true;
                    }
                    switch (filterOption) {
                        case "Medication English Name":
                            return medicine.getEnglishName().toLowerCase().contains(searchText);
                        case "Medication Arabic Name":
                            return medicine.getArabicName().toLowerCase().contains(searchText);
                        case "Manufacturing Company":
                            return medicine.getManufacturer().toLowerCase().contains(searchText);
                        case "Active Ingredient":
                            return medicine.getActiveIngredient().toLowerCase().contains(searchText);
                        default:
                            return true;
                    }
                }).collect(Collectors.toList());

        medicineObservableList.setAll(filteredList);
        NumberOfItems.setText(String.valueOf(filteredList.size()));
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}