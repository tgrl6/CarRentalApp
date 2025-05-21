package com.patika;

import java.sql.*;

public class JdbcConnection {
    public static void main(String[] args) {
        // 1. ADIM: JDBC Sürücüsünü Kaydetme (Java 6+ için opsiyonel)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL sürücüsü
            System.out.println("Sürücü başarıyla kaydedildi.");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver bulunamadı!");
            e.printStackTrace();
            return; // Sürücü yoksa uygulama durur.
        }

        // 2. ADIM: Veritabanı Bağlantısı
        String url = "jdbc:postgresql://localhost:5432/CarRentalDB"; // Veritabanı URL
        String user = "tgrl"; // Kullanıcı adı
        String password = "369245"; // Şifre

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Veritabanına bağlanıldı!");

            // 3. ADIM: Sorgu Hazırlama
            Statement statement = connection.createStatement();

            // 4. ADIM: Sorgu Çalıştırma (Örnek: SELECT)
            ResultSet resultSet = statement.executeQuery("SELECT * FROM employees");

            // Sonuçları Okuma
            while (resultSet.next()) {
                System.out.println(
                        "ID: " + resultSet.getInt("id") +
                                ", Ad: " + resultSet.getString("name")
                );
            }

            // 5. ADIM: Kaynakları Kapatma (try-with-resources ile otomatik)
        } catch (SQLException e) {
            System.out.println("Veritabanı hatası!");
            e.printStackTrace();
        }
    }

}
