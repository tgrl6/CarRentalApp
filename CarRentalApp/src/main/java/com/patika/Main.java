package com.patika;

import com.patika.dao.*;
import com.patika.model.*;
import com.patika.util.DButil;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection conn = DButil.getConnection()) {
            System.out.println(" Veritabanına bağlanıldı.");

            run(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run(Connection conn) throws Exception {
        UserDAO userDAO = new UserDAO(conn);
        VehicleDAO vehicleDAO = new VehicleDAO(conn);
        RentalDAO rentalDAO = new RentalDAO(conn);

        User currentUser = null;

        while (true) {
            if (currentUser == null) {
                System.out.println("\n=== ARAÇ KİRALAMA SİSTEMİ ===");
                System.out.println("1. Giriş Yap");
                System.out.println("2. Kayıt Ol");
                System.out.println("0. Çıkış");
                System.out.print("Seçiminiz: ");
                String secim = scanner.nextLine();

                switch (secim) {
                    case "1":
                        currentUser = login(userDAO);
                        break;
                    case "2":
                        currentUser = register(userDAO);
                        break;
                    case "0":
                        System.out.println("Programdan çıkılıyor...");
                        return;
                    default:
                        System.out.println(" Geçersiz seçim.");
                }
            } else {
                // === ROL KONTROLÜ ===
                if (currentUser.getRole().equalsIgnoreCase("admin")) {
                    System.out.println("\n=== [ADMIN PANELİ] ===");
                    System.out.println("1. Araç Ekle");
                    System.out.println("2. Tüm Araçları Listele");
                    System.out.println("3. Çıkış Yap");
                    System.out.print("Seçiminiz: ");
                    String secim = scanner.nextLine();

                    switch (secim) {
                        case "1":
                            addVehicle(conn);
                            break;
                        case "2":
                            for (Vehicle v : vehicleDAO.getAllVehicles()) {
                                System.out.println(v);
                            }
                            break;
                        case "3":
                            currentUser = null;
                            break;
                        default:
                            System.out.println(" Geçersiz seçim.");
                    }
                } else {
                    System.out.println("\n=== HOŞ GELDİN " + currentUser.getUsername() + " ===");
                    System.out.println("1. Tüm Araçları Listele");
                    System.out.println("2. Araç Kirala");
                    System.out.println("3. Kiralama Geçmişim");
                    System.out.println("4. Araç İade Et");
                    System.out.println("5. Çıkış Yap");
                    System.out.println("6. Çıkış Yap");
                    System.out.print("Seçiminiz: ");
                    String secim = scanner.nextLine();

                    switch (secim) {
                        case "1":
                            for (Vehicle v : vehicleDAO.getAllVehicles()) {
                                System.out.println(v);
                            }
                            break;
                        case "2":
                            rentVehicle(currentUser, conn);
                            break;
                        case "3":
                            for (Rental r : rentalDAO.getRentalsByUserId(currentUser.getId())) {
                                System.out.println(r);
                            }
                            break;
                        case "4":
                            returnVehicle(currentUser, conn);
                            break;
                        case "5":
                            currentUser = null;
                            break;
                        case "6":
                            currentUser = null;
                            break;
                        default:
                            System.out.println("❗ Geçersiz seçim.");
                    }
                }
            }
        }
    }

    public static void addVehicle(Connection conn) throws Exception {
        VehicleDAO vehicleDAO = new VehicleDAO(conn);

        System.out.print("Araç türü (car/motorcycle/helicopter): ");
        String type = scanner.nextLine();
        System.out.print("Marka: ");
        String brand = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();
        System.out.print("Yıl: ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("Fiyat (satın alma): ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Kiralama ücreti (günlük): ");
        double rentalPrice = Double.parseDouble(scanner.nextLine());

        Vehicle vehicle = new Vehicle(0, type, brand, model, year, price, rentalPrice, true);
        vehicleDAO.addVehicle(vehicle);

        System.out.println(" Araç başarıyla eklendi.");
    }

    public static User login(UserDAO userDAO) throws Exception {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Şifre: ");
        String password = scanner.nextLine();

        User user = userDAO.login(email, password);
        if (user != null) {
            System.out.println(" Giriş başarılı.");
            return user;
        } else {
            System.out.println(" Giriş başarısız.");
            return null;
        }
    }

    public static User register(UserDAO userDAO) throws Exception {
        System.out.print("Adınız: ");
        String username = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Şifre: ");
        String password = scanner.nextLine();
        System.out.print("Doğum yılı (örnek: 1995): ");
        int year = Integer.parseInt(scanner.nextLine());

        Date birthDate = Date.valueOf(LocalDate.of(year, 1, 1));
        User user = new User(0, username, email, password, birthDate, false, "customer");

        userDAO.addUser(user);
        System.out.println(" Kayıt başarılı.");
        return userDAO.login(email, password);
    }

    public static void rentVehicle(User user, Connection conn) throws Exception {    VehicleDAO vehicleDAO = new VehicleDAO(conn);
        RentalDAO rentalDAO = new RentalDAO(conn);

        List<Vehicle> available = vehicleDAO.getAllVehicles();
        if (available.isEmpty()) {
            System.out.println(" Müsait araç yok.");
            return;
        }

        System.out.println(" Kiralanabilir Araçlar:");
        for (Vehicle v : available) {
            System.out.println("ID: " + v.getId() + " | " + v.getBrand() + " " + v.getModel() + " (" + v.getType() + ")");
        }

        System.out.print("Kiralayacağınız araç ID: ");
        int vehicleId = Integer.parseInt(scanner.nextLine());

        Vehicle selectedVehicle = vehicleDAO.getAllVehicles().stream()
                .filter(v -> v.getId() == vehicleId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Araç bulunamadı."));

        //  Yaş ve kurumsal kontrol
        int userAge = LocalDate.now().getYear() - user.getBirthDate().toLocalDate().getYear();

        // Kurumsal kullanıcı en az 30 gün kuralı
        System.out.print("Kiralama tipi seçin (hourly/daily/weekly/monthly): ");
        String durationType = scanner.nextLine().toLowerCase();

        System.out.print("Kaç " + durationType + " kiralayacaksınız? ");
        int sure = Integer.parseInt(scanner.nextLine());

        if (user.isCorporate() && (durationType.equals("daily") && sure < 30)) {
            System.out.println(" Kurumsal kullanıcılar en az 1 aylık kiralama yapabilir.");
            return;
        }

        if (selectedVehicle.getPrice() > 2_000_000) {
            if (userAge < 30) {
                System.out.println(" Bu araç sadece 30 yaşından büyük kullanıcılar için kiralanabilir.");
                return;
            } else {
                double deposit = selectedVehicle.getPrice() * 0.10;
                System.out.println(" Bu araç için %10 depozito ödenmesi gereklidir: " + deposit + " TL");
            }
        }

        //  Tarihleri belirle
        Date start = Date.valueOf(LocalDate.now());
        Date end = switch (durationType) {
            case "daily" -> Date.valueOf(LocalDate.now().plusDays(sure));
            case "weekly" -> Date.valueOf(LocalDate.now().plusWeeks(sure));
            case "monthly" -> Date.valueOf(LocalDate.now().plusMonths(sure));
            default -> start; // hourly için sadece başlangıç yeterli
        };

        //  Fiyat hesaplama
        double basePrice = selectedVehicle.getRentalPrice();
        double totalPrice = switch (durationType) {
            case "hourly" -> basePrice * sure;
            case "daily" -> basePrice * sure;
            case "weekly" -> basePrice * sure * 5.5;
            case "monthly" -> basePrice * sure * 22;
            default -> basePrice;
        };

        //  Kiralama nesnesi
        Rental rental = new Rental(0, user.getId(), vehicleId, start, end, durationType, totalPrice);
        rentalDAO.rentVehicle(rental);
        vehicleDAO.markAsUnavailable(vehicleId);

        //  Fatura yazdır
        System.out.println("\n Kiralama tamamlandı!");
        System.out.println(" Fatura:");
        System.out.println("Araç: " + selectedVehicle.getBrand() + " " + selectedVehicle.getModel());
        System.out.println("Süre: " + sure + " " + durationType);
        System.out.println("Toplam: " + totalPrice + " TL");
    }

    public static void preloadVehicles(Connection conn) throws Exception {
        VehicleDAO vehicleDAO = new VehicleDAO(conn);

        vehicleDAO.addVehicle(new Vehicle(0, "car", "Toyota", "Corolla", 2021, 900000, 1200, true));
        vehicleDAO.addVehicle(new Vehicle(0, "car", "BMW", "320i", 2022, 2100000, 2200, true)); // 2M+ kısıt testi için
        vehicleDAO.addVehicle(new Vehicle(0, "motorcycle", "Yamaha", "MT-07", 2020, 600000, 800, true));
        vehicleDAO.addVehicle(new Vehicle(0, "motorcycle", "Kawasaki", "Ninja 650", 2023, 750000, 900, true));
        vehicleDAO.addVehicle(new Vehicle(0, "helicopter", "Airbus", "H125", 2019, 30000000, 15000, true)); // helikopter
        vehicleDAO.addVehicle(new Vehicle(0, "car", "Mercedes", "E200", 2023, 2500000, 2600, true));

        System.out.println("🚗 Örnek araçlar başarıyla eklendi.");
    }

    public static void returnVehicle(User user, Connection conn) throws Exception {
        RentalDAO rentalDAO = new RentalDAO(conn);
        VehicleDAO vehicleDAO = new VehicleDAO(conn);

        List<Rental> activeRentals = rentalDAO.getActiveRentalsByUserId(user.getId());

        if (activeRentals.isEmpty()) {
            System.out.println("📭 İade edilecek aktif kiralamanız bulunmamaktadır.");
            return;
        }

        System.out.println("📦 Aktif Kiralamalar:");
        for (Rental r : activeRentals) {
            System.out.println("Kiralama ID: " + r.getId() + ", Araç ID: " + r.getVehicleId() +
                    ", Bitiş: " + r.getEndDate() + ", Tutar: " + r.getTotalPrice());
        }

        System.out.print("İade etmek istediğiniz kiralamanın ID’sini girin: ");
        int rentalId = Integer.parseInt(scanner.nextLine());

        Rental selected = activeRentals.stream()
                .filter(r -> r.getId() == rentalId)
                .findFirst()
                .orElse(null);

        if (selected == null) {
            System.out.println("❌ Geçersiz kiralama ID.");
            return;
        }

        rentalDAO.returnRental(rentalId);
        vehicleDAO.markAsAvailable(selected.getVehicleId());

        System.out.println("✅ Araç başarıyla iade edildi.");
    }



}





