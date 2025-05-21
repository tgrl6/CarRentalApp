package com.patika.dao;

import com.patika.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private final Connection connection;
    private final BCryptPasswordEncoder encoder;

    public UserDAO(Connection connection) {
        this.connection = connection;
        this.encoder = new BCryptPasswordEncoder();
    }

    // Kullanıcı eklemek için
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, email, password_hash, birth_date, is_corporate, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());

            // BCrypt ile şifreyi hashle
            String hashedPassword = encoder.encode(user.getPasswordHash());
            stmt.setString(3, hashedPassword);

            stmt.setDate(4, user.getBirthDate());
            stmt.setBoolean(5, user.isCorporate());
            stmt.setString(6, user.getRole());

            stmt.executeUpdate();
        }
    }

    // Email ile kullanıcıyı bul
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getDate("birth_date"),
                        rs.getBoolean("is_corporate"),
                        rs.getString("role")
                );
            }
        }
        return null;
    }

    // Giriş işlemi: email + şifre doğrulama
    public User login(String email, String plainPassword) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");

                // Şifre doğru mu?
                if (encoder.matches(plainPassword, storedHash)) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            storedHash,
                            rs.getDate("birth_date"),
                            rs.getBoolean("is_corporate"),
                            rs.getString("role")
                    );
                }
            }
        }
        return null;
    }
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getDate("birth_date"),
                        rs.getBoolean("is_corporate"),
                        rs.getString("role")
                );
                users.add(user);
            }
        }
        return users;
    }
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, email = ?, birth_date = ?, is_corporate = ?, role = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setDate(3, user.getBirthDate());
            stmt.setBoolean(4, user.isCorporate());
            stmt.setString(5, user.getRole());
            stmt.setInt(6, user.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(" Kullanıcı güncellendi: ID = " + user.getId());
            } else {
                System.out.println(" Güncellenecek kullanıcı bulunamadı (ID = " + user.getId() + ")");
            }
        }
    }
    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("️ Kullanıcı silindi (ID = " + id + ")");
            } else {
                System.out.println("⚠ Kullanıcı bulunamadı (ID = " + id + ")");
            }
        }
    }



}
