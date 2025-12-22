package project.pharmacyv1;

import Database.DB;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.Date;

public class LogWriter {
    private static LogWriter instance;
    private final String filename = "src/main/java/Database/Log";

    private LogWriter() { }

    public static LogWriter getInstance() {
        if (instance == null) {
            synchronized (LogWriter.class) {
                if (instance == null) {
                    instance = new LogWriter();
                }
            }
        }
        return instance;
    }

    private void writeLog(String message) {
        System.out.println(message);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to log file:");
            e.printStackTrace();
        }
    }

    public void loginSuccess(String name, String time) {
        writeLog("User " + name + " logged in at " + time);
    }

    public void loginFailure(String name, String time) {
        writeLog("User " + name + " failed to log in at " + time);
    }

    public void logout() {
        String name = DB.logedInUser;
        String time = new Date().toString();
        writeLog("User " + name + " logged out at " + time);
    }

    public void addItem(String name, String item) {
        String time = new Date().toString();
        writeLog("User " + name + " added " + item + " at " + time);
    }

    public void removeItem(String name, String item) {
        String time = new Date().toString();
        writeLog("User " + name + " removed " + item + " at " + time);
    }

    public void editItem(String name, String item) {
        String time = new Date().toString();
        writeLog("User " + name + " edited " + item + " at " + time);
    }

    public void soldItem(String name, String id) {
        String time = new Date().toString();
        writeLog("User " + name + " Sales Invoice ID " + id + " at " + time);
    }
}
