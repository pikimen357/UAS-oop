package uas.oop.database;

import java.sql.Connection;
import java.sql.SQLException;
public class AppRegister {
    public static void main(String[] args) {
        // Nonaktifkan warning logger
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        Customer customer = new Customer(
                "john_doe", "john123", "customer", "john.doe@outlook.com",
                "John Doe", "1111222233334444", "087811112222", "Jl. Sudirman No. 123"
        );

        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
            connection.setAutoCommit(false); // Mulai transaksi

            customer.insertToUser(connection);
            customer.insertToCustomer(connection);

            long accountNumber = 150000431L;
            SavingsAccount account = new SavingsAccount(accountNumber, 50_000, customer.getIdUser());
            account.insertToAccounts(connection);

            connection.commit();
            System.out.println("Registrasi berhasil!");
            System.out.println("Customer ID: " + customer.getIdUser());

        } catch (SQLException e) {
            System.out.println("Gagal registrasi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

