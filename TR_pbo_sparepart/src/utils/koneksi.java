package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/tr_pbo";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
