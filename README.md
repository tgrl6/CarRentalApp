#  Araç Kiralama Sistemi (Java + PostgreSQL)

Bu proje, terminal tabanlı bir araç kiralama sistemidir. Kullanıcılar sisteme kayıt olabilir, araçları listeleyebilir, kiralayabilir ve iade edebilir. Admin kullanıcılar araç tanımlayabilir. PostgreSQL veritabanı ile çalışmaktadır.

---

##  Özellikler

- Kullanıcı kayıt ve giriş sistemi (email + şifre)
- BCrypt ile şifre güvenliği
- Admin paneli: araç ekleme ve listeleme
- Kullanıcı paneli: araç listeleme, filtreleme, kiralama, geçmiş görüntüleme, iade etme
- Kiralama süresi: saatlik, günlük, haftalık, aylık
- Yaş, kurumsal durum ve fiyat bazlı kısıtlamalar
- Katmanlı mimari (DAO, Model, Main, Util)

---

## Kullanılan Teknolojiler

- Java 21
- PostgreSQL
- JDBC
- Maven
- BCrypt (Spring Security Crypto)

---

## Proje Yapısı
src/
main/
java/com/patika/
Main.java
dao/ # Veritabanı işlemleri (UserDAO, VehicleDAO, RentalDAO)
 model/ # Veri modelleri (User, Vehicle, Rental)
util/ # DB bağlantı sınıfı (DButil.java)

