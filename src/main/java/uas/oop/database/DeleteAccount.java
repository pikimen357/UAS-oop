package uas.oop.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteAccount {
    public static void main(String[] args) {
        // Input username
        Scanner myObj = new Scanner(System.in);
        System.out.print("Enter username : ");
        String username = myObj.nextLine();

        // Input password
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter password : ");
        String passwordInput = scan.nextLine();

        String passwordHashDB = null;
        long accountNumber;
        int idCustomer = -1;

        try (Connection conn = ConnectionUtil.getDataSource().getConnection()) {
            // Validasi login terlebih dahulu
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
                accountNumber = rs.getLong("account_number");

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

            // Konfirmasi delete account
            System.out.print("\nApakah Anda yakin ingin menghapus account ini? (y/n): ");
            String confirmation = myObj.nextLine();

            if (!confirmation.equalsIgnoreCase("y")) {
                System.out.println("Penghapusan account dibatalkan.");
                return;
            }

            // Mulai proses delete dengan transaction
            conn.setAutoCommit(false);

            try {
                // 1. Hapus dari tabel accounts terlebih dahulu
                var deleteAccountStmt = conn.prepareStatement("""
                        DELETE FROM accounts 
                        WHERE customer_id = ?
                    """);
                deleteAccountStmt.setInt(1, idCustomer);
                int accountsDeleted = deleteAccountStmt.executeUpdate();

                // 2. Hapus dari tabel customers
                var deleteCustomerStmt = conn.prepareStatement("""
                        DELETE FROM customers 
                        WHERE id = ?
                    """);
                deleteCustomerStmt.setInt(1, idCustomer);
                int customersDeleted = deleteCustomerStmt.executeUpdate();

                // 3. Hapus dari tabel users (opsional, tergantung struktur database)
                var deleteUserStmt = conn.prepareStatement("""
                        DELETE FROM users 
                        WHERE username = ?
                    """);
                deleteUserStmt.setString(1, username);
                int usersDeleted = deleteUserStmt.executeUpdate();

                // Commit transaction jika semua berhasil
                conn.commit();

                System.out.println("\n=== ACCOUNT BERHASIL DIHAPUS ===");
                System.out.println("Accounts deleted: " + accountsDeleted);
                System.out.println("Customers deleted: " + customersDeleted);
                System.out.println("Users deleted: " + usersDeleted);
                System.out.println("Account dengan username '" + username + "' telah dihapus dari sistem.");

                deleteAccountStmt.close();
                deleteCustomerStmt.close();
                deleteUserStmt.close();

            } catch (SQLException e) {
                // Rollback jika terjadi error
                conn.rollback();
                System.out.println("Gagal menghapus account: " + e.getMessage());
                System.out.println("Semua perubahan dibatalkan.");
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Gagal mengambil data: " + e.getMessage());
        } finally {
            myObj.close();
            scan.close();
        }
    }
}