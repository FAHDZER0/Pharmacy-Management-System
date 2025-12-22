package Classes;

import Database.DBConfig;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

interface SalesInvoiceInterface {
    String getOrderType();
    String getCashairName();
    String getCustomerNumber();
    String getCustomerName();
    String getCustomerAddress();
    String getInvoiceNotes();
    String getInvoiceTotal();
    String getDiscountPercentage();
    String getFinalTotalPrice();
}

// SalesInvoiceBuilder Class with Builder Pattern
public class SalesInvoiceBuilder implements SalesInvoiceInterface {
    private final String orderType;
    private final String CashairName;
    private final String customerNumber;
    private final String customerName;
    private final String customerAddress;
    private final String invoiceNotes;
    private final String invoiceTotal;
    private final String discountPercentage;
    private final String finalTotalPrice;

    private SalesInvoiceBuilder(Builder b) {
        this.orderType         = b.orderType;
        this.CashairName       = b.CashairName;
        this.customerNumber    = b.customerNumber;
        this.customerName      = b.customerName;
        this.customerAddress   = b.customerAddress;
        this.invoiceNotes      = b.invoiceNotes;
        this.invoiceTotal      = b.invoiceTotal;
        this.discountPercentage= b.discountPercentage;
        this.finalTotalPrice   = b.finalTotalPrice;
    }

    // Getters...
    public String getOrderType()         { return orderType; }
    public String getCashairName()       { return CashairName; }
    public String getCustomerNumber()    { return customerNumber; }
    public String getCustomerName()      { return customerName; }
    public String getCustomerAddress()   { return customerAddress; }
    public String getInvoiceNotes()      { return invoiceNotes; }
    public String getInvoiceTotal()      { return invoiceTotal; }
    public String getDiscountPercentage(){ return discountPercentage; }
    public String getFinalTotalPrice()   { return finalTotalPrice; }

    public static class Builder {
        private String orderType;
        private String CashairName;
        private String customerNumber;
        private String customerName;
        private String customerAddress;
        private String invoiceNotes;
        private String invoiceTotal;
        private String discountPercentage;
        private String finalTotalPrice;

        public Builder orderType(String v)         { orderType = v; return this; }
        public Builder CashairName(String v)       { CashairName = v; return this; }
        public Builder customerNumber(String v)    { customerNumber = v; return this; }
        public Builder customerName(String v)      { customerName = v; return this; }
        public Builder customerAddress(String v)   { customerAddress = v; return this; }
        public Builder invoiceNotes(String v)      { invoiceNotes = v; return this; }
        public Builder invoiceTotal(String v)      { invoiceTotal = v; return this; }
        public Builder discountPercentage(String v){ discountPercentage = v; return this; }
        public Builder finalTotalPrice(String v)   { finalTotalPrice = v; return this; }

        public SalesInvoiceBuilder build() {
            return new SalesInvoiceBuilder(this);
        }

        /**
         * Fetches both invoice and customer in one JOINed query, and builds the object.
         */
        public static SalesInvoiceBuilder fromDatabase(int invoiceNumber, ToggleGroup paymentMethod) {
            String sql = """
                SELECT
                  si.CashairName,
                  si.CustomerID,
                  si.notes,
                  si.TotalSaleAmount,
                  si.DiscountAmount,
                  c.CustomerName,
                  c.CustomerAddress
                FROM salesinvoices si
                JOIN customers c ON si.CustomerID = c.CustomerID
                WHERE si.SalesInvoiceID = ?
                """;

            try (Connection conn = DBConfig.getInstance().getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, invoiceNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalStateException("No invoice found with ID " + invoiceNumber);
                    }

                    // Map DB columns directly into locals:
                    String CashairName       = rs.getString("CashairName");
                    String customerNumber    = rs.getString("CustomerID");
                    String invoiceNotes      = rs.getString("notes");
                    String invoiceTotal      = rs.getString("TotalSaleAmount");
                    String discountPercent   = rs.getString("DiscountAmount");
                    String customerName      = rs.getString("CustomerName");
                    String customerAddress   = rs.getString("CustomerAddress");
                    String finalTotalPrice   = invoiceTotal; // adjust if you want net

                    // Determine orderType
                    Toggle sel = paymentMethod.getSelectedToggle();
                    String selText = ((RadioButton)sel).getText();
                    String orderType = switch (selText) {
                        case "Cash", "Visa", "Credit" -> "In place";
                        case "Onhold"                -> "On hold";
                        case "Home Delivery"         -> "Home delivery";
                        default -> throw new IllegalArgumentException("Unknown payment type: " + selText);
                    };

                    // Build and return
                    return new Builder()
                            .orderType(orderType)
                            .CashairName(CashairName)
                            .customerNumber(customerNumber)
                            .customerName(customerName)
                            .customerAddress(customerAddress)
                            .invoiceNotes(invoiceNotes)
                            .invoiceTotal(invoiceTotal)
                            .discountPercentage(discountPercent)
                            .finalTotalPrice(finalTotalPrice)
                            .build();
                }

            } catch (SQLException e) {
                throw new RuntimeException("DB error fetching invoice/customer", e);
            }
        }
    }
}
