package ru.svidersky.javafx.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SQLiteConnection {
    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            String url = "jdbc:sqlite:db\\birthdays.db";
            return DriverManager.getConnection(url);
        } catch (Exception ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
