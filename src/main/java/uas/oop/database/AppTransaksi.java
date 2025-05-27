package uas.oop.database;

import java.sql.Connection;
import java.sql.SQLException;

public class AppTransaksi {
    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        String username = "erico";
        String passwordInput = "erico356"; // input dari user
        String passwordHashDB = null;
        int accountNumber;
        int idCustomer = -1;

        try (Connection conn = ConnectionUtil.getDataSource().getConnection()) {
            var stmt = conn.prepareStatement("""
                    SELECT customers.id, users.username, users.password_hash, a.account_number
                    FROM customers
                    JOIN users ON customers.user_id = users.id
                    JOIN uasoop.accounts a on customers.id = a.customer_id
                    WHERE users.username = ?;
                """);
            stmt.setString(1, username);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                idCustomer = rs.getInt("id");
                passwordHashDB = rs.getString("password_hash");
                accountNumber = rs.getInt("account_number");

                // Validasi password
                if (passwordInput.equals(passwordHashDB)) {
                    System.out.println("Login berhasil. ");
                    System.out.println("Username : " + username);
//                    System.out.println("Account Number : " + accountNumber);
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


        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
            connection.setAutoCommit(false);

            Transaction t1 = new Transaction(accountNumber, "deposit", 250_000, "Setoran tunai");
            Transaction t2 = new Transaction(accountNumber, "withdrawal", 100_000, "Tarik tunai");

            t1.insertToTransaction(connection);
            t2.insertToTransaction(connection);

//            t1.printDetails();
//            t2.printDetails();

            connection.commit();

//            account.showBalance();
//
//            account.deposit(250_000);
//            account.withdraw(100_000);
//            account.showBalance();
        } catch (SQLException e) {
            System.out.println("Kesalahan saat transaksi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
