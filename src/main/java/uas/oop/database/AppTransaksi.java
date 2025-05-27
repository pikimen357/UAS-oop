package uas.oop.database;

import java.sql.Connection;
import java.sql.SQLException;

public class AppTransaksi {
    public static void main(String[] args) {

        String username = "siti_amalia";
        String passwordInput = "siti123"; // input dari user
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

            SavingsAccount account = SavingsAccount.loadFromDB(accountNumber, connection);

            Transaction t1 = new Transaction(accountNumber, "deposit", 500_000, "Setoran tunai");
            Transaction t2 = new Transaction(accountNumber, "withdrawal", 200_000, "Tarik tunai");

            t1.process(account, connection);
            t2.process(account, connection);

            connection.commit();
            System.out.println("Semua transaksi berhasil diproses.");

            account.showBalance();
        } catch (SQLException e) {
            System.out.println("Kesalahan transaksi: " + e.getMessage());
        }

    }
}
