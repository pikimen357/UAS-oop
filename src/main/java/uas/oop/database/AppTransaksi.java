package uas.oop.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class AppTransaksi {
    public static void main(String[] args) {

//        Input username
        Scanner myObj = new Scanner(System.in);
        System.out.print("Enter username : ");
        String username = myObj.nextLine();

//        Input password
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter password : ");
        String passwordInput = scan.nextLine();

//        String username = "siti_amalia";
//        String passwordInput = "siti123"; // input dari user
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
                    System.out.println("Account Number : " + accountNumber);
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

            Scanner scanner = new Scanner(System.in);
            System.out.print("Type ('deposit' (1) or 'withdrawal' (2)) : ");
            String type = scanner.nextLine().toLowerCase();

            if (type.equals("deposit") || type.equals("1")) {
                SavingsAccount account = SavingsAccount.loadFromDB(accountNumber, connection);
                account.showBalance();

                Scanner scn = new Scanner(System.in);
                System.out.print("Amount : ");
                double amount = scn.nextDouble();

                Transaction t1 = new Transaction(accountNumber, "deposit", amount, "Setoran tunai");
                t1.process(account, connection);

                connection.commit();
                System.out.println("Deposit sebesar " + amount + " berhasil.");

                account.showBalance();
            } else if (type.equals("withdrawal") || type.equals("2")) {
                SavingsAccount account = SavingsAccount.loadFromDB(accountNumber, connection);
                account.showBalance();

                Scanner scn = new Scanner(System.in);
                System.out.print("Amount : ");
                double amount = scn.nextDouble();

                Transaction t1 = new Transaction(accountNumber, "withdrawal", amount, "Tunai");
                t1.process(account, connection);
                connection.commit();
                System.out.println("Transaksi tarik tunai sebesar : " + amount + " berhasil.");

                account.showBalance();
            } else {
                System.out.println("jenis transaksi tidak ditemukan.");
                connection.rollback();
            }

//            SavingsAccount account = SavingsAccount.loadFromDB(accountNumber, connection);
//
//            Transaction t1 = new Transaction(accountNumber, "deposit", 500_000, "Setoran tunai");
//
//            t1.process(account, connection);
//
//            connection.commit();
//            System.out.println("Semua transaksi berhasil diproses.");


        } catch (SQLException e) {
            System.out.println("Kesalahan transaksi: " + e.getMessage());
        }

    }
}
