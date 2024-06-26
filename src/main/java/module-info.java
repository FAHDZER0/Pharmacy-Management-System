module project.pharmacyv1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.management;
    requires TrayTester;
    requires jdk.compiler;
    requires pdfbox.app;
    requires com.ibm.icu;

    exports project.pharmacyv1.Customers to javafx.fxml;
    opens project.pharmacyv1.Customers to javafx.fxml;
    exports project.pharmacyv1;
    opens project.pharmacyv1 to javafx.fxml;
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
    exports project.pharmacyv1.GeneralAccounts;
    opens project.pharmacyv1.GeneralAccounts to javafx.fxml;
    exports project.pharmacyv1.Notification;
    opens project.pharmacyv1.Notification to javafx.fxml;
}