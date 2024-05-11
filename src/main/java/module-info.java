module project.pharmacyv1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.management;
    requires TrayTester;
    requires jdk.compiler;


    opens project.pharmacyv1 to javafx.fxml;
    exports project.pharmacyv1;
    exports project.pharmacyv1.Categories;
    opens project.pharmacyv1.Categories to javafx.fxml;
    exports project.pharmacyv1.Purchase;
    opens project.pharmacyv1.Purchase to javafx.fxml;
    exports project.pharmacyv1.Sales;
    opens project.pharmacyv1.Sales to javafx.fxml;
    exports project.pharmacyv1.Suppliers;
    opens project.pharmacyv1.Suppliers to javafx.fxml;
    exports project.pharmacyv1.Warehouse;
    opens project.pharmacyv1.Warehouse to javafx.fxml;
}