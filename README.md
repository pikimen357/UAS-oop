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
```
Dependensi diatas cukup lengkap yang terdapat pada  (`pom.xml`)

### 📊 Struktur Tabel Database

### Tabel `users`

| Kolom           | Tipe Data                  | Keterangan                               |
| --------------- | -------------------------- | ---------------------------------------- |
| `id`            | `INT`                      | Primary Key, Auto Increment              |
| `username`      | `VARCHAR(50)`              | Tidak boleh kosong, unik                 |
| `password_hash` | `VARCHAR(255)`             | Tidak boleh kosong                       |
| `role`          | `ENUM('admin','customer')` | Tidak boleh kosong, default `'customer'` |
| `created_at`    | `TIMESTAMP`                | Default current timestamp                |
| `email`         | `VARCHAR(250)`             | Tidak boleh kosong, unik                 |


### Tabel `customers`

| Kolom       | Tipe Data      | Keterangan                                         |
| ----------- | -------------- | -------------------------------------------------- |
| `id`        | `INT`          | Primary Key, Auto Increment                        |
| `user_id`   | `INT`          | Tidak boleh kosong, Foreign Key ke `users(id)` |
| `full_name` | `VARCHAR(100)` | Tidak boleh kosong                                 |
| `nik`       | `VARCHAR(16)`  | Tidak boleh kosong, unik                           |
| `phone`     | `VARCHAR(20)`  | Boleh kosong                                       |
| `address`   | `TEXT`         | Boleh kosong                                       |




### Tabel `accounts`

| Kolom       | Tipe Data      | Keterangan                                         |
| ----------- | -------------- | -------------------------------------------------- |
| `account_number`        | `bigint`          | Primary Key, Auto Increment                        |
| `customer_id `   | `int`          | Tidak boleh kosong, Foreign Key ke `customer(id)` |
| `balance` | `decimal(18,2)` | Tidak boleh kosong                                 |
| `created_at`    | `TIMESTAMP`                | Default current timestamp     |

### Tabel `transactions`

| Kolom            | Tipe Data                      | Keterangan                               |
| ---------------- | ------------------------------ | ---------------------------------------- |
| `id`             | `INT`                          | Primary Key, Auto Increment              |
| `account_number` | `BIGINT`                       | Foreign Key → `accounts(account_number)` |
| `type`           | `ENUM('deposit','withdrawal')` | Not null                                 |
| `amount`         | `DECIMAL(18,2)`                | Not null                                 |
| `description`    | `VARCHAR(255)`                 | Optional                                 |
| `created_at`     | `TIMESTAMP`                    | Waktu transaksi (default: `NOW()`)       |



### 🛠️ Cara Menjalankan

Ikuti langkah-langkah berikut untuk menjalankan aplikasi ini di lokal Anda:

1. **Clone repositori**
   ```bash
   git clone https://github.com/pikimen357/UAS-oop
2. **Masuk ke direktori proyek**
   ```bash
   cd UAS-oop

3. **Jalankan program beserta databasenya**


