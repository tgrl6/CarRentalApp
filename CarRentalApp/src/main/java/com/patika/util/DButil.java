package com.patika.util;

import java.sql.*;

public class DButil {
    private static final String URL = "jdbc:postgresql://localhost:5432/CarRentalDB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "2754";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL sürücüsü başarıyla kaydedildi.");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver bulunamadı!");
            e.printStackTrace();
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println("Veritabanına bağlanıldı!");

            // Test: users tablosundan veri çek
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {

                while (resultSet.next()) {
                    System.out.println(
                            "ID: " + resultSet.getInt("id") +
                                    ", Username: " + resultSet.getString("username") +
                                    ", Email: " + resultSet.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Veritabanı hatası!");
            e.printStackTrace();
        }
    }
}





