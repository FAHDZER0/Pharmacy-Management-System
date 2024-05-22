package project.pharmacyv1;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorPickerController {

    @FXML
    private ColorPicker bgColor1Picker;

    @FXML
    private ColorPicker bgColor2Picker;

    @FXML
    private ColorPicker bgColor3Picker;

    @FXML
    private ColorPicker textColor1Picker;

    @FXML
    private ColorPicker mainGridColorPicker;

    @FXML
    private ColorPicker topGridColorPicker;

    @FXML
    private ColorPicker grid1ColorPicker;

    @FXML
    private ColorPicker grid2ColorPicker;

    @FXML
    private ColorPicker grid3ColorPicker;

    @FXML
    private ColorPicker grid4ColorPicker;

    @FXML
    private ColorPicker grid5ColorPicker;

    @FXML
    private ColorPicker grid6ColorPicker;

    @FXML
    private Button close;

    private Stage dashboardStage;

    public void setDashboardStage(Stage dashboardStage) {
        this.dashboardStage = dashboardStage;
    }



    @FXML
    public void initialize() {

        close.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Are you sure you want to close the color picker?");
            alert.setContentText("The changes will be applied after restarting the program.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                dashboardStage.close();
                bgColor1Picker.getScene().getWindow().hide();
            } else {
                alert.close();
            }
        });

        Map<String, Color> colorMap = loadColorsFromCSS("F:\\Pharmacy Backup\\Pharmacy-Management-System\\src\\main\\resources\\project\\pharmacyv1\\Colors.css");
        if (colorMap != null) {
            bgColor1Picker.setValue(colorMap.get("-fx-bg-color-1"));
            bgColor2Picker.setValue(colorMap.get("-fx-bg-color-2"));
            bgColor3Picker.setValue(colorMap.get("-fx-bg-color-3"));
            textColor1Picker.setValue(colorMap.get("-fx-textcolor1"));
            mainGridColorPicker.setValue(colorMap.get("-fx-main-grid-pane-bg-color"));
            topGridColorPicker.setValue(colorMap.get("-fx-topGrid"));
            grid1ColorPicker.setValue(colorMap.get("-fx-grid-1"));
            grid2ColorPicker.setValue(colorMap.get("-fx-grid-2"));
            grid3ColorPicker.setValue(colorMap.get("-fx-grid-3"));
            grid4ColorPicker.setValue(colorMap.get("-fx-grid-4"));
            grid5ColorPicker.setValue(colorMap.get("-fx-grid-5"));
            grid6ColorPicker.setValue(colorMap.get("-fx-grid-6"));
        }

        addColorPickerListeners();
    }

    private void addColorPickerListeners() {
        bgColor1Picker.valueProperty().addListener((observable, oldValue, newValue) -> applyColors());
        bgColor2Picker.valueProperty().addListener((observable, oldValue, newValue) -> applyColors());
        bgColor3Picker.valueProperty().addListener((observable, oldValue, newValue) -> applyColors());
        textColor1Picker.valueProperty().addListener((observable, oldValue, newValue) -> applyColors());
        mainGridColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> applyColors());
        topGridColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> applyColors());
        grid1ColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> applyColors());
        grid2ColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> applyColors());
        grid3ColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> applyColors());
        grid4ColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> applyColors());
        grid5ColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> applyColors());
        grid6ColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> applyColors());
    }

    @FXML
    private void applyColors() {
        Color bgColor1 = bgColor1Picker.getValue();
        Color bgColor2 = bgColor2Picker.getValue();
        Color bgColor3 = bgColor3Picker.getValue();
        Color textColor1 = textColor1Picker.getValue();
        Color mainGridColor = mainGridColorPicker.getValue();
        Color topGridColor = topGridColorPicker.getValue();
        Color grid1Color = grid1ColorPicker.getValue();
        Color grid2Color = grid2ColorPicker.getValue();
        Color grid3Color = grid3ColorPicker.getValue();
        Color grid4Color = grid4ColorPicker.getValue();
        Color grid5Color = grid5ColorPicker.getValue();
        Color grid6Color = grid6ColorPicker.getValue();

        updateCSSFile(bgColor1, bgColor2, bgColor3, textColor1, mainGridColor, topGridColor, grid1Color, grid2Color, grid3Color, grid4Color, grid5Color, grid6Color);

//        bgColor1Picker.getScene().getWindow().hide();


    }
    @FXML
    public void resetColors(){
        bgColor1Picker.setValue(Color.web("#1996aa"));
        bgColor2Picker.setValue(Color.web("#a1d6e2"));
        bgColor3Picker.setValue(Color.web("#f1f1f2"));
        textColor1Picker.setValue(Color.web("#ffffff"));
        mainGridColorPicker.setValue(Color.web("#f1f1f2"));
        topGridColorPicker.setValue(Color.web("#147de2"));
        grid1ColorPicker.setValue(Color.web("#147de2"));
        grid2ColorPicker.setValue(Color.web("#3c9247"));
        grid3ColorPicker.setValue(Color.web("#f54934"));
        grid4ColorPicker.setValue(Color.web("#f48800"));
        grid5ColorPicker.setValue(Color.web("#9635af"));
        grid6ColorPicker.setValue(Color.web("#ff0f87"));
        applyColors();
    }

    private void updateCSSFile(Color bgColor1, Color bgColor2, Color bgColor3, Color textColor1, Color mainGridColor, Color topGridColor, Color grid1Color, Color grid2Color, Color grid3Color, Color grid4Color, Color grid5Color, Color grid6Color) {
        String cssContent = String.format(
                ".root {\n" +
                        "    -fx-bg-color-1: %s;\n" +
                        "    -fx-bg-color-2: %s;\n" +
                        "    -fx-bg-color-3: %s;\n" +
                        "    -fx-textcolor1: %s;\n" +
                        "    -fx-main-grid-pane-bg-color: %s;\n" +
                        "    -fx-topGrid: %s;\n" +
                        "    -fx-grid-1: %s;\n" +
                        "    -fx-grid-2: %s;\n" +
                        "    -fx-grid-3: %s;\n" +
                        "    -fx-grid-4: %s;\n" +
                        "    -fx-grid-5: %s;\n" +
                        "    -fx-grid-6: %s;\n" +
                        "}\n",
                toHexString(bgColor1),
                toHexString(bgColor2),
                toHexString(bgColor3),
                toHexString(textColor1),
                toHexString(mainGridColor),
                toHexString(topGridColor),
                toHexString(grid1Color),
                toHexString(grid2Color),
                toHexString(grid3Color),
                toHexString(grid4Color),
                toHexString(grid5Color),
                toHexString(grid6Color)
        );

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("F:\\Pharmacy Backup\\Pharmacy-Management-System\\src\\main\\resources\\project\\pharmacyv1\\Colors.css"))) {
            writer.write(cssContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private Map<String, Color> loadColorsFromCSS(String cssFilePath) {
        Map<String, Color> colorMap = new HashMap<>();
        Pattern pattern = Pattern.compile("(-fx-[\\w-]+):\\s*(#[0-9a-fA-F]{6});");

        try (BufferedReader reader = new BufferedReader(new FileReader(cssFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String property = matcher.group(1);
                    String colorValue = matcher.group(2);
                    Color color = Color.web(colorValue);
                    colorMap.put(property, color);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return colorMap;
    }
}
