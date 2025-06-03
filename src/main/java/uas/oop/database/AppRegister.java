package uas.oop.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.security.SecureRandom;

public class AppRegister {
    public static void main(String[] args) {
        // Nonaktifkan warning logger
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        Scanner scanner = new Scanner(System.in);

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine(); // kamu bisa hash ini jika perlu

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();

        System.out.print("NIK: ");
        String nik = scanner.nextLine();

        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        // Buat objek Customer
        Customer customer = new Customer(
                username, password, "customer", email,
                fullName, nik, phone, address
        );

        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
            connection.setAutoCommit(false); // Mulai transaksi

            customer.insertToUser(connection);
            customer.insertToCustomer(connection);

            // Generate account number acak
            SecureRandom random = new SecureRandom();
            long accountNumber = 100_000_000_000L + (Math.abs(random.nextLong()) % 900_000_000_000L);

            // Buat akun

            System.out.println("=================\nAccount Number: " + accountNumber);

            Scanner sc = new Scanner(System.in);
            System.out.print("Saldo awal : ");
            double saldo = sc.nextDouble();

            SavingsAccount account = new SavingsAccount(accountNumber, saldo, customer.getIdUser());
            account.insertToAccounts(connection);

            connection.commit();
            System.out.println("======================\nRegistrasi berhasil!");
            System.out.println("Customer ID: " + customer.getIdUser());
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Balance : " + account.getBalance());

        } catch (SQLException e) {
            System.out.println("Gagal registrasi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

