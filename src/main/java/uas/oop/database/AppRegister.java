package uas.oop.database;

import java.sql.Connection;
import java.sql.SQLException;
public class AppRegister {
    public static void main(String[] args) {
        // Nonaktifkan warning logger
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        Customer customer = new Customer(
                "erico", "erico356", "customer", "erico@gmail.com",
                "Erico China", "823428342938", "087435242", "Jl. Ciu No. 123"
        );

        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
            connection.setAutoCommit(false); // Mulai transaksi

            customer.insertToUser(connection);
            customer.insertToCustomer(connection);

            long accountNumber = 460300481L;
            SavingsAccount account = new SavingsAccount(accountNumber, 5_000_000, customer.getIdUser());
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

