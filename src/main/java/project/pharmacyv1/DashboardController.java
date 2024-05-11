package project.pharmacyv1;

import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

import Config.LanguageSetter;
import Database.DB;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class DashboardController {

    public MenuItem Sales;
    public MenuItem Purchase;
    //  Identify Buttons and Set hover and click actions______________________________//|
    @FXML
    public GridPane DashboardMain;
    @FXML
    private Button GeneralInformation;
    @FXML
    private MenuButton CategoriesMain;
    @FXML
    private MenuButton WarehousesMain;
    @FXML
    private MenuButton SuppliersMain;
    @FXML
    private MenuButton PurchasesMain;
    @FXML
    private MenuButton CustomersMain;
    @FXML
    private MenuButton SalesMain;
    @FXML
    private MenuButton GeneralAccountsMain;
    @FXML
    private MenuButton OrdersMain;
    @FXML
    private MenuButton EmployeesAffairsMain;
    @FXML
    private MenuButton FrameworkMain;

    private MenuButton currentButton; //Reference to the currently pressed button

    @FXML
    private Button Deficiencies_side;
    @FXML
    private Button ListOfItem_side;
    @FXML
    private Button PurchaseInvoice_side;
    @FXML
    private Button SalesInvoice_side;
    @FXML
    private Button Customers_side;
    @FXML
    private Button cashSupplay_side;

    private void setHoverEffect(Control control) {

        control.setOnMouseEntered(event -> {
            control.setStyle("-fx-background-color: white; -fx-font-size:13; -fx-font-weight:bold;");
        });

        control.setOnMouseExited(event -> {
            // If the button is not currently pressed, keep it blue and big
            if (control != currentButton) {
                control.setStyle("-fx-background-color:  transparent; -fx-font-size:13; -fx-font-weight:bold;");
            }
        });
    }

    //    __________________________________________________________________________
    @FXML
    private Label TotalSalesLabel;
    @FXML
    private Label purchaseMedicinesLabel;
    @FXML
    private Label profitThisMonthLabel;
    @FXML
    private PieChart purchaseMedicinesChart;
    @FXML
    private PieChart profitThisMonthChart;
    @FXML
    private Label totalMedicineValue;
    @FXML
    private Label expiryThisMonthValue;
    @FXML
    private Label outOfStockValue;

    private FadeTransition purchaseMedicinesLabelFadeInAnimation;
    private FadeTransition purchaseMedicinesLabelFadeOutAnimation;
    private FadeTransition profitThisMonthLabelFadeInAnimation;
    private FadeTransition profitThisMonthLabelFadeOutAnimation;
    private FadeTransition purchaseMedicinesChartFadeInAnimation;
    private FadeTransition purchaseMedicinesChartFadeOutAnimation;
    private FadeTransition profitThisMonthChartFadeInAnimation;
    private FadeTransition profitThisMonthChartFadeOutAnimation;

    // Timeline for periodically triggering animations
    private Timeline animationTimeline;

    private void setPieChartValues() {

        try {
            // Parse the numbers with commas from the labels
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            double purchaseMedicinesChartData = numberFormat.parse(purchaseMedicinesLabel.getText()).doubleValue();
            double profitThisMonthChartData = numberFormat.parse(profitThisMonthLabel.getText()).doubleValue();
            double TotalSales = numberFormat.parse(TotalSalesLabel.getText()).doubleValue();


            // Calculate percentages
            purchaseMedicinesChartData = (purchaseMedicinesChartData / TotalSales) * 100;
            profitThisMonthChartData = (profitThisMonthChartData / TotalSales) * 100;

            // Add data to pie charts
            purchaseMedicinesChart.getData().add(new PieChart.Data("Category 1", purchaseMedicinesChartData));
            purchaseMedicinesChart.getData().add(new PieChart.Data("Category 2", 100 - purchaseMedicinesChartData));

            profitThisMonthChart.getData().add(new PieChart.Data("Category A", profitThisMonthChartData));
            profitThisMonthChart.getData().add(new PieChart.Data("Category B", 100 - profitThisMonthChartData));
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle parsing exception
        }
    }

    private void setAnimationDashboard() {
        // Create fade animations for PieCharts without delay
        purchaseMedicinesLabelFadeInAnimation = createFadeAnimation(purchaseMedicinesLabel, 0, 1);
        purchaseMedicinesLabelFadeOutAnimation = createFadeAnimation(purchaseMedicinesLabel, 1, 0);
        profitThisMonthLabelFadeInAnimation = createFadeAnimation(profitThisMonthLabel, 0, 1);
        profitThisMonthLabelFadeOutAnimation = createFadeAnimation(profitThisMonthLabel, 1, 0);
        purchaseMedicinesChartFadeInAnimation = createFadeAnimation(purchaseMedicinesChart, 0, 1);
        purchaseMedicinesChartFadeOutAnimation = createFadeAnimation(purchaseMedicinesChart, 1, 0);
        profitThisMonthChartFadeInAnimation = createFadeAnimation(profitThisMonthChart, 0, 1);
        profitThisMonthChartFadeOutAnimation = createFadeAnimation(profitThisMonthChart, 1, 0);

        // Initialize timeline for periodic animations
        animationTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> startFadeInAnimations()),
                new KeyFrame(Duration.seconds(5), event -> startFadeOutAnimations()),
                new KeyFrame(Duration.seconds(9)) // Empty keyframe for waiting
        );
        animationTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely

        // Start the timeline
        animationTimeline.play();
    }

    // Method to start fade in animations
    private void startFadeInAnimations() {
        purchaseMedicinesChartFadeInAnimation.play();
        profitThisMonthChartFadeInAnimation.play();
        purchaseMedicinesLabelFadeOutAnimation.play();
        profitThisMonthLabelFadeOutAnimation.play();
    }

    // Method to start fade out animations
    private void startFadeOutAnimations() {
        purchaseMedicinesChartFadeOutAnimation.play();
        profitThisMonthChartFadeOutAnimation.play();
        purchaseMedicinesLabelFadeInAnimation.play();
        profitThisMonthLabelFadeInAnimation.play();
    }

    private FadeTransition createFadeAnimation(Node node, double fromValue, double toValue) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), node);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        return fadeTransition;
    }

    @FXML
    public BorderPane MainBoarderPane;

    @FXML
    public void removeFromCenter() {
        this.MainBoarderPane.setCenter(null);
        this.MainBoarderPane.setCenter(DashboardMain);
        setDashboardData();
    }

    // method to change the style from light to dark
    @FXML
    public void setStyleToDark(){
        MainBoarderPane.setStyle("-fx-background-color: #000000;");
    }
    @FXML
    public void setStyleToLight(){
        MainBoarderPane.setStyle("-fx-background-color: #ffffff;");
    }

    @FXML
    public void setInCenter(ActionEvent event) {

        String MenuItemName = null;
        String temp = null;

        // conditions to check if the id of the buttons or menu items is SalesInvoice add Sales/ before it

        if (event.getSource() instanceof Button) {
            MenuItemName = ((Button) event.getSource()).getId();
            int underscoreIndex = MenuItemName.indexOf('_');
            if (underscoreIndex != -1) {
                MenuItemName = MenuItemName.substring(0, underscoreIndex);
            }
        } else if (event.getSource() instanceof MenuItem) {
            MenuItemName = ((MenuItem) event.getSource()).getId();
        }

        if (MenuItemName.equalsIgnoreCase("SalesInvoice") || MenuItemName.equalsIgnoreCase("FindItem_PopUp") ){
            MenuItemName = "Sales/" + MenuItemName;
        } else if (MenuItemName.equals("PurchaseInvoice")) {
            MenuItemName = "Purchase/" + MenuItemName;
        } else if (MenuItemName.equals("SuppliersList") || MenuItemName.equalsIgnoreCase("FindSupplier_PopUp") || MenuItemName.equalsIgnoreCase("ReportAboutSuppliers") || MenuItemName.equalsIgnoreCase("EditSupplierPrice")){
            MenuItemName = "Suppliers/" + MenuItemName;
        } else if (MenuItemName.equals("ListOfItem") || MenuItemName.equalsIgnoreCase("ListOfProducts") || MenuItemName.equals("ModifyItemSName") || MenuItemName.equals("ReportAbouTManifuctrurerCampanies")) {
            MenuItemName = "Categories/" + MenuItemName;
        } else if(MenuItemName.equals("EditCountQuantity") || MenuItemName.equalsIgnoreCase("InnerWarehouse") || MenuItemName.equalsIgnoreCase("ReportAboutEditingTables") || MenuItemName.equalsIgnoreCase("ReportAboutQuantityOfMedicineinStockExp") || MenuItemName.equalsIgnoreCase("ReportAboutExpiredItemsinStrock")){
            MenuItemName = "Warehouses/" + MenuItemName;
        }

        removeFromCenter();

        // Extract substring before the underscore character, if exists
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/pharmacyv1/" + MenuItemName +".fxml"));

            BorderPane secondaryContent = loader.load();
            MainBoarderPane.setCenter(secondaryContent);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error

        }
    }

    private void setSideHoverEffect(Control control){
        control.setOnMouseEntered(event -> {
            control.setStyle("-fx-background-color: #00ff00; -fx-background-radius: 100px; -fx-border-color: green; -fx-border-radius: 100px; -fx-border-width: 3px;");
        });

        control.setOnMouseExited(event -> {
            // If the button is not currently pressed, keep it blue and big
            if (control != currentButton) {
                control.setStyle("-fx-background-color: #00cc00; -fx-background-radius: 100px; -fx-border-color: green; -fx-border-radius: 100px; -fx-border-width: 3px;");
            }
        });

    }

    private Tooltip createCustomTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setFont(new Font("Arial", 15));
        tooltip.setShowDelay(Duration.millis(100));
        return tooltip;
    }

    @FXML
    private MenuItem ArabicLanguageMenu;
    @FXML
    private MenuItem EnglishLanguageMenu;

    @FXML
    private Label DashboardGreeting;
    @FXML
    private Label DashboardUserName;
    @FXML
    private Label DashboardGrid1;
    @FXML
    private Label DashboardGrid2;
    @FXML
    private Label DashboardGrid3;
    @FXML
    private Label DashboardGrid4;
    @FXML
    private Label DashboardGrid5;
    @FXML
    private Label DashboardGrid6;
    @FXML
    private Label LoginTime;
    @FXML
    private Label LoginTimeValue;
    @FXML
    private Label CurrentUser;
    @FXML
    private Button Logout;
    @FXML
    private MenuItem ListOfItem;
    @FXML
    private MenuItem ListOfProducts;
    @FXML
    private MenuItem ModifyItemSName;
    @FXML
    private MenuItem ReportAbouTManifuctrurerCampanies;
    @FXML
    private MenuItem innerWarehouse;
    @FXML
    private MenuItem EditCountQuantity;
    @FXML
    private MenuItem ReportAboutEditingTables;
    @FXML
    private MenuItem ReportAboutQuantityOfMedicineinStockExp;
    @FXML
    private MenuItem Warehouse6;
    @FXML
    private MenuItem ReportAboutExpiredItemsinStrock;
    @FXML
    private MenuItem SuppliersList;
    @FXML
    private MenuItem ReportAboutSuppliers;
    @FXML
    private MenuItem EditSupplierPrice;
    @FXML
    private MenuItem supplier4;
    @FXML
    private MenuItem PurchaseInvoice;
    @FXML
    private MenuItem Purchase2;
    @FXML
    private MenuItem Purchase4;
    @FXML
    private MenuItem Purchase5;
    @FXML
    private MenuItem Purchase6;
    @FXML
    private MenuItem Customer1;
    @FXML
    private MenuItem Customer2;
    @FXML
    private MenuItem Customer5;
    @FXML
    private MenuItem sales2;
    @FXML
    private MenuItem sales4;
    @FXML
    private MenuItem sales5;
    @FXML
    private MenuItem sales7;
    @FXML
    private MenuItem sales8;
    @FXML
    private MenuItem SalesInvoice;
    @FXML
    private MenuItem accounts1;
    @FXML
    private MenuItem accounts2;
    @FXML
    private MenuItem accounts3;
    @FXML
    private MenuItem accounts4;
    @FXML
    private MenuItem accounts5;
    @FXML
    private MenuItem accounts6;
    @FXML
    private MenuItem accounts7;
    @FXML
    private MenuItem accounts8;
    @FXML
    private MenuItem accounts9;
    @FXML
    private MenuItem accounts10;
    @FXML
    private MenuItem accounts11;
    @FXML
    private MenuItem accounts12;
    @FXML
    private MenuItem accounts13;
    @FXML
    private MenuItem accounts14;
    @FXML
    private MenuItem accounts15;
    @FXML
    private MenuItem accounts16;
    @FXML
    private MenuItem accounts17;
    @FXML
    private MenuItem accounts18;
    @FXML
    private MenuItem accounts19;
    @FXML
    private MenuItem Order1;
    @FXML
    private MenuItem Order2;
    @FXML
    private MenuItem Order3;
    @FXML
    private MenuItem EmployeesAffairs1;
    @FXML
    private MenuItem EmployeesAffairs2;
    @FXML
    private MenuItem EmployeesAffairs3;
    @FXML
    private MenuItem EmployeesAffairs5;
    @FXML
    private MenuItem EmployeesAffairs7;
    @FXML
    private MenuItem EmployeesAffairs8;
    @FXML
    private MenuItem EmployeesAffairs9;
    @FXML
    private MenuItem EmployeesAffairs11;
    @FXML
    private MenuItem EmployeesAffairs12;
    @FXML
    private MenuItem EmployeesAffairs13;
    @FXML
    private MenuItem EmployeesAffairs14;
    @FXML
    private MenuItem EmployeesAffairs15;
    @FXML
    private MenuItem EmployeesAffairs16;
    @FXML
    private MenuItem EmployeesAffairs17;
    @FXML
    private MenuItem EmployeesAffairs18;
    @FXML
    private Label miniUserName;


    static public String Language = "en";

    public void setLanguage(String language){
        if (language.equals("ar")) {
            MainBoarderPane.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);

            Deficiencies_side.setTooltip(createCustomTooltip("كشكول النواقص"));
            ListOfItem_side.setTooltip(createCustomTooltip("الأصناف"));
            PurchaseInvoice_side.setTooltip(createCustomTooltip("فاتورة شراء"));
            SalesInvoice_side.setTooltip(createCustomTooltip("فاتورة بيع"));
            Customers_side.setTooltip(createCustomTooltip("العملاء"));
            cashSupplay_side.setTooltip(createCustomTooltip("توريد نقدية"));
            Language = "ar";

        }
        else if (language.equals("en")) {
            MainBoarderPane.setNodeOrientation(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);

            //setting tooltips for side buttons
            Deficiencies_side.setTooltip(createCustomTooltip("Deficiencies"));
            ListOfItem_side.setTooltip(createCustomTooltip("Categories"));
            PurchaseInvoice_side.setTooltip(createCustomTooltip("Purchase Invoice"));
            SalesInvoice_side.setTooltip(createCustomTooltip("Sales Invoice"));
            Customers_side.setTooltip(createCustomTooltip("Customers"));
            cashSupplay_side.setTooltip(createCustomTooltip("Cash Supply"));
            Language = "en";
        }
        LanguageSetter LS = new LanguageSetter();
        DashboardGreeting.setText(LS.il8n("greeting",language));
        DashboardGrid1.setText(LS.il8n("Grid1",language));
        DashboardGrid2.setText(LS.il8n("Grid2",language));
        DashboardGrid3.setText(LS.il8n("Grid3",language));
        DashboardGrid4.setText(LS.il8n("Grid4",language));
        DashboardGrid5.setText(LS.il8n("Grid5",language));
        DashboardGrid6.setText(LS.il8n("Grid6",language));
        GeneralInformation.setText(LS.il8n("MenuOption1",language));
        CategoriesMain.setText(LS.il8n("MenuOption2",language));
        WarehousesMain.setText(LS.il8n("MenuOption3",language));
        SuppliersMain.setText(LS.il8n("MenuOption4",language));
        PurchasesMain.setText(LS.il8n("MenuOption5",language));
        CustomersMain.setText(LS.il8n("MenuOption6",language));
        SalesMain.setText(LS.il8n("MenuOption7",language));
        GeneralAccountsMain.setText(LS.il8n("MenuOption8",language));
        OrdersMain.setText(LS.il8n("MenuOption9",language));
        EmployeesAffairsMain.setText(LS.il8n("MenuOption10",language));
        FrameworkMain.setText(LS.il8n("MenuOption11",language));
        LoginTime.setText(LS.il8n("LoginTime",language));
        CurrentUser.setText(LS.il8n("CurrentUser",language));
        Logout.setText(LS.il8n("Logout",language));

        ListOfItem.setText(LS.il8n("Categories1",language));
        ListOfProducts.setText(LS.il8n("Categories2",language));
        ModifyItemSName.setText(LS.il8n("Categories5",language));
        ReportAbouTManifuctrurerCampanies.setText(LS.il8n("Categories7",language));
        innerWarehouse.setText(LS.il8n("Warehouse1",language));
        EditCountQuantity.setText(LS.il8n("Warehouse3",language));
        ReportAboutEditingTables.setText(LS.il8n("Warehouse4",language));
        ReportAboutQuantityOfMedicineinStockExp.setText(LS.il8n("Warehouse5",language));
        Warehouse6.setText(LS.il8n("Warehouse6",language));
        ReportAboutExpiredItemsinStrock.setText(LS.il8n("Warehouse7",language));
        SuppliersList.setText(LS.il8n("supplier1",language));
        ReportAboutSuppliers.setText(LS.il8n("supplier2",language));
        EditSupplierPrice.setText(LS.il8n("supplier3",language));
        supplier4.setText(LS.il8n("supplier4",language));
        PurchaseInvoice.setText(LS.il8n("purchase1",language));
        Purchase2.setText(LS.il8n("purchase2",language));
        Purchase4.setText(LS.il8n("purchase4",language));
        Purchase5.setText(LS.il8n("purchase5",language));
        Purchase6.setText(LS.il8n("purchase6",language));
        Customer1.setText(LS.il8n("customer1",language));
        Customer2.setText(LS.il8n("customer2",language));
        Customer5.setText(LS.il8n("customer5",language));
        SalesInvoice.setText(LS.il8n("sales1",language));
        sales2.setText(LS.il8n("sales2",language));
        sales4.setText(LS.il8n("sales4",language));
        sales5.setText(LS.il8n("sales5",language));
        sales7.setText(LS.il8n("sales7",language));
        sales8.setText(LS.il8n("sales8",language));
        accounts1.setText(LS.il8n("accounts1",language));
        accounts2.setText(LS.il8n("accounts2",language));
        accounts3.setText(LS.il8n("accounts3",language));
        accounts4.setText(LS.il8n("accounts4",language));
        accounts5.setText(LS.il8n("accounts5",language));
        accounts6.setText(LS.il8n("accounts6",language));
        accounts7.setText(LS.il8n("accounts7",language));
        accounts8.setText(LS.il8n("accounts8",language));
        accounts9.setText(LS.il8n("accounts9",language));
        accounts10.setText(LS.il8n("accounts10",language));
        accounts11.setText(LS.il8n("accounts11",language));
        accounts12.setText(LS.il8n("accounts12",language));
        accounts13.setText(LS.il8n("accounts13",language));
        accounts14.setText(LS.il8n("accounts14",language));
        accounts15.setText(LS.il8n("accounts15",language));
        accounts16.setText(LS.il8n("accounts16",language));
        accounts17.setText(LS.il8n("accounts17",language));
        accounts18.setText(LS.il8n("accounts18",language));
        accounts19.setText(LS.il8n("accounts19",language));
        Order1.setText(LS.il8n("Order1",language));
        Order2.setText(LS.il8n("Order2",language));
        Order3.setText(LS.il8n("Order3",language));
        EmployeesAffairs1.setText(LS.il8n("EmployeesAffairs1",language));
        EmployeesAffairs2.setText(LS.il8n("EmployeesAffairs2",language));
        EmployeesAffairs3.setText(LS.il8n("EmployeesAffairs3",language));
        EmployeesAffairs5.setText(LS.il8n("EmployeesAffairs5",language));
        EmployeesAffairs7.setText(LS.il8n("EmployeesAffairs7",language));
        EmployeesAffairs8.setText(LS.il8n("EmployeesAffairs8",language));
        EmployeesAffairs9.setText(LS.il8n("EmployeesAffairs9",language));
        EmployeesAffairs11.setText(LS.il8n("EmployeesAffairs11",language));
        EmployeesAffairs12.setText(LS.il8n("EmployeesAffairs12",language));
        EmployeesAffairs13.setText(LS.il8n("EmployeesAffairs13",language));
        EmployeesAffairs14.setText(LS.il8n("EmployeesAffairs14",language));
        EmployeesAffairs15.setText(LS.il8n("EmployeesAffairs15",language));
        EmployeesAffairs16.setText(LS.il8n("EmployeesAffairs16",language));
        EmployeesAffairs17.setText(LS.il8n("EmployeesAffairs17",language));
        EmployeesAffairs18.setText(LS.il8n("EmployeesAffairs18",language));


        removeFromCenter();
    }

    DB db = new DB();
    LoginController LC = new LoginController();

    private void setDashboardData(){
        LoginTimeValue.setText(LC.getLoginDate());

        Platform.runLater(() -> {
            if (!db.isLogedIn)
                Logout();
            else {
                DashboardUserName.setText(db.logedInUser);
                miniUserName.setText(db.logedInUser);
            }
        });

        int totalNumberofmedicines = db.SelectQuerySum("medications" , "Quantity");
        totalNumberofmedicines += db.SelectQuerySum("products" , "Quantity");
        totalMedicineValue.setText(String.valueOf(totalNumberofmedicines));

        // Calculate the number of expired items in the current month
        expiryThisMonthValue.setText(String.valueOf(db.selectQueryExpiredItemsQuantity()));

        // calculate the number of out of stock items
        int result = db.selectQueryOutOfStockItemsQuantity();
        outOfStockValue.setText(String.valueOf(result));

        // Calculate the total sales
        double totalSales = db.SelectQueryTotalSales();
        TotalSalesLabel.setText(String.valueOf(totalSales));

        // Calculate the total purchase of medicines where it gets the sum of the total cost column from the purchaseinvoices table
        double purchaseMedicines = db.SelectQueryTotalCost();
        purchaseMedicinesLabel.setText(String.valueOf(purchaseMedicines));

        // Calculate the profit of this month
        // it will calculate the profit of the last 30 days by
        // subtracting the sum of the sales the (totalSaleAmount column from the salesinvoices table) from the sum of the purchases (TotalCost column from the purchaseinvoices table)
        double profitThisMonth = db.calculateProfit();
        profitThisMonthLabel.setText(String.valueOf(profitThisMonth));



    }

    @FXML
    public void initialize() {


        Platform.runLater(() -> {
            setPieChartValues();
            setAnimationDashboard();
        });

        //setting tooltips for side buttons
        Deficiencies_side.setTooltip(createCustomTooltip("Deficiencies"));
        ListOfItem_side.setTooltip(createCustomTooltip("Categories"));
        PurchaseInvoice_side.setTooltip(createCustomTooltip("Purchase Invoice"));
        SalesInvoice_side.setTooltip(createCustomTooltip("Sales Invoice"));
        Customers_side.setTooltip(createCustomTooltip("Customers"));
        cashSupplay_side.setTooltip(createCustomTooltip("Cash Supply"));

        // Set hover effect for all buttons
        setHoverEffect(CategoriesMain);
        setHoverEffect(WarehousesMain);
        setHoverEffect(SuppliersMain);
        setHoverEffect(PurchasesMain);
        setHoverEffect(CustomersMain);
        setHoverEffect(SalesMain);
        setHoverEffect(GeneralAccountsMain);
        setHoverEffect(OrdersMain);
        setHoverEffect(EmployeesAffairsMain);
        setHoverEffect(FrameworkMain);
        setHoverEffect(GeneralInformation);
        setSideHoverEffect(Deficiencies_side);
        setSideHoverEffect(PurchaseInvoice_side);
        setSideHoverEffect(SalesInvoice_side);
        setSideHoverEffect(Customers_side);
        setSideHoverEffect(cashSupplay_side);
        setSideHoverEffect(ListOfItem_side);

        setLanguage("ar");

        ArabicLanguageMenu.setOnAction(event -> setLanguage("ar"));
        EnglishLanguageMenu.setOnAction(event -> setLanguage("en"));
    }

    //  Exit Function
    @FXML
    public void Logout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/project/pharmacyv1/Login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            LogWriter logWriter = new LogWriter();
            logWriter.Logout();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error
        }

        db.isLogedIn = false;
        //write in Log file that user "name" logged in at "time"
        try {
            FileWriter writer = new FileWriter("F:\\Pharmacy Backup\\Pharmacy-Management-System\\src\\main\\java\\Database\\Log", true);
            Date date = new Date();
            writer.write("User " + db.logedInUser + " logged out at " + date + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
        Stage stage = (Stage) Logout.getScene().getWindow();
        stage.close();
    }


}
