    package project.pharmacyv1.Sales;

    import java.io.IOException;
    import java.util.Arrays;
    import java.util.List;
    import java.util.Map;

    import Config.LanguageSetter;
    import Database.DB;
    import javafx.beans.property.SimpleObjectProperty;
    import javafx.beans.value.ChangeListener;
    import javafx.collections.ObservableList;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Scene;
    import javafx.scene.control.Label;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;
    import javafx.scene.control.TextField;
    import javafx.scene.input.MouseButton;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.layout.VBox;
    import javafx.stage.Modality;
    import javafx.stage.Stage;
    import project.pharmacyv1.DashboardController;
    import project.pharmacyv1.LogWriter;

    public class SalesInvoiceController {

    @FXML
    public TableView Sales1BigTable;

    @FXML
    private TextField NoOfItem ;

    @FXML
    private TextField TotalBeforeDisc ;

    @FXML
    private TextField CustomerPhone;

    @FXML
    private TextField CustomerCode;

    @FXML
    private Label CustomerName;

    @FXML
    private TextField CustomerBalance ;

    @FXML
    private TextField DiscountPrecent ;

    @FXML
    private TextField DiscountAmount ;

    @FXML
    private TextField Notes ;

    @FXML
    private Label SalesInvoice;

    @FXML
    private Label InvoiceCostLabel;

    @FXML
    private Label invoiceProfitValue;

    @FXML
    private Label InvoiceTotalValue;

    DB db = new DB();
    LogWriter log = new LogWriter();

    public void doublClick(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/pharmacyv1/Sales/FindItem_PopUp.fxml"));
                    VBox secondaryContent = loader.load();

                    // Get the controller and set the SalesInvoiceController instance
                    FindItem_PopUpController controller = loader.getController();
                    controller.setSalesInvoiceController(this);

                    Scene FindScene = new Scene(secondaryContent,830,666);

                    Stage FindStage = new Stage();
                    FindStage.setResizable(false);
                    FindStage.setTitle("Search An Item");
                    FindStage.setScene(FindScene);
                    FindStage.initModality(Modality.APPLICATION_MODAL);
                    FindStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    double totalcost = 0;
    double currentCost = 0;
    double totalprofit = 0;
    double currentProfit = 0;


    public void setSales1BigTable(Map<String, Object> data) {
        // Calculate currentProfit
        if (data.get("PurchasePrice") != null && data.get("Amount") != null) {
            double purchasePrice = Double.parseDouble(data.get("PurchasePrice").toString());
            double amount = Double.parseDouble(data.get("Amount").toString());
            currentCost = Double.parseDouble(data.get("SellingPrice").toString());
            currentProfit = (currentCost - purchasePrice) * amount;
        }

        // Remove unnecessary data
        data.keySet().removeAll(Arrays.asList("PurchasePrice", "InternationalCode", "Manufacturer",
                "ActiveIngredient", "ExpiryDate", "ReorderLevel", "Quantity"));

        if (Sales1BigTable.getColumns().isEmpty()) {
            // Add "ID" and "Barcode" columns first
            List<String> firstColumns = Arrays.asList("ItemID", "ItemBarcode");
            for (String column : firstColumns) {
                TableColumn<Map<String, Object>, Object> col = new TableColumn<>(column);
                col.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().get(column)));
                Sales1BigTable.getColumns().add(col);
            }

            // Then add the rest of the columns
            for (String column : data.keySet()) {
                if (!firstColumns.contains(column)) {
                    TableColumn<Map<String, Object>, Object> col = new TableColumn<>(column);
                    col.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().get(column)));
                    Sales1BigTable.getColumns().add(col);
                }
            }
        }

        Sales1BigTable.setStyle("-fx-font-size: 16");
        Sales1BigTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        totalcost += currentCost * Double.parseDouble(data.get("Amount").toString());
        totalprofit += currentProfit; // Update totalprofit calculation

        InvoiceCostLabel.setText(String.valueOf(totalcost));
        invoiceProfitValue.setText(String.valueOf(totalprofit));
        InvoiceTotalValue.setText(String.valueOf(totalcost));

        Sales1BigTable.getItems().add(data);
        setDiscountTable();
    }

    public void setDiscountTable(){

        System.out.println("Setting Discount Table");
        NoOfItem.setText( String.valueOf(Sales1BigTable.getItems().size()));
        TotalBeforeDisc.setText(InvoiceTotalValue.getText());
        if (DiscountPrecent.getText().isEmpty()){
            DiscountPrecent.setText("0");
        }
        if (DiscountAmount.getText().isEmpty()){
            DiscountAmount.setText("0");
        }
        if (Notes.getText().isEmpty()){
            Notes.setText("No Notes");
        }

    }

    public void setDiscountByPercentage() {
        double discountPercent = Double.parseDouble(DiscountPrecent.getText());
        double totalBeforeDisc = Double.parseDouble(TotalBeforeDisc.getText());

        double discountAmount = totalBeforeDisc * discountPercent / 100;
        DiscountAmount.setText(String.valueOf(discountAmount));

        double totalAfterDisc = totalBeforeDisc - discountAmount;
        InvoiceTotalValue.setText(String.valueOf(totalAfterDisc));
    }

    public void setDiscountByAmount() {
        double discountAmount = Double.parseDouble(DiscountAmount.getText());
        double totalBeforeDisc = Double.parseDouble(TotalBeforeDisc.getText());

        double discountPercent = (discountAmount / totalBeforeDisc) * 100;
        DiscountPrecent.setText(String.valueOf(discountPercent));

        double totalAfterDisc = totalBeforeDisc - discountAmount;
        InvoiceTotalValue.setText(String.valueOf(totalAfterDisc));
    }

    public void settingCustomerDataUsingPhone(){
        db = new DB();
        String customerphone = CustomerPhone.getText();
        ObservableList<Map<String, Object>> data = db.SelectQuery("customers", "PersonalPhoneNumber", customerphone);
        if (data.size() <= 0 || customerphone.isEmpty()) {
            System.out.println("Customer Not Found");
            CustomerName.setText("Customer Not Found");
            CustomerBalance.setText("0");
            CustomerCode.setText("Customer Not Found");
            CustomerBalance.styleProperty().setValue("-fx-text-fill: red; -fx-font-weight: bold;");
            CustomerName.styleProperty().setValue("-fx-text-fill: red; -fx-font-weight: bold;");
            CustomerCode.styleProperty().setValue("-fx-text-fill: red; -fx-font-weight: bold;");
        }else{
            Map<String, Object> customer = data.get(0);
            CustomerName.setText(customer.get("CustomerName").toString());
            CustomerBalance.setText(customer.get("CurrentBalance").toString());
            CustomerCode.setText(customer.get("CustomerID").toString());
            CustomerName.styleProperty().setValue("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 16; -fx-border-color: black; -fx-border-width: 1px; -fx-background-color: #f4f4f4;");
            CustomerBalance.styleProperty().setValue("-fx-text-fill: black; -fx-font-weight: bold;");
            CustomerCode.styleProperty().setValue("-fx-text-fill: black; -fx-font-weight: bold;");
            CustomerPhone.styleProperty().setValue("-fx-text-fill: black; -fx-font-weight: bold;");
        }
    }

    public void settingCustomerDataUsingCode(){
        db = new DB();
        String customercode = CustomerCode.getText();
        ObservableList<Map<String, Object>> data = db.SelectQuery("customers", "CustomerID", customercode);
        if (data.size() <= 0 || customercode.isEmpty()) {
            System.out.println("Customer Not Found");
            CustomerName.setText("Customer Not Found");
            CustomerBalance.setText("0");
            CustomerPhone.setText("Customer Not Found");
            CustomerBalance.styleProperty().setValue("-fx-text-fill: red; -fx-font-weight: bold;");
            CustomerName.styleProperty().setValue("-fx-text-fill: red; -fx-font-weight: bold;");
            CustomerPhone.styleProperty().setValue("-fx-text-fill: red; -fx-font-weight: bold;");
        }else{
            Map<String, Object> customer = data.get(0);
            CustomerName.setText(customer.get("CustomerName").toString());
            CustomerBalance.setText(customer.get("CurrentBalance").toString());
            CustomerPhone.setText(customer.get("PersonalPhoneNumber").toString());
            CustomerName.styleProperty().setValue("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 16; -fx-border-color: black; -fx-border-width: 1px; -fx-background-color: #f4f4f4;");
            CustomerBalance.styleProperty().setValue("-fx-text-fill: black; -fx-font-weight: bold;");
            CustomerPhone.styleProperty().setValue("-fx-text-fill: black; -fx-font-weight: bold;");
            CustomerCode.styleProperty().setValue("-fx-text-fill: black; -fx-font-weight: bold;");

        }
    }

    @FXML
    public void saveSalesInvoice(){
        System.out.println("Saving Sales Invoice");
        //getting the data from the row and saving it to the database
        //salesinvoices has these columns SalesInvoiceID , CustomerID , TotalSaleAmount , SaleStatus , SalesDate

        log.SoldItem(db.logedInUser, "SalesInvoiceID");

    }

    public void initialize() {
        DashboardController DC = new DashboardController();
        LanguageSetter LS = new LanguageSetter();

        Sales1BigTable.setOnMouseClicked(this::doublClick);

        DiscountPrecent.textProperty().addListener((observable, oldValue, newValue) -> {
            setDiscountByPercentage();
        });

        DiscountAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            setDiscountByAmount();
        });

        // Listener for changes in CustomerPhone text and focus
        CustomerPhone.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // Check if the field gains focus
                CustomerPhone.textProperty().addListener(phoneTextListener);
            } else { // Field loses focus
                CustomerPhone.textProperty().removeListener(phoneTextListener);
            }
        });

        // Listener for changes in CustomerCode text and focus
        CustomerCode.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // Check if the field gains focus
                CustomerCode.textProperty().addListener(codeTextListener);
            } else { // Field loses focus
                CustomerCode.textProperty().removeListener(codeTextListener);
            }
        });

        if(DC.Language.equals("en")){
            SalesInvoice.setText(LS.il8n("SalesTitle","en"));
        } else if(DC.Language.equals("ar")){
            SalesInvoice.setText(LS.il8n("SalesTitle","ar"));
        }
    }

    // Listener for changes in the text property of CustomerPhone
    ChangeListener<String> phoneTextListener = (observable, oldValue, newValue) -> {
        settingCustomerDataUsingPhone();
    };

    // Listener for changes in the text property of CustomerCode
    ChangeListener<String> codeTextListener = (observable, oldValue, newValue) -> {
        settingCustomerDataUsingCode();
    };

}