package uas.oop.database;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SavingsAccount extends BankAccount {
    public SavingsAccount(long accountNumber, double balance, int customerId) {
        super(accountNumber, balance, customerId);
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposited: " + amount);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.println("Withdrew: " + amount);
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    public static SavingsAccount loadFromDB(long accountNumber, Connection conn) throws SQLException {
        String sql = "SELECT account_number, balance, customer_id FROM accounts WHERE account_number = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long accNum = rs.getLong("account_number");
                double balance = rs.getDouble("balance");
                int customerId = rs.getInt("customer_id");
                return new SavingsAccount(accNum, balance, customerId);
            } else {
                throw new SQLException("Akun tidak ditemukan.");
            }
        }
    }



    public void insertToAccounts(Connection connection) throws SQLException {
        String sql = """
            INSERT INTO accounts (account_number, customer_id, balance)
            VALUES (?, ?, ?);
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, getAccountNumber());
            preparedStatement.setInt(2, getCustomerId());
            preparedStatement.setDouble(3, getBalance());

            int updateCount = preparedStatement.executeUpdate();
            if (updateCount > 0) {
                System.out.println("Insert Accounts successful");
            } else {
                System.out.println("Insert Accounts failed");
                throw new SQLException("Insert accounts gagal");
            }

        }
    }

    public void updateBalanceInDB(Connection conn) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, balance);
            stmt.setLong(2, accountNumber);
            stmt.executeUpdate();
        }
    }

}
