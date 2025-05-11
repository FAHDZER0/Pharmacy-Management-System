package Classes;

import java.time.LocalDate;

public interface BasicProduct {

    int getId();
    void setBarcode(String barcode);
    String getBarcode();
    void setArabicName(String arabicName);
    String getArabicName();
    void setEnglishName(String englishName);
    String getEnglishName();
    void setManufacturer(String manufacturer);
    String getManufacturer();
    void setExpiryDate(LocalDate expiryDate);
    LocalDate getExpiryDate();
    void setQuantity(double quantity);
    double getQuantity();
    void setSellingPrice(double sellingPrice);
    double getSellingPrice();
    void setPurchasePrice(double purchasePrice);
    double getPurchasePrice();
    void setReorderLevel(double reorderLevel);
    double getReorderLevel();
}