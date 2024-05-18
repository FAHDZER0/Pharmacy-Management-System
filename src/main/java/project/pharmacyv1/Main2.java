package project.pharmacyv1;

import Database.DatabaseMigrator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.UnknownHostException;
import java.sql.SQLException;

public class Main2 extends Application {

    @Override
    public void start(Stage stage) {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        Label loadingLabel = new Label("Loading Database...");
        VBox vbox = new VBox(progressIndicator, loadingLabel);
        vbox.setAlignment(Pos.CENTER);

        Task<Void> loadDatabaseTask = createDatabaseLoadTask(stage);
        progressIndicator.progressProperty().bind(loadDatabaseTask.progressProperty());
        new Thread(loadDatabaseTask).start();

        stage.setScene(new Scene(vbox));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Loading Database...");
        stage.setMinWidth(300);
        stage.setMinHeight(200);
        stage.show();

        loadDatabaseTask.setOnSucceeded(e -> {
            if (loadDatabaseTask.getException() == null) {
                stage.setScene(loadLoginScene());
                stage.centerOnScreen();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Database Migration Completed");
                alert.setHeaderText(null);
                alert.setContentText("The local database has been updated with the latest data from the source database.");

                alert.showAndWait();
            }
        });

        loadDatabaseTask.setOnFailed(e -> {
            Throwable exception = loadDatabaseTask.getException();
            handleDatabaseLoadError(stage, exception);
        });
    }

    private Task<Void> createDatabaseLoadTask(Stage stage) {
        return new Task<>() {
            @Override
            protected Void call() throws SQLException {
                DatabaseMigrator databaseMigrator = new DatabaseMigrator();

                if (databaseMigrator.isSourceDatabaseConnected() && databaseMigrator.isTargetDatabaseConnected()) {
                    String[] tableNames = {"customers", "employees", "medications", "products", "salesinvoices", "suppliers", "salesreturns", "stock", "purchaseinvoices", "purchasereturns", "purchasereturndetails", "purchaseinvoicedetails", "salesinvoicedetails", "salesreturndetails"};
                    String[] tableNames2 = {"salesreturndetails" , "purchasereturndetails" , "purchasereturns" , "purchaseinvoicedetails" , "purchaseinvoices" , "salesreturns" , "salesinvoicedetails", "salesinvoices" , "customers", "employees", "medications", "products", "suppliers", "stock"};

                    // Delete data from tables
                    for (String tableName : tableNames2) {
                        databaseMigrator.deleteDataFromTable(tableName);
                    }

                    // Migrate data to tables
                    for (int i = 0; i < tableNames.length; i++) {
                        databaseMigrator.migrateTable(tableNames[i]);
                        updateProgress(i + 1, tableNames.length);
                    }
                } else {
                    try {
                        throw new UnknownHostException("Cannot connect to one or both databases.");
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                }

                return null;
            }
        };
    }

    private Scene loadLoginScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml")); // Adjust path if necessary
            return new Scene(fxmlLoader.load());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void handleDatabaseLoadError(Stage stage, Throwable exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Load Error");
        alert.setHeaderText(null);
        alert.setContentText("An error occurred while loading the database: " + exception.getMessage());

        alert.showAndWait();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
