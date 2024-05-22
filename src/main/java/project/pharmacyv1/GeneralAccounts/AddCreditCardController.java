package project.pharmacyv1.GeneralAccounts;

import Database.DB;
import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Polyline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddCreditCardController {

    @FXML
    private TextField CVC;

    @FXML
    private TextField CardNumber;

    @FXML
    private TextField ValidMonth;

    @FXML
    private Button addButton;

    @FXML
    private TextField cardholderName;

    @FXML
    private TextField year;

    @FXML
    private Polyline starHologram;

    public void initialize() {

        addButton.setOnAction(e -> addCard());

        Timeline timeline = new Timeline();

        KeyValue kv1 = new KeyValue(starHologram.fillProperty(), Color.SILVER);
        KeyValue kv2 = new KeyValue(starHologram.fillProperty(), Color.WHITE);

        KeyFrame kf1 = new KeyFrame(Duration.ZERO, kv1);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(1), kv2);
        KeyFrame kf3 = new KeyFrame(Duration.seconds(2), kv1);

        timeline.getKeyFrames().addAll(kf1, kf2, kf3);

        timeline.setAutoReverse(true);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void addCard() {
        // Validate that all fields are filled
        if (cardholderName.getText().isEmpty() || CardNumber.getText().isEmpty() || CVC.getText().isEmpty() || ValidMonth.getText().isEmpty() || year.getText().isEmpty()) {
            showAlert("Error", "Incomplete Fields", "Please fill all the fields.");
            return;
        }

        // Validate that the card number is 16 digits
        if (CardNumber.getText().length() != 16 || !CardNumber.getText().matches("\\d{16}")) {
            showAlert("Error", "Invalid Card Number", "Card Number should be 16 digits and numeric.");
            return;
        }

        // Validate that the CVC is 3 or 4 digits and numeric
        if (!CVC.getText().matches("\\d{3,4}")) {
            showAlert("Error", "Invalid CVC", "CVC should be 3 or 4 digits and numeric.");
            return;
        }

        // Validate that the month is between 01 and 12
        int month;
        try {
            month = Integer.parseInt(ValidMonth.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Month", "Month should be numeric.");
            return;
        }
        if (month < 1 || month > 12) {
            showAlert("Error", "Invalid Month", "Month should be between 01 and 12.");
            return;
        }

        // Validate that the year is numeric and reasonable (e.g., not in the past)
        int yearValue;
        try {
            yearValue = Integer.parseInt(year.getText()) + 2000;
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Year", "Year should be numeric.");
            return;
        }
        Date currentDate = new Date();
        if (yearValue < currentDate.getYear()){
            showAlert("Error", "Invalid Year", "Year should not be in the past.");
            System.out.println("Year: " + yearValue + " Current Year: " + currentDate.getYear());
            return;
        }

        // Create a valid date string and validate it
        String dateString = "01/" + String.format("%02d", month) + "/" + yearValue;
        Date date;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        } catch (ParseException e) {
            showAlert("Error", "Invalid Date", "The date format is invalid.");
            return;
        }

        // Check if the date is in the past
        if (date.before(new Date())) {
            showAlert("Error", "Expired Card", "The expiration date cannot be in the past.");
            return;
        }
        // Check if the card is already in the database
        DB db = new DB();
        if (db.SelectQuery("creditcard", "CardNumber", CardNumber.getText()).size() > 0) {
            showAlert("Error", "Duplicate Card", "This card is already in the database.");
            return;
        }

        // Prepare data for insertion
        Map<String, Object> data = new HashMap<>();
        data.put("CardNumber", CardNumber.getText());
        data.put("CardholderName", cardholderName.getText());
        data.put("CardCVC", CVC.getText());
        data.put("CardValidThru", date);

        // Insert data into the database
        db.InsertQuery("creditcard", data);

        System.out.println("Card Added");
    }

    // Helper method to show alert
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
