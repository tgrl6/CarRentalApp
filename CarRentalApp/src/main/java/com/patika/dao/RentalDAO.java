package com.patika.dao;

import com.patika.model.Rental;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO {
    private final Connection connection;

    public RentalDAO(Connection connection) {
        this.connection = connection;
    }

    public void rentVehicle(Rental rental) throws SQLException {
        String sql = "INSERT INTO rentals (user_id, vehicle_id, start_date, end_date, duration_type, total_price) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rental.getUserId());
            stmt.setInt(2, rental.getVehicleId());
            stmt.setDate(3, rental.getStartDate());
            stmt.setDate(4, rental.getEndDate());
            stmt.setString(5, rental.getDurationType());
            stmt.setDouble(6, rental.getTotalPrice());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                System.out.println(" Kiralama işlemi başarılı.");
            } else {
                System.out.println(" Kiralama işlemi başarısız.");
            }
        }
    }

    public List<Rental> getRentalsByUserId(int userId) throws SQLException {
        List<Rental> rentals = new ArrayList<>();

        String sql = "SELECT * FROM rentals WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Rental rental = new Rental(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("vehicle_id"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("duration_type"),
                        rs.getDouble("total_price")
                );
                rentals.add(rental);
            }
        }

        return rentals;
    }

    public void returnRental(int rentalId) throws SQLException {
        String sql = "UPDATE rentals SET is_returned = true WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rentalId);
            stmt.executeUpdate();
        }

    }

    public List<Rental> getActiveRentalsByUserId(int userId) throws SQLException {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals WHERE user_id = ? AND is_returned = false";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rentals.add(new Rental(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("vehicle_id"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("duration_type"),
                        rs.getDouble("total_price")
                ));
            }
        }
        return rentals;
    }






}
