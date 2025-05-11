package Classes;


import java.time.LocalDate;

public class Medicine {
    private final int id;                   // immutable primary key
    private String barcode;
    private String arabicName;
    private String englishName;
    private String internationalCode;
    private String activeIngredient;
    private String manufacturer;
    private LocalDate expiryDate;
    private String unit;
    private double quantity;
    private double sellingPrice;
    private double purchasePrice;
    private double reorderLevel;
    private String medicationType;

    /* Medicine Builder Pattern */
    public static class MedicineBuilder {
        private int id; // optional (0 = not yet stored)
        private String barcode;
        private String arabicName;
        private String englishName;
        private String internationalCode;
        private String activeIngredient;
        private String manufacturer;
        private LocalDate expiryDate;
        private String unit = "pcs";
        private double quantity = 0;
        private double sellingPrice;
        private double purchasePrice;
        private double reorderLevel;
        private String medicationType;

        public MedicineBuilder id(int v){ this.id = v; return this; }
        public MedicineBuilder barcode(String v){ this.barcode = v; return this; }
        public MedicineBuilder arabicName(String v){ this.arabicName = v; return this; }
        public MedicineBuilder englishName(String v){ this.englishName = v; return this; }
        public MedicineBuilder internationalCode(String v){ this.internationalCode = v; return this; }
        public MedicineBuilder activeIngredient(String v){ this.activeIngredient = v; return this; }
        public MedicineBuilder manufacturer(String v){ this.manufacturer = v; return this; }
        public MedicineBuilder expiryDate(LocalDate v){ this.expiryDate = v; return this; }
        public MedicineBuilder unit(String v){ this.unit = v; return this; }
        public MedicineBuilder quantity(double v){ this.quantity = v; return this; }
        public MedicineBuilder sellingPrice(double v){ this.sellingPrice = v; return this; }
        public MedicineBuilder purchasePrice(double v){ this.purchasePrice = v; return this; }
        public MedicineBuilder reorderLevel(double v){ this.reorderLevel = v; return this; }
        public MedicineBuilder medicationType(String v){ this.medicationType = v; return this; }

        public Medicine build() { return new Medicine(this); }
    }

    private Medicine(MedicineBuilder b) {
        this.id = b.id;
        this.barcode = b.barcode;
        this.arabicName = b.arabicName;
        this.englishName = b.englishName;
        this.internationalCode = b.internationalCode;
        this.activeIngredient = b.activeIngredient;
        this.manufacturer = b.manufacturer;
        this.expiryDate = b.expiryDate;
        this.unit = b.unit;
        this.quantity = b.quantity;
        this.sellingPrice = b.sellingPrice;
        this.purchasePrice = b.purchasePrice;
        this.reorderLevel = b.reorderLevel;
        this.medicationType = b.medicationType;
    }

    public int getId() { return id; }
    /* getters & setters (omitted for brevity) */

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicine medicine = (Medicine) o;
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

    public String getInternationalCode() {
        return internationalCode;
    }

    public void setInternationalCode(String internationalCode) {
        this.internationalCode = internationalCode;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getMedicationType() {
        return medicationType;
    }

    public void setMedicationType(String medicationType) {
        this.medicationType = medicationType;
    }
}