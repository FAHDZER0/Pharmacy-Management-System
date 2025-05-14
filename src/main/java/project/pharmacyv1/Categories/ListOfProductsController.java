package project.pharmacyv1.Categories;

import Config.LanguageSetter;
import Classes.Product;
import DAOs.ProductDAO;
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

public class ListOfProductsController {

    @FXML
    private Label Categories2Title;
    @FXML
    private ComboBox<String> choice1;
    @FXML
    private BorderPane SecondaryMainBorderPane;
    @FXML
    private TableView<Product> ItemListTableView;
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

    LogWriter log = LogWriter.getInstance();
    DashboardController DC = new DashboardController();
    LanguageSetter LS = new LanguageSetter();

    private String getSearchTextField() {
        return SearchTextField.getText();
    }

    private final ProductDAO productDAO = new ProductDAO(); // Use DAO for database interactions
    private ObservableList<Product> productObservableList = FXCollections.observableArrayList();

    public void initialize() {
        // Set up table columns
        setupTableColumns();

        // Add listeners to search field and filters
        searchEvents();

        // Load initial data
        loadProducts();

        if(DC.Language.equals("en")){
            Categories2Title.setText(LS.il8n("Categories2","en"));
            deletebutton.setText("Delete");
            saveeditbutton.setText("Save Edit");
            addbutton.setText("Add");
            Categories1Title1.setText("Number of Items:");
            choice1.setItems(FXCollections.observableArrayList("Product English Name", "Product Arabic Name" ,"Manufacturing Company"));
        } else if(DC.Language.equals("ar")){
            Categories2Title.setText(LS.il8n("Categories2","ar"));
            deletebutton.setText("حذف");
            saveeditbutton.setText("حفظ التعديل");
            addbutton.setText("إضافة");
            Categories1Title1.setText("عدد العناصر:");
            choice1.setItems(FXCollections.observableArrayList("اسم المنتج بالإنجليزية", "اسم المنتج بالعربية" ,"شركة التصنيع"));
        }

        choice1.getSelectionModel().select(0);

    }

    private void setupTableColumns() {
        ItemListTableView.getColumns().clear();

        String[] columnNames = {
                "Barcode", "English Name", "Arabic Name", "Manufacturer",
                "Expiry Date", "Quantity", "Selling Price", "Purchase Price",
                "Reorder Level", "Product Type"
        };

        for (String columnName : columnNames) {
            TableColumn<Product, String> column = new TableColumn<>(columnName);

            column.setCellValueFactory(data -> {
                Product product = data.getValue();
                return switch (columnName) {
                    case "Barcode" -> new SimpleStringProperty(product.getBarcode());
                    case "English Name" -> new SimpleStringProperty(product.getEnglishName());
                    case "Arabic Name" -> new SimpleStringProperty(product.getArabicName());
                    case "Manufacturer" -> new SimpleStringProperty(product.getManufacturer());
                    case "Expiry Date" -> new SimpleStringProperty(product.getExpiryDate().toString());
                    case "Quantity" -> new SimpleStringProperty(String.valueOf(product.getQuantity()));
                    case "Selling Price" -> new SimpleStringProperty(String.valueOf(product.getSellingPrice()));
                    case "Purchase Price" -> new SimpleStringProperty(String.valueOf(product.getPurchasePrice()));
                    case "Reorder Level" -> new SimpleStringProperty(String.valueOf(product.getReorderLevel()));
                    case "Product Type" -> new SimpleStringProperty(product.getProductType());
                    default -> null;
                };
            });

            if (columnName.equals("English Name") || columnName.equals("Arabic Name")) {
                column.setCellFactory(TextFieldTableCell.forTableColumn());
                column.setOnEditCommit(event -> {
                    Product selectedProduct = event.getRowValue();
                    if (columnName.equals("English Name")) {
                        selectedProduct.setEnglishName(event.getNewValue());
                    } else if (columnName.equals("Arabic Name")) {
                        selectedProduct.setArabicName(event.getNewValue());
                    }
                    productDAO.update(selectedProduct);
                });
            }

            ItemListTableView.getColumns().add(column);
        }

        // Enable table editing
        ItemListTableView.setEditable(true);
    }

    private void loadProducts() {
        // Fetch all products from the database using ProductDAO
        List<Product> products = productDAO.findAll();
        productObservableList.setAll(products);
        ItemListTableView.setItems(productObservableList);

        // Update the number of items label
        NumberOfItems.setText(String.valueOf(productObservableList.size()));
    }

    @FXML
    private void RefreshButtonAction() {
        // Refresh the table by reloading data
        loadProducts();
    }

    @FXML
    private void EditSelectedRow() {
        Product selectedProduct = ItemListTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            // Save edited product to the database
            productDAO.update(selectedProduct);
            RefreshButtonAction();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Row Selected", "Please select a row to edit.");
        }
    }

    @FXML
    private void DeleteSelectedRow() {
        Product selectedProduct = ItemListTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            // Delete the product from the database
            productDAO.delete(selectedProduct.getId());
            RefreshButtonAction();
            tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
            AnimationType type = AnimationType.POPUP;
            tray.setAnimationType(type);
            tray.setTitle("Success");
            tray.setMessage("Product Deleted Successfully");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(javafx.util.Duration.seconds(2));

        } else {
            showAlert(Alert.AlertType.WARNING, "No Row Selected", "Please select a row to delete.");
        }
    }

    @FXML
    public void setInCenter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/pharmacyv1/Categories/AddProducts.fxml"));
            BorderPane secondaryContent = loader.load();
            SecondaryMainBorderPane.setCenter(secondaryContent);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the Add Categories screen.");
        }
    }

    private void searchEvents() {
        SearchTextField.textProperty().addListener((observable, oldValue, newValue) -> filterProducts());
        choice1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filterProducts());
    }

    private void filterProducts() {
        String searchText = SearchTextField.getText().toLowerCase();
        String filterOption = choice1.getValue();

        List<Product> filteredList = productDAO.findAll().stream()
                .filter(product -> {
                    if (filterOption == null || searchText.isEmpty()) {
                        return true;
                    }
                    switch (filterOption) {
                        case "Product English Name":
                            return product.getEnglishName().toLowerCase().contains(searchText);
                        case "Product Arabic Name":
                            return product.getArabicName().toLowerCase().contains(searchText);
                        case "Manufacturing Company":
                            return product.getManufacturer().toLowerCase().contains(searchText);
                        default:
                            return true;
                    }
                }).collect(Collectors.toList());

        productObservableList.setAll(filteredList);
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