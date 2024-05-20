    package project.pharmacyv1.Sales;

    import java.io.IOException;
    import java.util.*;

    import Config.LanguageSetter;
    import Config.PDFprinterController;
    import Database.DB;
    import javafx.beans.property.SimpleObjectProperty;
    import javafx.beans.value.ChangeListener;
    import javafx.collections.ObservableList;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.input.MouseButton;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.layout.VBox;
    import javafx.scene.text.Font;
    import javafx.stage.Modality;
    import javafx.stage.Stage;
    import javafx.util.Duration;
    import project.pharmacyv1.DashboardController;
    import project.pharmacyv1.LogWriter;
    import project.pharmacyv1.Sales.CommentedInvoicesController;

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
    @FXML
    public Button newInvoiceButton;
    @FXML
    public Button SaveButton;
    @FXML
    public Button invoiceCommentButton;
    @FXML
    public Button AddItemButton;
    @FXML
    public Button NewItemButton ;
    @FXML
    public Button DeleteRowButton;
    @FXML
    public ToggleGroup patmentMethod;
    @FXML
    public RadioButton cash;
    @FXML
    public RadioButton Visa;
    @FXML
    public RadioButton Credit;
    @FXML
    public RadioButton Onhold;
    @FXML
    public RadioButton HomeDelivery;

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

    private Button currentButton; //Reference to the currently pressed button


    private void setSideHoverEffect(Control control){
        control.setOnMouseEntered(event -> {
            control.setStyle("-fx-background-color:  #a1d6e2; -fx-background-radius: 100px; -fx-border-color: #f1f1f2; -fx-border-radius: 100px; -fx-border-width: 3px;");
        });

        control.setOnMouseExited(event -> {
            // If the button is not currently pressed, keep it blue and big
            if (control != currentButton) {
                control.setStyle("-fx-background-color:  #1996aa; -fx-background-radius: 100px; -fx-border-color: #f1f1f2; -fx-border-radius: 100px; -fx-border-width: 3px;");
            }
        });

    }

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
            CustomerBalance.setText(customer.get("CustomerDebt").toString());
            CustomerPhone.setText(customer.get("PersonalPhoneNumber").toString());
            CustomerName.styleProperty().setValue("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 16; -fx-border-color: black; -fx-border-width: 1px; -fx-background-color: #f4f4f4;");
            CustomerBalance.styleProperty().setValue("-fx-text-fill: black; -fx-font-weight: bold;");
            CustomerPhone.styleProperty().setValue("-fx-text-fill: black; -fx-font-weight: bold;");
            CustomerCode.styleProperty().setValue("-fx-text-fill: black; -fx-font-weight: bold;");

        }
    }

    private Tooltip createCustomTooltip(String text) {
            Tooltip tooltip = new Tooltip(text);
            tooltip.setFont(new Font("Arial", 15));
            tooltip.setShowDelay(Duration.millis(100));
            return tooltip;
        }

    @FXML
    public void saveSalesInvoice(){
        // Save the invoice to the database
        // the columns are SalesInvoiceID , CustomerID , TotalSaleAmount , SaleStatus , SalesDate , DiscountAmount , TotalProfit and Notes in the salesinvoices table

        Date date = new Date();

        Map<String, Object> data = new HashMap<>();
        data.put("CustomerID", CustomerCode.getText());
        data.put("TotalSaleAmount", InvoiceTotalValue.getText());
        if (((RadioButton) patmentMethod.getSelectedToggle()).getText().equalsIgnoreCase("On Hold")) {
            data.put("SaleStatus", "On hold");
        } else {
            data.put("SaleStatus", "Complete");
        }
        data.put("SalesDate", date );
        data.put("DiscountAmount", DiscountAmount.getText());
        data.put("TotalProfit", invoiceProfitValue.getText());
        data.put("Notes", Notes.getText());
        data.put("PaymentMethod", ((RadioButton) patmentMethod.getSelectedToggle()).getText());
        data.put("CashairName", db.logedInUser);

        int salesInvoiceID = db.InsertQuery("salesinvoices", data);

        System.out.println(Sales1BigTable.getItems().size());
        // Insert the sales invoice details
        for (int i = 0; i < Sales1BigTable.getItems().size(); i++) {
            Map<String, Object> item = (Map<String, Object>) Sales1BigTable.getItems().get(i);
            data = new HashMap<>();
            data.put("SalesInvoiceID", salesInvoiceID);
            data.put("ProductType", item.get("ItemType"));
            data.put("Quantity", item.get("Amount"));
            data.put("ItemID", item.get("ItemID"));
            data.put("SellingPrice", item.get("SellingPrice"));
            data.put("Unit", item.get("Unit"));
            data.put("ItemName",item.get("EnglishName"));
            db.InsertQuery("salesinvoicedetails", data);
        }

        // Log the selling of the items
        log.SoldItem(db.logedInUser, String.valueOf(salesInvoiceID));

        //showing a message to ask the employee if he wants to print the invoice
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Print Invoice");
        alert.setHeaderText("Do you want to print the invoice?");
        alert.setContentText("Choose your option.");
        alert.showAndWait();

        // If the user presses OK, print the invoice
        if (alert.getResult() == ButtonType.OK) {
            printInvoice(salesInvoiceID);
            System.out.println("Printing the invoice");
        } else if (alert.getResult() == ButtonType.CANCEL){
            // If the user presses Cancel, do nothing
            System.out.println("Cancel");
        }

        NewInvoice();
    }

    @FXML
    public void printInvoice(int InvoiceNumber){
        // Print the invoice

        ObservableList<Map<String, Object>> invoice = db.SelectQuery("salesinvoices", "SalesInvoiceID", String.valueOf(InvoiceNumber));

        String orderType = "";

        switch (((RadioButton) patmentMethod.getSelectedToggle()).getText()) {
            case "Cash":
                orderType = "In place";
                break;
            case "Visa":
                orderType = "In place";
                break;
            case "Credit":
                orderType = "In place";
                break;
            case "Onhold":
                orderType = "On hold";
                break;
            case "Home Delivery":
                orderType = "Home delivery";
                break;
        }

        String ChasierName = invoice.get(0).get("CashairName").toString();
        String CustomerNumber = invoice.get(0).get("CustomerID").toString();
        String CustomerName = db.SelectQuery("customers", "CustomerID", CustomerNumber).get(0).get("CustomerName").toString();
        String CustomerAddress = db.SelectQuery("customers", "CustomerID", CustomerNumber).get(0).get("CustomerAddress").toString();
        String InvoiceNotes = invoice.get(0).get("notes").toString();
        String InvoiceTotal = invoice.get(0).get("TotalSaleAmount").toString();
        String discountPercentage = invoice.get(0).get("DiscountAmount").toString();
        String finaltotalPrice = invoice.get(0).get("TotalSaleAmount").toString();

        PDFprinterController pdf = new PDFprinterController();

        // Create a list to store the items in the invoice
        List<PDFprinterController.InvoiceItem> items = new ArrayList<>();

        // Fetch the sales invoice details from the database
        ObservableList<Map<String, Object>> salesInvoiceDetails = db.SelectQuery("salesinvoicedetails", "SalesInvoiceID", String.valueOf(InvoiceNumber));

        // Convert each Map<String, Object> in salesInvoiceDetails to a PDFprinterController.InvoiceItem and add it to the list
        for (Map<String, Object> itemMap : salesInvoiceDetails) {
            String itemName = itemMap.get("ItemName").toString();
            String itemPrice = itemMap.get("SellingPrice").toString();
            String itemQuantity = String.valueOf(itemMap.get("Quantity"));
            String itemTotal = String.valueOf(Double.parseDouble(itemPrice) * Double.parseDouble(itemQuantity));

            PDFprinterController.InvoiceItem invoiceItem = new PDFprinterController.InvoiceItem(itemName, itemPrice, itemQuantity, itemTotal);
            items.add(invoiceItem);
        }

        pdf.PrintSalesIntoPDF(orderType ,ChasierName, String.valueOf(InvoiceNumber), CustomerNumber , CustomerName , CustomerAddress , InvoiceNotes , items ,InvoiceTotal , discountPercentage , "20" , finaltotalPrice  );
    }

    //this method is for displaying the commented Invoices in a separate stage
    @FXML
    public void CommentedInvoices() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/pharmacyv1/Sales/CommentedInvoices.fxml"));
            VBox root1 = (VBox) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Commented Invoices");
            stage.setScene(new Scene(root1));
            stage.show();
            CommentedInvoicesController controller = fxmlLoader.getController();
            controller.setSalesInvoiceController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCommentedInvoice(int InvoiceNumber){
        // Fetch the commented invoice from the database
        ObservableList<Map<String, Object>> invoice = db.SelectQuery("salesinvoices", "SalesInvoiceID", String.valueOf(InvoiceNumber));

        // Set the data in the invoice to the fields in the SalesInvoiceController
        CustomerCode.setText(invoice.get(0).get("CustomerID") != null ? invoice.get(0).get("CustomerID").toString() : "");
        CustomerPhone.setText(db.SelectQuery("customers", "CustomerID", CustomerCode.getText()).get(0).get("PersonalPhoneNumber") != null ? db.SelectQuery("customers", "CustomerID", CustomerCode.getText()).get(0).get("PersonalPhoneNumber").toString() : "");
        CustomerName.setText(db.SelectQuery("customers", "CustomerID", CustomerCode.getText()).get(0).get("CustomerName") != null ? db.SelectQuery("customers", "CustomerID", CustomerCode.getText()).get(0).get("CustomerName").toString() : "");
        CustomerBalance.setText(db.SelectQuery("customers", "CustomerID", CustomerCode.getText()).get(0).get("CustomerDebt") != null ? db.SelectQuery("customers", "CustomerID", CustomerCode.getText()).get(0).get("CustomerDebt").toString() : "");
        DiscountAmount.setText(invoice.get(0).get("DiscountAmount") != null ? invoice.get(0).get("DiscountAmount").toString() : "");
        DiscountPrecent.setText(invoice.get(0).get("DiscountAmount") != null ? invoice.get(0).get("DiscountAmount").toString() : "");
        Notes.setText(invoice.get(0).get("Notes") != null ? invoice.get(0).get("Notes").toString() : "");
        InvoiceCostLabel.setText(invoice.get(0).get("TotalSaleAmount") != null ? invoice.get(0).get("TotalSaleAmount").toString() : "");
        invoiceProfitValue.setText(invoice.get(0).get("TotalProfit") != null ? invoice.get(0).get("TotalProfit").toString() : "");
        InvoiceTotalValue.setText(invoice.get(0).get("TotalSaleAmount") != null ? invoice.get(0).get("TotalSaleAmount").toString() : "");

        // Fetch the sales invoice details from the database
        ObservableList<Map<String, Object>> salesInvoiceDetails = db.SelectQuery("salesinvoicedetails", "SalesInvoiceID", String.valueOf(InvoiceNumber));

        // Add the sales invoice details to the Sales1BigTable
        for (Map<String, Object> item : salesInvoiceDetails) {
            setSales1BigTable(item);
        }
    }

    @FXML
    public void NewInvoice(){
        Sales1BigTable.getItems().clear();
        NoOfItem.setText("0");
        TotalBeforeDisc.setText("0");
        CustomerPhone.setText("");
        CustomerCode.setText("0");
        DiscountPrecent.setText(String.valueOf(0));
        DiscountAmount.setText(String.valueOf(0));
        Notes.setText("");
        InvoiceCostLabel.setText("0");
        invoiceProfitValue.setText("0");
        InvoiceTotalValue.setText("0");
        patmentMethod.selectToggle(cash);
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
            newInvoiceButton.setTooltip(createCustomTooltip("New Invoice"));
            AddItemButton.setTooltip(createCustomTooltip("Add Item"));
            NewItemButton.setTooltip(createCustomTooltip("New Item"));
            DeleteRowButton.setTooltip(createCustomTooltip("Delete Row"));
            SaveButton.setTooltip(createCustomTooltip("Save Invoice"));
            invoiceCommentButton.setTooltip(createCustomTooltip("Commented Invoices"));

        } else if(DC.Language.equals("ar")){
            SalesInvoice.setText(LS.il8n("SalesTitle","ar"));
            newInvoiceButton.setTooltip(createCustomTooltip("فاتورة جديدة"));
            AddItemButton.setTooltip(createCustomTooltip("إضافة عنصر"));
            DeleteRowButton.setTooltip(createCustomTooltip("حذف صف"));
            SaveButton.setTooltip(createCustomTooltip("حفظ فاتورة"));
            invoiceCommentButton.setTooltip(createCustomTooltip("فواتير معلقة"));

        }

        setSideHoverEffect(newInvoiceButton);
        setSideHoverEffect(SaveButton);
        setSideHoverEffect(invoiceCommentButton);
        setSideHoverEffect(AddItemButton);
        setSideHoverEffect(NewItemButton);
        setSideHoverEffect(DeleteRowButton);


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