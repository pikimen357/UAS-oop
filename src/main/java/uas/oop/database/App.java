package uas.oop.database;
import java.sql.SQLException;
import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        // Disable SLF4J warning
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        Customer customer = new Customer(
                "budi123", "hashed_pw", "customer", "budi@gmail.com",
                "Budi Santoso", "1234567890123456", "081234567890", "Jl. Mawar No. 5"
        );

        Connection connection = null;
        try {
            connection = ConnectionUtil.getDataSource().getConnection();
            connection.setAutoCommit(false); // Mulai transaksi

            customer.insertToUser(connection);  // insert ke tabel users
            customer.insertToCustomer(connection);  // insert ke tabel customers

            int idCustomer = customer.getIdUser();

            SavingsAccount account = new SavingsAccount(10000001L, 500_000, idCustomer);
            account.insertToAccounts(connection);   // insert ke tabel accounts

            connection.commit(); // Jika semua berhasil
            System.out.println("Semua transaksi berhasil!");

            customer.showInfo();
            account.showBalance();

            account.deposit(250_000);
            account.withdraw(100_000);
            account.showBalance();

            Transaction t1 = new Transaction(account.getAccountNumber(), "deposit", 250_000, "Setoran tunai");
            Transaction t2 = new Transaction(account.getAccountNumber(), "withdrawal", 100_000, "Tarik tunai");

            t1.printDetails();
            t2.printDetails();

        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());

            // Rollback menggunakan koneksi yang sama
            if (connection != null) {
                try {
                    connection.rollback(); // Gunakan koneksi yang sama
                    System.out.println("Transaksi dibatalkan.");
                } catch (SQLException rollbackEx) {
                    System.out.println("Error saat rollback: " + rollbackEx.getMessage());
                    rollbackEx.printStackTrace();
                }
            }
        } finally {
            // Pastikan koneksi selalu ditutup
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Koneksi ditutup.");
                } catch (SQLException closeEx) {
                    System.out.println("Error saat menutup koneksi: " + closeEx.getMessage());
                }
            }
        }
    }
}