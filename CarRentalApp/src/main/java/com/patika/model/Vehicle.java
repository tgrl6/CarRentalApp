package com.patika.model;

public class Vehicle {
        private int id;
        private String type;
        private String brand;
        private String model;
        private int year;
        private double price;
        private double rentalPrice;
        private boolean isAvailable;

        public Vehicle() {}

        public Vehicle(int id, String type, String brand, String model, int year, double price, double rentalPrice, boolean isAvailable) {
            this.id = id;
            this.type = type;
            this.brand = brand;
            this.model = model;
            this.year = year;
            this.price = price;
            this.rentalPrice = rentalPrice;
            this.isAvailable = isAvailable;
        }

        // GETTER & SETTER'lar (otomatik oluşturabilirsin)
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getBrand() { return brand; }
        public void setBrand(String brand) { this.brand = brand; }

        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }

        public int getYear() { return year; }
        public void setYear(int year) { this.year = year; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public double getRentalPrice() { return rentalPrice; }
        public void setRentalPrice(double rentalPrice) { this.rentalPrice = rentalPrice; }

        public boolean isAvailable() { return isAvailable; }
        public void setAvailable(boolean available) { isAvailable = available; }

        @Override
        public String toString() {
            return "Vehicle{" +
                    "id=" + id +
                    ", type='" + type + '\'' +
                    ", brand='" + brand + '\'' +
                    ", model='" + model + '\'' +
                    ", year=" + year +
                    ", price=" + price +
                    ", rentalPrice=" + rentalPrice +
                    ", isAvailable=" + isAvailable +
                    '}';
        }
    }



