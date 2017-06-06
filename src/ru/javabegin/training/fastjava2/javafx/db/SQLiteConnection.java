package ru.javabegin.training.fastjava2.javafx.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SQLiteConnection {



    public static Connection getConnection() {
        try {

            Class.forName("org.sqlite.JDBC").newInstance();

            // ���� � �� ���������� �������� � ��������� ���� ��������
            String url = "jdbc:sqlite:db\\addressbook.db";// ��������� ������������� ���� � ����� ��

            return DriverManager.getConnection(url);

        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
