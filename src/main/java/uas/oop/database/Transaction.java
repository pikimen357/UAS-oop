package uas.oop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private long accountNumber;
    private String type; // deposit / withdrawal
    private double amount;
    private String description;
    private LocalDateTime createdAt;

    // Constructor for creating new transaction
    public Transaction(long accountNumber, String type, double amount, String description) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor for loading transaction from database
    public Transaction(long accountNumber, String type, double amount, String description, LocalDateTime createdAt) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Constructor for account number only
    public Transaction(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    // Insert transaction record to database
    public void insertToTransaction(Connection connection) throws SQLException {
        String sql = """
            INSERT INTO transactions (account_number, type, amount, description, created_at)
            VALUES (?, ?, ?, ?, NOW());
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, accountNumber);
            preparedStatement.setString(2, type);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, description);
            preparedStatement.executeUpdate();
        }
    }

    // Get all transactions for an account
    public static List<Transaction> getAllTransactions(long accountNumber, Connection conn) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = """
            SELECT t.created_at, t.type, t.amount, t.description
            FROM transactions t
            WHERE t.account_number = ?
            ORDER BY t.created_at DESC;
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");
                double amount = rs.getDouble("amount");
                String description = rs.getString("description");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                transactions.add(new Transaction(accountNumber, type, amount, description, createdAt));
            }
        }

        return transactions;
    }

    // Get transactions with limit
    public static List<Transaction> getRecentTransactions(long accountNumber, Connection conn, int limit) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = """
            SELECT t.created_at, t.type, t.amount, t.description
            FROM transactions t
            WHERE t.account_number = ?
            ORDER BY t.created_at DESC
            LIMIT ?;
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, accountNumber);
            stmt.setInt(2, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");
                double amount = rs.getDouble("amount");
                String description = rs.getString("description");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                transactions.add(new Transaction(accountNumber, type, amount, description, createdAt));
            }
        }

        return transactions;
    }

    // Print transaction details
    public void printDetails() {
        System.out.printf("[%s] %s - Rp %,.2f - %s\n",
                createdAt != null ? createdAt.toString() : "N/A",
                type.toUpperCase(),
                amount,
                description);
    }

    // Process the transaction (main business logic)
    public boolean process(SavingsAccount account, Connection conn) throws SQLException {
        // Validate transaction type
        if (!type.equalsIgnoreCase("deposit") && !type.equalsIgnoreCase("withdrawal")) {
            throw new IllegalArgumentException("Tipe transaksi tidak valid. Harus 'deposit' atau 'withdrawal'.");
        }

        // Validate amount
        if (amount <= 0) {
            throw new IllegalArgumentException("Jumlah transaksi harus lebih besar dari nol.");
        }

        // Process based on transaction type
        if (type.equalsIgnoreCase("deposit")) {
            account.deposit(amount);
        } else if (type.equalsIgnoreCase("withdrawal")) {
            // Check if sufficient balance
            if (amount > account.getBalance()) {
                return false; // saldo tidak cukup
            }
            account.withdraw(amount);
        }

        // Update account balance in database
        account.updateBalanceInDB(conn);

        // Insert transaction record
        insertToTransaction(conn);

        return true;
    }

    // Getters
    public long getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters (if needed)
    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return String.format("Transaction{accountNumber=%d, type='%s', amount=%.2f, description='%s', createdAt=%s}",
                accountNumber, type, amount, description, createdAt);
    }
}