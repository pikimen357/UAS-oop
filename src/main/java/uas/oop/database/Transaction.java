package uas.oop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Transaction {
    private long accountNumber;
    private String type; // deposit / withdrawal
    private double amount;
    private String description;

    public Transaction(long accountNumber, String type, double amount, String description) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    public void insertToTransaction(Connection connection) throws SQLException {
        String sql = """
            INSERT INTO transactions (account_number, type, amount, description)
            VALUES (?, ?, ?, ?);
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, accountNumber);
            preparedStatement.setString(2, type);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, description);
            preparedStatement.executeUpdate();
        }
    }

    public void printDetails() {
        System.out.printf("[%s] Rp %.2f - %s\n", type.toUpperCase(), amount, description);
    }

    public boolean process(SavingsAccount account, Connection conn) throws SQLException {
        if (type.equalsIgnoreCase("deposit")) {
            account.deposit(amount);
        } else if (type.equalsIgnoreCase("withdrawal")) {
            if (amount > account.getBalance()) {
                return false; // saldo tidak cukup
            }
            account.withdraw(amount);
        } else {
            throw new IllegalArgumentException("Tipe transaksi tidak valid.");
        }

        account.updateBalanceInDB(conn);
        insertToTransaction(conn);
        return true;
    }
}

