package Classes;


import java.time.LocalDate;

public class Product implements BasicProduct{
    private final int id;
    private String barcode;
    private String arabicName;
    private String englishName;
    private String manufacturer;
    private LocalDate expiryDate;
    private double quantity;
    private double sellingPrice;
    private double purchasePrice;
    private double reorderLevel;
    private String productType;

    /* Medicine Builder Pattern */
    public static class ProductBuilder {
        private int id;
        private String barcode;
        private String arabicName;
        private String englishName;
        private String manufacturer;
        private LocalDate expiryDate;
        private double quantity;
        private double sellingPrice;
        private double purchasePrice;
        private double reorderLevel;
        private String productType;

        public ProductBuilder id(int v){ this.id = v; return this; }
        public ProductBuilder barcode(String v){ this.barcode = v; return this; }
        public ProductBuilder arabicName(String v){ this.arabicName = v; return this; }
        public ProductBuilder englishName(String v){ this.englishName = v; return this; }
        public ProductBuilder manufacturer(String v){ this.manufacturer = v; return this; }
        public ProductBuilder expiryDate(LocalDate v){ this.expiryDate = v; return this; }
        public ProductBuilder quantity(double v){ this.quantity = v; return this; }
        public ProductBuilder sellingPrice(double v){ this.sellingPrice = v; return this; }
        public ProductBuilder purchasePrice(double v){ this.purchasePrice = v; return this; }
        public ProductBuilder reorderLevel(double v){ this.reorderLevel = v; return this; }
        public ProductBuilder productType(String v){ this.productType = v; return this; }

        public Product build() { return new Product(this); }
    }

    private Product(ProductBuilder b) {
        this.id = b.id;
        this.barcode = b.barcode;
        this.arabicName = b.arabicName;
        this.englishName = b.englishName;
        this.manufacturer = b.manufacturer;
        this.expiryDate = b.expiryDate;
        this.quantity = b.quantity;
        this.sellingPrice = b.sellingPrice;
        this.purchasePrice = b.purchasePrice;
        this.reorderLevel = b.reorderLevel;
        this.productType = b.productType;
    }

    public int getId() { return id; }
    /* getters & setters (omitted for brevity) */

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product medicine = (Product) o;
        return id == medicine.id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getArabicName() {
        return arabicName;
    }

    public void setArabicName(String arabicName) {
        this.arabicName = arabicName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(double reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}