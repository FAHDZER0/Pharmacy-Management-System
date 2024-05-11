package project.pharmacyv1;

import Database.DB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginController {

    @FXML
    static public Date LoginDate = new Date();
    @FXML
    private Button LoginButton;
    @FXML
    private TextField UserName;
    @FXML
    private TextField Password;

    LogWriter log = new LogWriter();

    @FXML
    public void CloseButtonAction() {
        Stage stage = (Stage) LoginButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void LoginButtonAction() {
        // Check if the username and password are correct and then open the dashboard
        DB db = new DB();
        boolean result = db.SelectQuery("employees", "EmployeeName" ,getEmployeeName() , "EmployeeID", getEmployeePassword());
        if(result){
            tray.notification.TrayNotification tray = new tray.notification.TrayNotification();
            AnimationType type = AnimationType.POPUP;
            tray.setAnimationType(type);
            tray.setTitle("Welcome");
            tray.setMessage("Welcome User " + getEmployeeName() + " to Pharmacy Management System");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(javafx.util.Duration.seconds(2));
            db.logedInUser = getEmployeeName();
            db.logedInID = getEmployeePassword();
            db.isLogedIn = true;
            //write in Log file that user "name" logged in at "time"
            log.LoginSuccess(getEmployeeName(), getLoginDate());
            openDashboard();

        }else{
            System.out.println("Invalid Username or Password");
            // Show error message
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Username or Password");
            alert.setContentText("Please enter correct username and password");
            alert.showAndWait();
            log.LoginFailure(getEmployeeName(), getLoginDate());
        }


    }

    public String getEmployeeName(){
        return UserName.getText();
    }

    public String getEmployeePassword(){
        return Password.getText();
    }

    public String getLoginDate(){
        LoginDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a dd-MM-yyyy");
        String strDate = formatter.format(LoginDate);

        return strDate;
    }

    public void openDashboard(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/project/pharmacyv1/Dashboard.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root,1330,740);
            stage.setMinWidth(1510);
            stage.setMinHeight(780);
            stage.setTitle("Pharmacy Management System");
            stage.setMaximized(true);
            stage.setScene(scene);
            stage.show();
            Stage stage1 = (Stage) LoginButton.getScene().getWindow();
            stage1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}