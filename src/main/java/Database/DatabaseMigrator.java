package Database;

import java.sql.*;

public class DatabaseMigrator {
    private static final String SOURCE_DB_URL = "jdbc:mysql://mysql-1f24e996-fahdahmed132-a849.h.aivencloud.com:17858/defaultdb?sslmode=require";
    private static final String SOURCE_DB_USERNAME = "avnadmin";
    private static final String SOURCE_DB_PASSWORD = "AVNS_2uCizTOWsxwddX2mOTk";

    private static final String TARGET_DB_URL = "jdbc:mysql://localhost:3306/pharmacyv1?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String TARGET_DB_USERNAME = "root";
    private static final String TARGET_DB_PASSWORD = "";

    public boolean isTargetDatabaseConnected() {
        try (Connection connection = DriverManager.getConnection(TARGET_DB_URL, TARGET_DB_USERNAME, TARGET_DB_PASSWORD)) {
            return connection.isValid(2);
        } catch (SQLException e) {
            System.out.println("Error while connecting to the local database: " + e.getMessage());
            return false;
        }
    }

    public boolean isSourceDatabaseConnected() {
        try (Connection connection = DriverManager.getConnection(SOURCE_DB_URL, SOURCE_DB_USERNAME, SOURCE_DB_PASSWORD)) {
            return connection.isValid(2);
        } catch (SQLException e) {
            System.out.println("Error while connecting to the remote database: " + e.getMessage());
            return false;
        }
    }

    public void deleteDataFromTable(String tableName) throws SQLException {
        try (
                Connection targetConnection = DriverManager.getConnection(TARGET_DB_URL, TARGET_DB_USERNAME, TARGET_DB_PASSWORD);
                Statement targetStatement = targetConnection.createStatement()
        ) {
            targetStatement.executeUpdate("DELETE FROM " + tableName);
            System.out.println("Deleted all data from table " + tableName);
        } catch (SQLException e) {
            throw e;
        }
    }

    public void deleteDataFromOnlineTable(String tableName) throws SQLException {
        try (
                Connection onlineConnection = DriverManager.getConnection(SOURCE_DB_URL, SOURCE_DB_USERNAME, SOURCE_DB_PASSWORD);
                Statement onlineStatement = onlineConnection.createStatement()
        ) {
            onlineStatement.executeUpdate("DELETE FROM " + tableName);
            System.out.println("Deleted all data from online table " + tableName);
        } catch (SQLException e) {
            throw e;
        }
    }

    public void migrateTable(String tableName) throws SQLException {
        try (
                Connection sourceConnection = DriverManager.getConnection(SOURCE_DB_URL, SOURCE_DB_USERNAME, SOURCE_DB_PASSWORD);
                Connection targetConnection = DriverManager.getConnection(TARGET_DB_URL, TARGET_DB_USERNAME, TARGET_DB_PASSWORD);
                Statement sourceStatement = sourceConnection.createStatement();
                Statement targetStatement = targetConnection.createStatement();
        ) {
            ResultSet sourceResultSet = sourceStatement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = sourceResultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            StringBuilder insertQuery = new StringBuilder("INSERT INTO " + tableName + " VALUES (");
            for (int i = 1; i <= columnCount; i++) {
                insertQuery.append("?, ");
            }
            insertQuery.deleteCharAt(insertQuery.length() - 2);
            insertQuery.append(")");

            try (PreparedStatement insertStatement = targetConnection.prepareStatement(insertQuery.toString())) {
                while (sourceResultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        insertStatement.setObject(i, sourceResultSet.getObject(i));
                    }
                    insertStatement.executeUpdate();
                }
                System.out.println("Finished loading table " + tableName);
            }
        } catch (SQLException e) {
            throw e;
        }
    }
    public void migrateToOnlineTable(String tableName) throws SQLException {
        try (
                Connection sourceConnection = DriverManager.getConnection(TARGET_DB_URL, TARGET_DB_USERNAME, TARGET_DB_PASSWORD);
                Connection onlineConnection = DriverManager.getConnection(SOURCE_DB_URL, SOURCE_DB_USERNAME, SOURCE_DB_PASSWORD);
                Statement sourceStatement = sourceConnection.createStatement();
        ) {
            ResultSet sourceResultSet = sourceStatement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = sourceResultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            StringBuilder insertQuery = new StringBuilder("INSERT INTO " + tableName + " VALUES (");
            for (int i = 1; i <= columnCount; i++) {
                insertQuery.append("?, ");
            }
            insertQuery.deleteCharAt(insertQuery.length() - 2);
            insertQuery.append(")");

            try (PreparedStatement insertStatement = onlineConnection.prepareStatement(insertQuery.toString())) {
                while (sourceResultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        insertStatement.setObject(i, sourceResultSet.getObject(i));
                    }
                    insertStatement.executeUpdate();
                }
                System.out.println("Finished loading online table " + tableName);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

}
