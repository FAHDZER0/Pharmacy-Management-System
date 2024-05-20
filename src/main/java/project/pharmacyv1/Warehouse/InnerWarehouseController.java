package project.pharmacyv1.Warehouse;

import javafx.animation.*;
import javafx.fxml.FXML;
import Database.DB;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.util.Duration;
import project.pharmacyv1.DashboardController;

import java.sql.SQLException;
import java.util.Map;
import java.util.Random;

public class InnerWarehouseController {

    @FXML
    public BorderPane SecondaryBordaerPane;

    private DB db = new DB();
    private GridPane gridPane;
    DashboardController DC = new DashboardController();

    @FXML
    public void initialize() {
        setupGridPane();
        populateGridPane();
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        Label title = new Label("Inner Warehouse");
        if(DC.Language.equals("en")) {
            title.setText("Inner Warehouse");
        } else if (DC.Language.equals("ar")) {
            title.setText("المخازن الداخلية");
        }
        title.setStyle("-fx-font-size: 50pt; -fx-text-fill: #000; -fx-font-weight:bold;"); // Larger font and blue color
        vBox.getChildren().addAll(title , gridPane);
        SecondaryBordaerPane.setCenter(vBox);
    }

    // Array of calm colors
    private final String[] calmColors = {
            "#7EBDC2", // Light blue
            "#A3D2CA", // Light green
            "#E0E4CC", // Light yellow
            "#E7E5DF", // Light grey
            "#D4DBDB"  // Light lavender
    };

    // Method to generate a random calm color
    private String getRandomCalmColor() {
        Random rand = new Random();
        int index = rand.nextInt(calmColors.length);
        return calmColors[index];
    }

    private void setupGridPane() {
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.prefWidthProperty().bind(SecondaryBordaerPane.widthProperty());
        gridPane.prefHeightProperty().bind(SecondaryBordaerPane.heightProperty());
    }

    public Label label ;

    private void populateGridPane() {
        try {
            Map<String, Integer> stocks = db.getStocks();
            int row = 0;
            for (Map.Entry<String, Integer> entry : stocks.entrySet()) {
                Label l1 = new Label("Stock Type: " + entry.getKey());
                Label l2 = new Label("Quantity: " + entry.getValue());
                if(DC.Language.equals("en")) {
                    l1.setText("Stock Type: " + entry.getKey());
                    l2.setText("Quantity: " + entry.getValue());
                } else if (DC.Language.equals("ar")) {
                    l1.setText("نوع المخزون: " + entry.getKey());
                    l2.setText("الكمية: " + entry.getValue());
                }
                    l1.setStyle("-fx-font-size: 16pt; -fx-text-fill: #336699; -fx-font-weight:bold;"); // Larger font and blue color
                l2.setStyle("-fx-font-size: 16pt; -fx-text-fill: #336699; -fx-font-weight:bold;"); // Larger font and blue color
                VBox vBox = new VBox();
                vBox.setPadding(new Insets(10));
                vBox.getChildren().addAll(l1, l2);
                HBox hBox = new HBox();
                hBox.getChildren().addAll(vBox , getItemPieChart(entry.getValue(), l2)); // Pass l2 to getItemPieChart
                hBox.setStyle("-fx-background-color: " + getRandomCalmColor() + "; -fx-border-color: #336699; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");
                gridPane.add(hBox, 0, row);
                animatePieChart((PieChart) hBox.getChildren().get(1), l2); // Pass l2 to animatePieChart
                row++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void animatePieChart(PieChart pieChart, Label l2) {
        FadeTransition ft = new FadeTransition(Duration.millis(500), l2); // Use the passed l2
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        ft.play();
        ft.setOnFinished(event1 -> {
            //adding the text with animation to add more effect like showing the percentage char by char
            String text = l2.getText();
            int quantity = Integer.parseInt(text.substring(text.indexOf(":") + 2));
            int totalQuantity = getTotalQuantity(); // You need to implement this method
            int percentage = (quantity * 100) / totalQuantity;
            Timeline timeline = new Timeline();
            for (int i = 0; i <= percentage; i++) {
                int finalI = i;
                KeyFrame keyFrame = new KeyFrame(Duration.millis(50 * i), e -> {
                    if (DC.Language.equals("en"))
                        l2.setText("Quantity: " + finalI + "%");
                    else if (DC.Language.equals("ar"))
                        l2.setText("الكمية: " + finalI + "%");
                });
                timeline.getKeyFrames().add(keyFrame);
            }

            timeline.play();

            Timeline timeline1 = new Timeline();
            // make the i increment by 2 to make the animation faster
            for (int i = 0; i <= quantity; i += 7) {
                int finalI = i;
                KeyFrame keyFrame = new KeyFrame(Duration.millis(50 * i), e -> {
                    pieChart.getData().get(0).setPieValue(finalI);
                    pieChart.getData().get(1).setPieValue(totalQuantity - finalI);
                });
                timeline1.getKeyFrames().add(keyFrame);
            }
            timeline1.setOnFinished(event2 -> {
                pieChart.getData().get(0).setPieValue(quantity);
                pieChart.getData().get(1).setPieValue(totalQuantity - quantity);
            });
            timeline1.setRate(1);
            timeline1.play();

        });
    }

    private int getTotalQuantity() {
        return 100; // You need to implement this method
    }

    private Node getItemPieChart(int quantity, Label l2) {
        PieChart pieChart = new PieChart();
        PieChart.Data slice1 = new PieChart.Data("Available", 0);
        PieChart.Data slice2 = new PieChart.Data("Not Available", 0.01);
        pieChart.getData().add(slice1);
        pieChart.getData().add(slice2);
        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(true);
        //making the pie chart smaller
        pieChart.setStyle("-fx-pref-width: 100px; -fx-pref-height: 100px;");
        return pieChart;
    }

}
