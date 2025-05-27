package uas.oop.database;

import java.sql.Connection;
import java.sql.SQLException;

public class AppTransaksi {
    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        String username = "john_doe";
        String passwordInput = "john123"; // input dari user
        String passwordHashDB = null;
        int idCustomer = -1;

        try (Connection conn = ConnectionUtil.getDataSource().getConnection()) {
            var stmt = conn.prepareStatement("""
                    SELECT customers.id, users.username, users.password_hash
                    FROM customers
                    JOIN users ON customers.user_id = users.id
                    WHERE users.username = ?
                """);
            stmt.setString(1, username);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                idCustomer = rs.getInt("id");
                passwordHashDB = rs.getString("password_hash");

                // Validasi password
                if (passwordInput.equals(passwordHashDB)) {
                    System.out.println("Login berhasil. ");
                    System.out.println("Username : " + username);
                } else {
                    System.out.println("Password salah.");
                    return;
                }
            } else {
                System.out.println("Username tidak ditemukan.");
                return;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Gagal mengambil data: " + e.getMessage());
            return;
        }


//        long accountNumber = 470000021L;

//        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
//            connection.setAutoCommit(false);
//
//            SavingsAccount account = new SavingsAccount(accountNumber, 500_000, idCustomer);
//            account.insertToAccounts(connection);
//
//            connection.commit();
//            System.out.println("Akun berhasil dibuat untuk Customer ID: " + idCustomer);
//
//            account.showBalance();
//
//            account.deposit(250_000);
//            account.withdraw(100_000);
//            account.showBalance();
//
//            Transaction t1 = new Transaction(account.getAccountNumber(), "deposit", 250_000, "Setoran tunai");
//            Transaction t2 = new Transaction(account.getAccountNumber(), "withdrawal", 100_000, "Tarik tunai");
//
//            t1.printDetails();
//            t2.printDetails();
//
//        } catch (SQLException e) {
//            System.out.println("Kesalahan saat transaksi: " + e.getMessage());
//            e.printStackTrace();
//        }
    }
}
