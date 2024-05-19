package project.pharmacyv1;

import Database.DB;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class LogWriter {

    String filename = "F:\\Pharmacy Backup\\Pharmacy-Management-System\\src\\main\\java\\Database\\Log";

    public void LoginSuccess(String name, String time) {
        System.out.println("User " + name + " logged in at " + time);
        LoginController LC = new LoginController();
        try {
            FileWriter writer = new FileWriter(filename, true);
            writer.write("User " + name + " logged in at " + time + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }

    }

    public void LoginFailure(String name, String time) {
        System.out.println("User " + name + " failed to log in at " + time);
        LoginController LC = new LoginController();
        try {
            FileWriter writer = new FileWriter(filename, true);
            writer.write("User " + name + " failed to log in at " + time + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public void Logout() {
        String name = DB.logedInUser;
        String time = new Date().toString();
        System.out.println("User " + name + " logged out at " + time);
        LoginController LC = new LoginController();
        try {
            FileWriter writer = new FileWriter(filename, true);
            writer.write("User " + name + " logged out at " + time + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public void AddItem(String name , String item) {
        String time = new Date().toString();
        System.out.println("User " + name + " added " + item + " at " + time);
        try {
            FileWriter writer = new FileWriter(filename, true);
            writer.write("User " + name + " added " + item + " at " + time + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public void RemoveItem(String name , String item) {
        String time = new Date().toString();
        System.out.println("User " + name + " removed " + item + " at " + time);
        try {
            FileWriter writer = new FileWriter(filename, true);
            writer.write("User " + name + " removed " + item + " at " + time + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public void EditItem(String name , String item) {
        String time = new Date().toString();
        System.out.println("User " + name + " edited " + item + " at " + time);
        try {
            FileWriter writer = new FileWriter(filename, true);
            writer.write("User " + name + " edited " + item + " at " + time + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public void SoldItem(String name , String id) {
        String time = new Date().toString();
        System.out.println("User " + name + " Sales Invoice ID " + id + " at " + time);
        try {
            FileWriter writer = new FileWriter(filename, true);
            writer.write("User " + name + " Sales Invoice ID " + id + " at " + time + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
}
