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

    // Get latest transaction for an account
    public static Transaction getLatestTransaction(long accountNumber, Connection conn) throws SQLException {
        String sql = """
            SELECT t.created_at, t.type, t.amount, t.description
            FROM transactions t
            WHERE t.account_number = ?
            ORDER BY t.created_at DESC
            LIMIT 1;
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String type = rs.getString("type");
                double amount = rs.getDouble("amount");
                String description = rs.getString("description");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                return new Transaction(accountNumber, type, amount, description, createdAt);
            } else {
                throw new SQLException("Tidak ada transaksi ditemukan untuk rekening: " + accountNumber);
            }
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


// ===== 4. PENGGUNAAN DI RiwayatTransaksi.java - HALAMAN KHUSUS HISTORY =====
//public class RiwayatTransaksi extends JFrame {
//    private JTable transactionTable;
//    private DefaultTableModel tableModel;
//    private long loggedInAccountNumber;
//
//    private void loadAllTransactions() {
//        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
//
//            // MENDAPATKAN SEMUA TRANSAKSI
//            List<Transaction> allTransactions = Transaction.getAllTransactions(loggedInAccountNumber, connection);
//
//            // Clear table
//            tableModel.setRowCount(0);
//
//            // Populate table
//            for (Transaction transaction : allTransactions) {
//                Object[] row = {
//                        transaction.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
//                        transaction.getCreatedAt().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
//                        transaction.getType().equals("deposit") ? "KREDIT" : "DEBIT",
//                        transaction.getType().equals("deposit") ?
//                                String.format("+ Rp %,.2f", transaction.getAmount()) :
//                                String.format("- Rp %,.2f", transaction.getAmount()),
//                        transaction.getDescription()
//                };
//                tableModel.addRow(row);
//            }
//
//            // Update status
//            statusLabel.setText("Total: " + allTransactions.size() + " transaksi");
//
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Gagal memuat riwayat: " + e.getMessage(),
//                    "Error", JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
//    }
//
//    // Filter transaksi berdasarkan tanggal
//    private void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
//        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
//
//            List<Transaction> allTransactions = Transaction.getAllTransactions(loggedInAccountNumber, connection);
//
//            // Filter berdasarkan tanggal
//            List<Transaction> filteredTransactions = allTransactions.stream()
//                    .filter(t -> {
//                        LocalDate transactionDate = t.getCreatedAt().toLocalDate();
//                        return !transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate);
//                    })
//                    .collect(Collectors.toList());
//
//            // Update table dengan data yang sudah difilter
//            tableModel.setRowCount(0);
//            for (Transaction transaction : filteredTransactions) {
//                Object[] row = {
//                        transaction.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
//                        transaction.getCreatedAt().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
//                        transaction.getType().equals("deposit") ? "KREDIT" : "DEBIT",
//                        transaction.getType().equals("deposit") ?
//                                String.format("+ Rp %,.2f", transaction.getAmount()) :
//                                String.format("- Rp %,.2f", transaction.getAmount()),
//                        transaction.getDescription()
//                };
//                tableModel.addRow(row);
//            }
//
//            statusLabel.setText("Ditemukan: " + filteredTransactions.size() + " transaksi");
//
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Gagal memfilter transaksi: " + e.getMessage(),
//                    "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//}