package DAOs;

import Classes.Product;
import Database.DBConfig;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private static final String TABLE_NAME = "products";

    public Product findById(int id) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE ProductID = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> findAll() {
        String query = "SELECT * FROM " + TABLE_NAME;
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public void save(Product product) {
        String query = "INSERT INTO " + TABLE_NAME + " (" +
                "ProductBarcode, ArabicName, EnglishName," +
                "Manufacturer, ExpiryDate, Quantity, " +
                "SellingPrice, PurchasePrice, ReorderLevel, ProductType) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getBarcode());
            stmt.setString(2, product.getArabicName());
            stmt.setString(3, product.getEnglishName());
            stmt.setString(4, product.getManufacturer());
            stmt.setDate(5, java.sql.Date.valueOf(product.getExpiryDate()));
            stmt.setDouble(6, product.getQuantity());
            stmt.setDouble(7, product.getSellingPrice());
            stmt.setDouble(8, product.getPurchasePrice());
            stmt.setDouble(9, product.getReorderLevel());
            stmt.setString(10, product.getProductType());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating product failed, no rows affected.");
            }

            // Retrieve auto-generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    System.out.println("Inserted Product ID: " + id);
                    tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
                    AnimationType type = AnimationType.POPUP;
                    tray.setAnimationType(type);
                    tray.setTitle("Success");
                    tray.setMessage("Product Added Successfully");
                    tray.setNotificationType(NotificationType.SUCCESS);
                    tray.showAndDismiss(javafx.util.Duration.seconds(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Product product) {
        String query = "UPDATE " + TABLE_NAME + " SET " +
                "ProductBarcode = ?, ArabicName = ?, EnglishName = ?, Manufacturer = ?, " +
                "ExpiryDate = ?, Quantity = ?, SellingPrice = ?, PurchasePrice = ?, " +
                "ReorderLevel = ?, ProductType = ? WHERE ProductID = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, product.getBarcode());
            stmt.setString(2, product.getArabicName());
            stmt.setString(3, product.getEnglishName());
            stmt.setString(4, product.getManufacturer());
            stmt.setDate(5, java.sql.Date.valueOf(product.getExpiryDate()));
            stmt.setDouble(6, product.getQuantity());
            stmt.setDouble(7, product.getSellingPrice());
            stmt.setDouble(8, product.getPurchasePrice());
            stmt.setDouble(9, product.getReorderLevel());
            stmt.setString(10, product.getProductType());
            stmt.setInt(11, product.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Update failed: No product found with ID: " + product.getId());
                tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
                AnimationType type = AnimationType.POPUP;
                tray.setAnimationType(type);
                tray.setTitle("Failed");
                tray.setMessage("Product Update Failed. No Product Found With ID: " + product.getId() + ". Please Try Again Later.");
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(javafx.util.Duration.seconds(2));
            } else {
                System.out.println("Product with ID " + product.getId() + " updated successfully.");
                tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
                AnimationType type = AnimationType.POPUP;
                tray.setAnimationType(type);
                tray.setTitle("Success");
                tray.setMessage("Product Updated Successfully");
                tray.setNotificationType(NotificationType.SUCCESS);
                tray.showAndDismiss(javafx.util.Duration.seconds(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE ProductID = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("No product found with ID: " + id);
                tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
                AnimationType type = AnimationType.POPUP;
                tray.setAnimationType(type);
                tray.setTitle("Failed");
                tray.setMessage("Error Deleting Product. No Product Found With ID: " + id + ". Please Try Again Later.");
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(javafx.util.Duration.seconds(2));
            } else {
                System.out.println("Product with ID " + id + " has been deleted.");
                tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
                AnimationType type = AnimationType.POPUP;
                tray.setAnimationType(type);
                tray.setTitle("Success");
                tray.setMessage("Product Deleted Successfully");
                tray.setNotificationType(NotificationType.SUCCESS);
                tray.showAndDismiss(javafx.util.Duration.seconds(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> findByManufacturer(String manufacturer) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE Manufacturer = ?";
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, manufacturer);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Helper method to map ResultSet to a Product object
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return new Product.ProductBuilder()
                .id(rs.getInt("ProductID")) // Map the 'id' column from the database to the object
                .barcode(rs.getString("ProductBarcode"))
                .arabicName(rs.getString("ArabicName"))
                .englishName(rs.getString("EnglishName"))
                .manufacturer(rs.getString("Manufacturer"))
                .expiryDate(rs.getDate("ExpiryDate").toLocalDate())
                .quantity(rs.getDouble("Quantity"))
                .sellingPrice(rs.getDouble("SellingPrice"))
                .purchasePrice(rs.getDouble("PurchasePrice"))
                .reorderLevel(rs.getDouble("ReorderLevel"))
                .productType(rs.getString("ProductType"))
                .build();
    }}