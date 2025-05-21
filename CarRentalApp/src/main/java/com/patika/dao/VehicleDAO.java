package com.patika.dao;

import com.patika.model.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {private final Connection connection;

    public VehicleDAO(Connection connection) {
        this.connection = connection;
    }

    public void addVehicle(Vehicle vehicle) throws SQLException {
        String sql = "INSERT INTO vehicles (type, brand, model, year, price, rental_price, is_available) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, vehicle.getType());
            stmt.setString(2, vehicle.getBrand());
            stmt.setString(3, vehicle.getModel());
            stmt.setInt(4, vehicle.getYear());
            stmt.setDouble(5, vehicle.getPrice());
            stmt.setDouble(6, vehicle.getRentalPrice());
            stmt.setBoolean(7, vehicle.isAvailable());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                System.out.println("Araç veritabanına eklendi: " + vehicle.getBrand() + " " + vehicle.getModel());
            } else {
                System.out.println("Araç eklenemedi.");
            }
        }
    }
    public List<Vehicle> getAllVehicles() throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getDouble("price"),
                        rs.getDouble("rental_price"),
                        rs.getBoolean("is_available")
                );
                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }
    public List<Vehicle> getVehiclesByType(String type) throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE type = ? AND is_available = true";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type.toLowerCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getDouble("price"),
                        rs.getDouble("rental_price"),
                        rs.getBoolean("is_available")
                );
                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }
    public void markAsUnavailable(int vehicleId) throws SQLException {
        String sql = "UPDATE vehicles SET is_available = false WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, vehicleId);
            stmt.executeUpdate();
        }
    }

    public void markAsAvailable(int vehicleId) throws SQLException {
        String sql = "UPDATE vehicles SET is_available = true WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, vehicleId);
            stmt.executeUpdate();
        }
    }






}
