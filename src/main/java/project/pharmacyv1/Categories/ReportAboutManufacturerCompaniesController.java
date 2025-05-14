package project.pharmacyv1.Categories;

import Config.LanguageSetter;
import Config.PDFprinterController;
import DAOs.MedicineDAO;
import DAOs.ProductDAO;
import Classes.Medicine;
import Classes.Product;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import project.pharmacyv1.DashboardController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.*;

public class ReportAboutManufacturerCompaniesController {

    @FXML private Label Categories1Title;
    @FXML private ComboBox<String> choice1;
    @FXML private TableView<Map<String, Object>> ItemListTableView;
    @FXML private PieChart ManufacturerPiechart;
    @FXML private Button PrintButton;
    private boolean displayAsPercentage = false;

    // Use DAOs instead of DB helper
    private final MedicineDAO medicineDAO = new MedicineDAO();
    private final ProductDAO productDAO   = new ProductDAO();

    private final DashboardController DC = new DashboardController();

    @FXML
    public void initialize() {
        // Populate manufacturer list
        Set<String> manufacturers = new LinkedHashSet<>();
        medicineDAO.findAll().forEach(m -> manufacturers.add(m.getManufacturer()));
        productDAO.findAll().forEach(p -> manufacturers.add(p.getManufacturer()));
        choice1.setItems(FXCollections.observableArrayList(manufacturers));
        choice1.getSelectionModel().selectFirst();
        choice1.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> RefreshButtonAction());

        // Kick off the chart and table
        setPieChartData();
        RefreshButtonAction();

        // Localization
        LanguageSetter LS = LanguageSetter.getInstance();
        if ("en".equals(DC.Language)) {
            Categories1Title.setText(LS.il8n("Categories7","en"));
            PrintButton.setText("Print");
        } else {
            Categories1Title.setText(LS.il8n("Categories7","ar"));
            PrintButton.setText("طباعة");
        }
    }

    public void RefreshButtonAction() {
        String selectedMan = choice1.getSelectionModel().getSelectedItem();
        if (selectedMan == null) return;

        // Fetch filtered data from DAOs
        List<Medicine> meds = medicineDAO.findByManufacturer(selectedMan);
        List<Product> prods = productDAO.findByManufacturer(selectedMan);

        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

        // Convert Medicine → Map
        for (Medicine m : meds) {
            Map<String, Object> row = new HashMap<>();
            row.put("Manufacturer", m.getManufacturer());
            row.put("MedicationID", m.getId());
            row.put("EnglishName", m.getEnglishName());
            row.put("ArabicName", m.getArabicName());
            row.put("Quantity", m.getQuantity());
            data.add(row);
        }

        // Convert Product → Map
        for (Product p : prods) {
            Map<String, Object> row = new HashMap<>();
            row.put("Manufacturer", p.getManufacturer());
            row.put("ProductID", p.getId());
            row.put("EnglishName", p.getEnglishName());
            row.put("ArabicName", p.getArabicName());
            row.put("Quantity", p.getQuantity());
            data.add(row);
        }

        fillTable(ItemListTableView, data);
    }

    private void fillTable(TableView<Map<String, Object>> tableView, ObservableList<Map<String, Object>> dataList) {
        tableView.getColumns().clear();

        if (!dataList.isEmpty()) {
            for (String columnName : dataList.get(0).keySet()) {
                TableColumn<Map<String, Object>, String> col = new TableColumn<>(columnName);
                col.setCellValueFactory(cell -> {
                    Object v = cell.getValue().get(columnName);
                    return new SimpleObjectProperty<>(v == null ? "" : v.toString());
                });
                tableView.getColumns().add(col);
            }
        }

        tableView.setItems(dataList);
    }

    private void setPieChartData() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), ev -> {
            ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();

            // All medicines
            List<Medicine> allMeds = medicineDAO.findAll();
            double totalMedQty = allMeds.stream().mapToDouble(Medicine::getQuantity).sum();
            for (Medicine m : allMeds) {
                double qty = m.getQuantity();
                String label = m.getEnglishName();
                if (displayAsPercentage) {
                    label += String.format(" (%.2f%%)", qty / totalMedQty * 100);
                } else {
                    label += String.format(" (%.0f)", qty);
                }
                chartData.add(new PieChart.Data(label, qty));
            }

            // All products
            List<Product> allProds = productDAO.findAll();
            double totalProdQty = allProds.stream().mapToDouble(Product::getQuantity).sum();
            for (Product p : allProds) {
                double qty = p.getQuantity();
                String label = p.getEnglishName();
                if (displayAsPercentage) {
                    label += String.format(" (%.2f%%)", qty / totalProdQty * 100);
                } else {
                    label += String.format(" (%.0f)", qty);
                }
                chartData.add(new PieChart.Data(label, qty));
            }

            ManufacturerPiechart.setData(chartData);
            displayAsPercentage = !displayAsPercentage;
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    public void PrintButtonAction() {
        new PDFprinterController().printTableIntoPDF(ItemListTableView.getItems(), false);
    }
}
