package uas.oop.database;

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
}
