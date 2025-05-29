# 💳 Aplikasi Perbankan Sederhana

Aplikasi ini dirancang untuk mengelola sistem perbankan sederhana menggunakan **bahasa pemrograman Java** dan menerapkan konsep **Object-Oriented Programming (OOP)** seperti **inheritance**, **encapsulation**, dan **polymorphism**. Kelas-kelas utama dalam aplikasi ini meliputi:

- `User`
- `Customer`
- `BankAccount`
- `SavingsAccount`
- `Transaction`

Aplikasi ini juga **terkoneksi dengan database MySQL** sehingga semua data pelanggan, akun, dan transaksi dapat disimpan dan dikelola secara aman dan terstruktur.

---

## ⚙️ Fitur Utama

- Registrasi dan autentikasi pengguna
- Pembukaan akun tabungan
- Penarikan dan penyetoran saldo
- Riwayat transaksi tersimpan di database
- Validasi saldo sebelum transaksi

---

## 📦 Dependensi

Aplikasi Java ini menggunakan **Maven** untuk mengelola dependensi. Pastikan Anda memiliki Maven terinstal di sistem Anda.

### Contoh konfigurasi Maven untuk MySQL (`pom.xml`):

```xml
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
</dependencies>
