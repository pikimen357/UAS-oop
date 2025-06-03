// AppTransaksi.java
package uas.oop.database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class AppTransaksi extends JFrame {

    private JPanel transactionPanel;
    private JPanel headerPanel;
    private JLabel welcomeLabel;
    private JLabel accountNumberLabel;
    private JLabel currentBalanceLabel;
    private JTextField amountField;
    private JButton depositButton;
    private JButton withdrawalButton;
    private JLabel transactionMessageLabel;

    private long loggedInAccountNumber;
    private String loggedInUsername;
    private int loggedInCustomerId;

    public AppTransaksi(long accountNumber, String username, int customerId) {
        this.loggedInAccountNumber = accountNumber;
        this.loggedInUsername = username;
        this.loggedInCustomerId = customerId;

        setTitle("Dashboard Mobile Banking");
        setSize(400, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 🔵 Panel utama (pakai BorderLayout supaya header di atas)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(180, 200, 245));

        // 🔵 Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(65, 105, 225));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50));

        JLabel headerLabel = new JLabel("  Bank Plecit"); // spasi di awal untuk margin kiri
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

        headerPanel.add(headerLabel, BorderLayout.WEST);

        // 🔵 Background panel untuk isi transaksi
        JPanel background = new JPanel(new GridBagLayout());
        background.setOpaque(false); // biar header kelihatan
        background.setBackground(new Color(180, 200, 245));

        // Container panel (card) dengan RoundedPanel
        RoundedPanel card = new RoundedPanel(20);
        card.setPreferredSize(new Dimension(320, 400));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        // Buat panel transaksi (isinya form & tombol)
        createTransactionPanel();

        // Tambahkan transactionPanel ke card (dengan padding & layout card)
        card.add(transactionPanel);

        // Tambahkan card ke background
        background.add(card);

        // 🔵 Tambahkan header dan background panel ke mainPanel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(background, BorderLayout.CENTER);

        add(mainPanel);

        // Tampilkan saldo awal
        updateBalanceDisplay();
    }


    public void header(){
        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(180, 200, 245));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        welcomeLabel = new JLabel("Selamat datang, " + loggedInUsername + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(welcomeLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void createTransactionPanel() {
        transactionPanel = new JPanel();
        transactionPanel.setLayout(new BoxLayout(transactionPanel, BoxLayout.Y_AXIS));
        transactionPanel.setOpaque(false); // Supaya transparan di dalam RoundedPanel

        // Welcome Label
        welcomeLabel = new JLabel("Selamat datang, " + loggedInUsername + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        transactionPanel.add(welcomeLabel);
        transactionPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Account Number
        accountNumberLabel = new JLabel("Nomor Rekening: " + loggedInAccountNumber);
        accountNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        transactionPanel.add(accountNumberLabel);
        transactionPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Current Balance
        currentBalanceLabel = new JLabel("Saldo Saat Ini: Rp 0");
        currentBalanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        currentBalanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        transactionPanel.add(currentBalanceLabel);
        transactionPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Amount Input
        JPanel amountPanel = new JPanel(new BorderLayout());
        amountPanel.setOpaque(false);
        JLabel amountLabel = new JLabel("Jumlah: ");
        amountField = new JTextField();
        amountPanel.add(amountLabel, BorderLayout.WEST);
        amountPanel.add(amountField, BorderLayout.CENTER);
        transactionPanel.add(amountPanel);
        transactionPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);
        depositButton = new JButton("Setor Tunai");
        withdrawalButton = new JButton("Tarik Tunai");
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawalButton);
        transactionPanel.add(buttonPanel);
        transactionPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Transaction Message
        transactionMessageLabel = new JLabel("");
        transactionMessageLabel.setForeground(Color.BLUE);
        transactionMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        transactionPanel.add(transactionMessageLabel);

        // Button Action Listeners
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performTransaction("deposit");
            }
        });

        withdrawalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performTransaction("withdrawal");
            }
        });
    }

    private void updateBalanceDisplay() {
        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
            SavingsAccount account = SavingsAccount.loadFromDB(loggedInAccountNumber, connection);
            if (account != null) {
                currentBalanceLabel.setText(String.format("Saldo Saat Ini: Rp %,.2f", account.getBalance()));
            } else {
                currentBalanceLabel.setText("Saldo Saat Ini: N/A");
            }
        } catch (SQLException e) {
            transactionMessageLabel.setText("Gagal memuat saldo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean performTransaction(String type) {
        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                transactionMessageLabel.setText("Jumlah harus lebih besar dari nol.");
                return false;
            }
        } catch (NumberFormatException ex) {
            transactionMessageLabel.setText("Masukkan jumlah yang valid.");
            return false;
        }

        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
            connection.setAutoCommit(false);
            SavingsAccount account = SavingsAccount.loadFromDB(loggedInAccountNumber, connection);

            if (account == null) {
                transactionMessageLabel.setText("Akun tidak ditemukan.");
                connection.rollback();
                return false;
            }

            Transaction t1 = new Transaction(loggedInAccountNumber, type, amount, type.equals("deposit") ? "Setoran tunai" : "Tarik tunai");
            boolean success = t1.process(account, connection);

            if (success) {
                connection.commit();
                transactionMessageLabel.setText(String.format("%s sebesar Rp %,.2f berhasil.",
                        type.equals("deposit") ? "Deposit" : "Tarik tunai", amount));
                updateBalanceDisplay();
                amountField.setText("");
                return true;
            } else {
                connection.rollback();
                transactionMessageLabel.setText(String.format("%s gagal. Saldo tidak mencukupi atau kesalahan lain.",
                        type.equals("deposit") ? "Deposit" : "Tarik tunai"));
                return false;
            }

        } catch (SQLException e) {
            transactionMessageLabel.setText("Kesalahan transaksi: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppTransaksi(1234567890L, "user", 1).setVisible(true));
    }
}
