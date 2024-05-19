package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

import java.sql.*;
import java.util.*;


public class DB {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/pharmacyv1?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    static public String logedInUser = null;
    static public String logedInID = null;
//    static public boolean isLogedIn = false;
    static public boolean isLogedIn = true;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    // This method is used to execute SELECT number of rows queries
    public int SelectQueryCount(String TableName) {
        int count = 0;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM " + TableName);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count;
    }

    // This method is used to execute SELECT queries
    public ObservableList<Map<String, Object>> SelectQuery(String TableName) {
        ObservableList<Map<String, Object>> resultList = FXCollections.observableArrayList();
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + TableName);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            //return error message
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("Error", "Table not found");
            resultList.add(row);
        }
        return resultList;
    }

    // This method is used to execute SELECT queries
    public ObservableList<Map<String, Object>> SelectQuery(String TableName, String ColumnName, String Value) {
        ObservableList<Map<String, Object>> resultList = FXCollections.observableArrayList();
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + TableName + " WHERE " + ColumnName + " LIKE ?");
            preparedStatement.setString(1, "%" + Value + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            //return error message
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("Error", "Either Table not found or User not found");
            resultList.add(row);
        }
        return resultList;
    }

    // This method is used to execute SELECT queries to get the total quantity of items out of stock from table medications and products
    public Integer selectQueryOutOfStockItemsQuantity() {
        Integer totalQuantity = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT SUM(Quantity) FROM ( " +
                     "SELECT COUNT(Quantity) AS Quantity FROM medications WHERE Quantity <= 0 " +
                     "UNION ALL " +
                     "SELECT COUNT(Quantity) AS Quantity FROM products WHERE Quantity <= 0 " +
                     ") AS OutOfStockItems")) {

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                totalQuantity = (resultSet.getObject(1) != null) ? resultSet.getInt(1) : null;
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
        return totalQuantity;
    }

    // this method os used to calculate the profit in the last 30 days by subtracting the total cost from the total sales
    public int calculateProfit() {
        int profit = 0;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT (SELECT SUM(TotalSaleAmount) FROM salesinvoices WHERE SalesDate >= DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY)) - (SELECT SUM(TotalCost) FROM purchaseinvoices WHERE PurchaseDate >= DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY))");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            profit = resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return profit;
    }

    //select total number of items bought in the last 30 days
    public int selectQueryTotalItemsBought() {
        int totalItemsBought = 0;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(Quantity) FROM purchaseinvoices WHERE PurchaseDate >= DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY)");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            totalItemsBought = resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return totalItemsBought;
    }

    // This method is used to execute SELECT queries to return the total quantity of expired items
    public int selectQueryExpiredItemsQuantity() {
        int totalQuantity = 0;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(Quantity) FROM medications WHERE ExpiryDate < CURRENT_DATE()");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            totalQuantity = resultSet.getInt(1);
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT SUM(Quantity) FROM products WHERE ExpiryDate < CURRENT_DATE()");
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            resultSet2.next();
            totalQuantity += resultSet2.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return totalQuantity;
    }

    //this method is used to execute SELECT queries to return an int column's sum value
    public int SelectQuerySum(String TableName, String ColumnName) {
        int sum = 0;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(" + ColumnName + ") FROM " + TableName);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            sum = resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return sum;
    }

    // This method is used to execute SELECT queries for login to match username and password
    public Boolean SelectQuery(String TableName, String ColumnName1, String Value1, String ColumnName2, String Value2 ) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + TableName + " WHERE " + ColumnName1 + " = ? AND " + ColumnName2 + " = ?");
            preparedStatement.setString(1, Value1);
            preparedStatement.setString(2, Value2);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            // Log error message
            System.out.println("Either Table not found or User not found");
        }
        return false;
    }

    //this method is used to execute SELECT queries to return the total cost spent to buy items in the last 30 days
    public int SelectQueryTotalCost() {
        int totalCost = 0;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(TotalCost) FROM purchaseinvoices WHERE PurchaseDate >= DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY)");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            totalCost = resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return totalCost;
    }

    //this method is used to execute SELECT queries to return the total sales value
    public int SelectQueryTotalSales() {
        int totalSales = 0;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(TotalSaleAmount) FROM salesinvoices");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            totalSales = resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return totalSales;
    }

    public Map<String, Integer> getStocks() throws SQLException {
        Map<String, Integer> stocks = new HashMap<>();

        String query = "SELECT StockType, QuantityInStock FROM stock";
        ResultSet rs = getConnection().createStatement().executeQuery(query);

        while (rs.next()) {
            String stockType = rs.getString("StockType");
            int quantity = rs.getInt("QuantityInStock");
            stocks.put(stockType, quantity);
        }

        return stocks;
    }

    //This method is used to execute insert queries and return the generated ID
    public int InsertQuery(String TableName, Map<String, Object> ColumnValue) {
        int generatedId = -1; // Default value if there was an error
        try {
            Connection connection = getConnection();
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();
            for (Map.Entry<String, Object> entry : ColumnValue.entrySet()) {
                columns.append(entry.getKey()).append(",");
                values.append("?,");
            }
            columns.deleteCharAt(columns.length() - 1);
            values.deleteCharAt(values.length() - 1);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + TableName + " (" + columns + ") VALUES (" + values + ")", Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            for (Object value : ColumnValue.values()) {
                preparedStatement.setObject(index++, value);
            }
            preparedStatement.executeUpdate();

            // Retrieve the generated key (ID)
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }

            // sout the success message
            System.out.println("Data inserted successfully. Generated ID is " + generatedId);
            // alert to the user that the data has been inserted successfully
            tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
            AnimationType type = AnimationType.POPUP;
            tray.setAnimationType(type);
            tray.setTitle("Success");
            tray.setMessage("Medication Added Successfully. Generated ID is " + generatedId);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(javafx.util.Duration.seconds(2));
        } catch (SQLException e) {
            // Show error message
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Data");
            alert.setContentText("Please enter needed data correctly");
            alert.showAndWait();

            System.out.println(e.getMessage());
        }
        return generatedId;
    }

    //This method is used to execute update queries
    public void UpdateQuery(String TableName, Map<String, Object> ColumnValue, String ColumnName, String Value) {
        if (TableName == null || TableName.isEmpty()) {
            throw new IllegalArgumentException("TableName cannot be null or empty");
        }
        if (ColumnName == null || ColumnName.isEmpty()) {
            throw new IllegalArgumentException("ColumnName cannot be null or empty");
        }
        if (Value == null || Value.isEmpty()) {
            throw new IllegalArgumentException("Value cannot be null or empty");
        }
        if (ColumnValue == null || ColumnValue.isEmpty()) {
            throw new IllegalArgumentException("ColumnValue cannot be null or empty");
        }

        try {
            Connection connection = getConnection();
            StringBuilder columns = new StringBuilder();
            for (Map.Entry<String, Object> entry : ColumnValue.entrySet()) {
                columns.append(entry.getKey()).append(" = ?,");
            }
            columns.deleteCharAt(columns.length() - 1);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + TableName + " SET " + columns + " WHERE " + ColumnName + " = ?");
            int index = 1;
            for (Object value : ColumnValue.values()) {
                preparedStatement.setObject(index++, value);
            }
            preparedStatement.setObject(index, Value);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("No rows updated, check your parameters");
            } else {
                // sout the success message
                System.out.println("Data updated successfully");
                tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
                AnimationType type = AnimationType.POPUP;
                tray.setAnimationType(type);
                tray.setTitle("Success");
                tray.setMessage(TableName + " Updated Successfully");
                tray.setNotificationType(NotificationType.SUCCESS);
                tray.showAndDismiss(javafx.util.Duration.seconds(2));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //This method is used to execute update medication price queries
    public void UpdateMedicationPrice(String TableName, Map<String, Object> ColumnValue, String ColumnName, String Value) {
        if (TableName == null || TableName.isEmpty()) {
            throw new IllegalArgumentException("TableName cannot be null or empty");
        }
        if (ColumnName == null || ColumnName.isEmpty()) {
            throw new IllegalArgumentException("ColumnName cannot be null or empty");
        }
        if (Value == null || Value.isEmpty()) {
            throw new IllegalArgumentException("Value cannot be null or empty");
        }
        if (ColumnValue == null || ColumnValue.isEmpty()) {
            throw new IllegalArgumentException("ColumnValue cannot be null or empty");
        }

        try {
            Connection connection = getConnection();
            StringBuilder columns = new StringBuilder();
            for (Map.Entry<String, Object> entry : ColumnValue.entrySet()) {
                columns.append(entry.getKey()).append(" = ?,");
            }
            columns.deleteCharAt(columns.length() - 1);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + TableName + " SET " + columns + " WHERE " + ColumnName + " = ?");
            int index = 1;
            for (Object value : ColumnValue.values()) {
                preparedStatement.setObject(index++, value);
            }
            preparedStatement.setObject(index, Value);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("No rows updated, check your parameters");
            } else {
                // sout the success message
                System.out.println("Data updated successfully");
                tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
                AnimationType type = AnimationType.POPUP;
                tray.setAnimationType(type);
                tray.setTitle("Success");
                tray.setMessage(TableName + " Updated Successfully");
                tray.setNotificationType(NotificationType.SUCCESS);
                tray.showAndDismiss(javafx.util.Duration.seconds(2));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //This method is used to execute delete queries
    public void DeleteQuery(String TableName, String ColumnName, String Value) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + TableName + " WHERE " + ColumnName + " = ?");
            preparedStatement.setString(1, Value);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // sout the success message
        System.out.println("Data deleted successfully");
    }

    public ObservableList<Map<String, Object>> searchMedicationsAndSuppliers(String searchField, String searchValue, List<String> columnsToRetain) {
        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement;

            if (searchValue.isEmpty()) {
                preparedStatement = connection.prepareStatement("SELECT * FROM medications JOIN suppliers ON medications.Manufacturer = suppliers.ParentCompany");
            } else {
                preparedStatement = connection.prepareStatement("SELECT * FROM medications JOIN suppliers ON medications.Manufacturer = suppliers.ParentCompany WHERE " + searchField + " LIKE ?");
                preparedStatement.setString(1, "%" + searchValue + "%");
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    if (columnsToRetain.contains(metaData.getColumnName(i))) {
                        row.put(metaData.getColumnName(i), resultSet.getObject(i));
                    }
                }
                data.add(row);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return data;
    }
}