package project.pharmacyv1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main2 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/project/pharmacyv1/Dashboard.fxml"));
//        Scene scene = new Scene(root,1330,740);
//        stage.setMinWidth(1510);
//        stage.setMinHeight(780);
//        stage.setTitle("Pharmacy Management System");
//        stage.setMaximized(true);
//        stage.setScene(scene);
//        stage.show();

        Parent root = FXMLLoader.load(getClass().getResource("/project/pharmacyv1/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}