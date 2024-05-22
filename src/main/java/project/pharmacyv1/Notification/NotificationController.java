package project.pharmacyv1.Notification;

import Database.DB;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import project.pharmacyv1.DashboardController;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class NotificationController {

    @FXML
    private Group closeButton;
    @FXML
    private VBox notificationPane;
    @FXML
    private Label title;
    DB db = new DB();
    DashboardController DC = new DashboardController();

    // this method is for checking the quantity of the medications in the database
    public void checkOutOfStock() {
        ObservableList<Map<String, Object>> data = db.SelectQuery("medications");
        // loop through the data and check if the quantity of the medication is less than 10

        for (Map<String, Object> row : data) {
            if (Integer.parseInt(row.get("Quantity").toString()) == 0) {
                if (DC.Language.equals("en"))
                    notificationPane.getChildren().add(createNotificationItem("Out of Stock!!", "The medication \"" + row.get("EnglishName") + "\" is out of stock"));
                else
                    notificationPane.getChildren().add(createNotificationItem("إنتهت الكمية!!", "الدواء \"" + row.get("ArabicName") + "\" إنتهت الكمية"));

            }else if (Integer.parseInt(row.get("Quantity").toString()) < Integer.parseInt(row.get("ReorderLevel").toString())) {
                if (DC.Language.equals("en"))
                    notificationPane.getChildren().add(createNotificationItem("Running Out!!", "The medication \"" + row.get("EnglishName") + "\" is running out of stock"));
                else
                    notificationPane.getChildren().add(createNotificationItem("تنفدت الكمية!!", "الدواء \"" + row.get("ArabicName") + "\" تنفدت الكمية"));
            }
        }
        ObservableList<Map<String, Object>> data2 = db.SelectQuery("products");
        // loop through the data and check if the quantity of the medication is less than 10

        for (Map<String, Object> row : data2) {
            if (Integer.parseInt(row.get("Quantity").toString()) == 0) {
                if (DC.Language.equals("en"))
                    notificationPane.getChildren().add(createNotificationItem("Out of Stock!!", "The product \"" + row.get("EnglishName") + "\" is out of stock"));
                else
                    notificationPane.getChildren().add(createNotificationItem("إنتهت الكمية!!", "المنتج \"" + row.get("ArabicName") + "\" إنتهت الكمية"));

            }else if (Integer.parseInt(row.get("Quantity").toString()) < Integer.parseInt(row.get("ReorderLevel").toString())) {
                if (DC.Language.equals("en"))
                    notificationPane.getChildren().add(createNotificationItem("Running Out!!", "The product \"" + row.get("EnglishName") + "\" is running out of stock"));
                else
                    notificationPane.getChildren().add(createNotificationItem("تنفدت الكمية!!", "المنتج \"" + row.get("ArabicName") + "\" تنفدت الكمية"));
            }
        }
    }

    public void checkExpiredInStock(){
        ObservableList<Map<String, Object>> data = db.SelectQuery("medications");
        // loop through the data and check if the expiry date of the medication is less than the current date
        for (Map<String, Object> row : data) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate expiryDate = LocalDate.parse(row.get("ExpiryDate").toString(), formatter);
            LocalDate currentDate = LocalDate.now();

            if (expiryDate.isBefore(currentDate)) {
                if (DC.Language.equals("en"))
                    notificationPane.getChildren().add(createNotificationItem("Expired!!", "The medication \"" + row.get("EnglishName") + "\" has expired"));
                else
                    notificationPane.getChildren().add(createNotificationItem("إنتهت الصلاحية!!", "الدواء \"" + row.get("ArabicName") + "\" إنتهت الصلاحية"));
            } else if (expiryDate.isBefore(currentDate.plusDays(30))) {
                if (DC.Language.equals("en"))
                    notificationPane.getChildren().add(createNotificationItem("Expiring Within This Month!!", "The medication \"" + row.get("EnglishName") + "\" will expire this month"));
                else
                    notificationPane.getChildren().add(createNotificationItem("تنتهي الصلاحية خلال هذا الشهر!!", "الدواء \"" + row.get("ArabicName") + "\" ستنتهي الصلاحية خلال هذا الشهر"));
            }
        }
        ObservableList<Map<String, Object>> data2 = db.SelectQuery("products");
        // loop through the data and check if the expiry date of the medication is less than the current date
        for (Map<String, Object> row : data2) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate expiryDate = LocalDate.parse(row.get("ExpiryDate").toString(), formatter);
            LocalDate currentDate = LocalDate.now();

            if (expiryDate.isBefore(currentDate)) {
                if (DC.Language.equals("en"))
                    notificationPane.getChildren().add(createNotificationItem("Expired!!", "The product \"" + row.get("EnglishName") + "\" has expired"));
                else
                    notificationPane.getChildren().add(createNotificationItem("إنتهت الصلاحية!!", "المنتج \"" + row.get("ArabicName") + "\" إنتهت الصلاحية"));
            } else if (expiryDate.isBefore(currentDate.plusDays(30))) {
                if (DC.Language.equals("en"))
                    notificationPane.getChildren().add(createNotificationItem("Expiring Within This Month!!", "The product \"" + row.get("EnglishName") + "\" will expire this month"));
                else
                    notificationPane.getChildren().add(createNotificationItem("تنتهي الصلاحية خلال هذا الشهر!!", "المنتج \"" + row.get("ArabicName") + "\" ستنتهي الصلاحية خلال هذا الشهر"));
            }
        }
    }

    @FXML
    public void initialize() {
        if (DC.Language.equals("en"))
            title.setText("Notifications");
        else
            title.setText("الإشعارات");
        closeButton.setOnMouseClicked(e -> closeNotification());
        checkOutOfStock();
        checkExpiredInStock();
        loadColorsFromCSS();
    }
    @FXML
    public void closeNotification() {
        closeButton.getScene().getWindow().hide();
    }

    public String loadColorsFromCSS() {
        String[] colorParts = null;
        try {
            File file = new File("F:\\Pharmacy Backup\\Pharmacy-Management-System\\src\\main\\resources\\project\\pharmacyv1\\Colors.css");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("-fx-bg-color-1")) {
                    String[] parts = line.split(":");
                    colorParts = parts[1].split(";");
                    try {
                        Color color = Color.web(colorParts[0]);
                    } catch (IllegalArgumentException e) {
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return colorParts[0];
    }

    public Pane createNotificationItem(String title, String message) {
        AnchorPane notificationItem = new AnchorPane();
        notificationItem.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/project/pharmacyv1/Style.css")).toExternalForm());
        String color = loadColorsFromCSS();
        if (title == "Out of Stock!!" || title == "إنتهت الكمية!!")
            color = "#ff0000";
        else if (title == "Expired!!" || title == "إنتهت الصلاحية!!")
            color = "#ff0000";
        else if (title == "Expiring Within This Month!!" || title == "تنتهي الصلاحية خلال هذا الشهر!!")
            color = "green";{
        }
        notificationItem.setStyle("-fx-background-color: "+color+";");
        notificationItem.getStyleClass().add("notification-item");
        notificationItem.setPrefWidth(300);
        notificationItem.setPrefHeight(70);
        notificationItem.setLayoutX(10);
        notificationItem.setLayoutY(10);

        Button closeButton = new Button("X");
        closeButton.setLayoutX(10); // Set X position
        closeButton.setLayoutY(10); // Set Y position
        if (title == "Out of Stock!!" || title == "إنتهت الكمية!!")
            closeButton.setStyle("-fx-background-color: rgba(0,160,255,0); -fx-text-fill: #000000; -fx-font-size: 20;");
        else if (title == "Expired!!" || title == "إنتهت الصلاحية!!")
            closeButton.setStyle("-fx-background-color: rgba(0,160,255,0); -fx-text-fill: #000000; -fx-font-size: 20;");
        else if (title == "Expiring Within This Month!!" || title == "تنتهي الصلاحية خلال هذا الشهر!!")
            closeButton.setStyle("-fx-background-color: rgba(0,160,255,0); -fx-text-fill: #000000; -fx-font-size: 20;");
        else
            closeButton.setStyle("-fx-background-color: rgba(0,160,255,0); -fx-text-fill: #ff0000; -fx-font-size: 20;");
        closeButton.setOnMouseClicked(e -> notificationPane.getChildren().remove(notificationItem));
        notificationItem.getChildren().add(closeButton);

        Label titleLabel = new Label(title);
        titleLabel.setLayoutX(50); // Set X position
        titleLabel.setLayoutY(10); // Set Y position
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20)); // Set font size to 20 and make it bold
        notificationItem.getChildren().add(titleLabel);

        Label messageLabel = new Label(message);
        messageLabel.setLayoutX(50); // Set X position
        messageLabel.setLayoutY(40); // Set Y position
        messageLabel.setFont(Font.font("System", FontWeight.BOLD, 20)); // Set font size to 20 and make it bold
        notificationItem.getChildren().add(messageLabel);

        return notificationItem;
    }
}