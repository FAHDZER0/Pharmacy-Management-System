package DAOs;

import Classes.Medicine;
import Database.DBConfig;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicineDAO {
    private static final String TABLE_NAME = "medications";

    public Medicine findById(int id) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE MedicationID = ?";
        try (Connection conn = DBConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToMedicine(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Medicine> findAll() {
        String query = "SELECT * FROM " + TABLE_NAME;
        List<Medicine> medicines = new ArrayList<>();
        try (Connection conn = DBConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                medicines.add(mapResultSetToMedicine(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicines;
    }

    public void save(Medicine medicine) {
        String query = "INSERT INTO " + TABLE_NAME + " (" +
                "MedicationBarcode, ArabicName, EnglishName, InternationalCode, " +
                "ActiveIngredient, Manufacturer, ExpiryDate, Unit, Quantity, " +
                "SellingPrice, PurchasePrice, ReorderLevel, MedicationType, Limited) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, medicine.getBarcode());
            stmt.setString(2, medicine.getArabicName());
            stmt.setString(3, medicine.getEnglishName());
            stmt.setString(4, medicine.getInternationalCode());
            stmt.setString(5, medicine.getActiveIngredient());
            stmt.setString(6, medicine.getManufacturer());
            stmt.setDate(7, java.sql.Date.valueOf(medicine.getExpiryDate()));
            stmt.setString(8, medicine.getUnit());
            stmt.setDouble(9, medicine.getQuantity());
            stmt.setDouble(10, medicine.getSellingPrice());
            stmt.setDouble(11, medicine.getPurchasePrice());
            stmt.setDouble(12, medicine.getReorderLevel());
            stmt.setString(13, medicine.getMedicationType());
            stmt.setInt(14, medicine.getLimited()); // حفظ القيمة كما هي بدون تعديل
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating medicine failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    System.out.println("Inserted Medicine ID: " + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Medicine medicine) {
        String query = "UPDATE " + TABLE_NAME + " SET " +
                "MedicationBarcode = ?, ArabicName = ?, EnglishName = ?, InternationalCode = ?, " +
                "ActiveIngredient = ?, Manufacturer = ?, ExpiryDate = ?, Unit = ?, Quantity = ?, " +
                "SellingPrice = ?, PurchasePrice = ?, ReorderLevel = ?, MedicationType = ?, Limited = ? " +
                "WHERE MedicationID = ?";
        try (Connection conn = DBConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, medicine.getBarcode());
            stmt.setString(2, medicine.getArabicName());
            stmt.setString(3, medicine.getEnglishName());
            stmt.setString(4, medicine.getInternationalCode());
            stmt.setString(5, medicine.getActiveIngredient());
            stmt.setString(6, medicine.getManufacturer());
            stmt.setDate(7, java.sql.Date.valueOf(medicine.getExpiryDate()));
            stmt.setString(8, medicine.getUnit());
            stmt.setDouble(9, medicine.getQuantity());
            stmt.setDouble(10, medicine.getSellingPrice());
            stmt.setDouble(11, medicine.getPurchasePrice());
            stmt.setDouble(12, medicine.getReorderLevel());
            stmt.setString(13, medicine.getMedicationType());
            stmt.setInt(14, medicine.getLimited()); // حفظ القيمة كما هي بدون تعديل
            stmt.setInt(15, medicine.getId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Update failed: No medicine found with ID: " + medicine.getId());
                tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
                AnimationType type = AnimationType.POPUP;
                tray.setAnimationType(type);
                tray.setTitle("Failed");
                tray.setMessage("Medication Update Failed. No Medication Found With ID: " + medicine.getId() + ". Please Try Again Later.");
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(javafx.util.Duration.seconds(2));
            } else {
                System.out.println("Medicine with ID " + medicine.getId() + " updated successfully.");
                tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
                AnimationType type = AnimationType.POPUP;
                tray.setAnimationType(type);
                tray.setTitle("Success");
                tray.setMessage("Medication Updated Successfully");
                tray.setNotificationType(NotificationType.SUCCESS);
                tray.showAndDismiss(javafx.util.Duration.seconds(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE MedicationID = ?";
        try (Connection conn = DBConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("No medicine found with ID: " + id);
                tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
                AnimationType type = AnimationType.POPUP;
                tray.setAnimationType(type);
                tray.setTitle("Failed");
                tray.setMessage("Error Deleting Medication. No Medication Found With ID: " + id + ". Please Try Again Later.");
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(javafx.util.Duration.seconds(2));
            } else {
                System.out.println("Medicine with ID " + id + " has been deleted.");
                tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
                AnimationType type = AnimationType.POPUP;
                tray.setAnimationType(type);
                tray.setTitle("Success");
                tray.setMessage("Medication Deleted Successfully");
                tray.setNotificationType(NotificationType.SUCCESS);
                tray.showAndDismiss(javafx.util.Duration.seconds(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Medicine> findByManufacturer(String manufacturer) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE Manufacturer = ?";
        List<Medicine> medicines = new ArrayList<>();
        try (Connection conn = DBConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, manufacturer);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                medicines.add(mapResultSetToMedicine(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicines;
    }

    private Medicine mapResultSetToMedicine(ResultSet rs) throws SQLException {
        return new Medicine.MedicineBuilder()
                .id(rs.getInt("MedicationID"))
                .barcode(rs.getString("MedicationBarcode"))
                .arabicName(rs.getString("ArabicName"))
                .englishName(rs.getString("EnglishName"))
                .internationalCode(rs.getString("InternationalCode"))
                .activeIngredient(rs.getString("ActiveIngredient"))
                .manufacturer(rs.getString("Manufacturer"))
                .expiryDate(rs.getDate("ExpiryDate").toLocalDate())
                .unit(rs.getString("Unit"))
                .quantity(rs.getDouble("Quantity"))
                .sellingPrice(rs.getDouble("SellingPrice"))
                .purchasePrice(rs.getDouble("PurchasePrice"))
                .reorderLevel(rs.getDouble("ReorderLevel"))
                .medicationType(rs.getString("MedicationType"))
                .Limited(rs.getInt("Limited")) // قراءة القيمة كما هي بدون تعديل
                .build();
    }
}