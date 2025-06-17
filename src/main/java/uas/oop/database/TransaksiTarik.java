// AppTransaksi.java
package uas.oop.database;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class TransaksiTarik extends JFrame {

    private JPanel transactionPanel;
    private JLabel welcomeLabel;
    private JLabel accountNumberLabel;
    private JLabel currentBalanceLabel;
    private JTextField amountField;
    private JButton withdrawButton;
    private JLabel transactionMessageLabel;

    private long loggedInAccountNumber;
    private String loggedInUsername;
    private int loggedInCustomerId;

    public TransaksiTarik(long accountNumber, String username, int customerId) {
        this.loggedInAccountNumber = accountNumber;
        this.loggedInUsername = username;
        this.loggedInCustomerId = customerId;

        setTitle("Transaksi Mobile Banking");
        setSize(450, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        buildGUI();
        updateBalanceDisplay();
    }

    private void buildGUI() {
        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 245, 255));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();

        // Content Panel
        JPanel contentPanel = createContentPanel();

        // Footer Panel
        JPanel footerPanel = createFooterPanel();

        // Gabungkan semua
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(65, 105, 225));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Logo/Title sebagai tombol
        JButton headerLabel = new JButton("Bank Plecit");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setContentAreaFilled(false);
        headerLabel.setBorderPainted(false);
        headerLabel.setFocusPainted(false);
        headerLabel.setOpaque(false);
        headerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        headerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 23));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
            }
        });

        headerLabel.addActionListener(e -> {
            dispose();
            new Dashboard(loggedInAccountNumber, loggedInUsername, loggedInCustomerId).setVisible(true);
        });

        // Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setOpaque(false);
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });

        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(240, 245, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        // Main transaction card
        JPanel transactionCard = createTransactionCard();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        contentPanel.add(transactionCard, gbc);

        return contentPanel;
    }

    private JPanel createTransactionCard() {
        RoundedPanel card = new RoundedPanel(25) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow effect
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(3, 3, getWidth(), getHeight(), 25, 25);

                // Card background
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };

        card.setPreferredSize(new Dimension(380, 550));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        createTransactionPanel(card);

        return card;
    }

    private void createTransactionPanel(JPanel parentCard) {
        // Title
        JLabel titleLabel = new JLabel("Transaksi Banking");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(65, 105, 225));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        parentCard.add(titleLabel);
        parentCard.add(Box.createRigidArea(new Dimension(0, 20)));

        // Welcome message
        welcomeLabel = new JLabel("Selamat datang, " + loggedInUsername + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeLabel.setForeground(new Color(60, 60, 60));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        parentCard.add(welcomeLabel);
        parentCard.add(Box.createRigidArea(new Dimension(0, 15)));

        // Account info panel
        JPanel accountInfoPanel = createAccountInfoPanel();
        parentCard.add(accountInfoPanel);
        parentCard.add(Box.createRigidArea(new Dimension(0, 25)));

        // Amount input panel
        JPanel amountPanel = createAmountInputPanel();
        parentCard.add(amountPanel);
        parentCard.add(Box.createRigidArea(new Dimension(0, 25)));

        // Buttons panel
        JPanel buttonPanel = createButtonPanel();
        parentCard.add(buttonPanel);
        parentCard.add(Box.createRigidArea(new Dimension(0, 25)));

        // Transaction message
        transactionMessageLabel = new JLabel(" ");
        transactionMessageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        transactionMessageLabel.setForeground(new Color(65, 105, 225));
        transactionMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        parentCard.add(transactionMessageLabel);
    }

    private JPanel createAccountInfoPanel() {
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(248, 250, 255));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 255), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        infoPanel.setOpaque(true);

        // Account number
        accountNumberLabel = new JLabel("Nomor Rekening: " + loggedInAccountNumber);
        accountNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        accountNumberLabel.setForeground(new Color(80, 80, 80));
        accountNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(accountNumberLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Current balance
        currentBalanceLabel = new JLabel("Saldo Saat Ini: Rp 0");
        currentBalanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        currentBalanceLabel.setForeground(new Color(65, 105, 225));
        currentBalanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(currentBalanceLabel);

        return infoPanel;
    }

    private JPanel createAmountInputPanel() {
        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.Y_AXIS));
        amountPanel.setOpaque(false);
        amountPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel amountLabel = new JLabel("Jumlah Transaksi:");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        amountLabel.setForeground(new Color(60, 60, 60));
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        amountField = new JTextField();
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        amountField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        amountField.setPreferredSize(new Dimension(250, 35));
        amountField.setMaximumSize(new Dimension(250, 35));
        amountField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tambahkan label dan field ke panel dengan spacing
        amountPanel.add(amountLabel);
        amountPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        amountPanel.add(amountField);

        return amountPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(250, 45));
        buttonPanel.setPreferredSize(new Dimension(250, 45));

        // Deposit button
        withdrawButton = createStyledButton("💰 Tarik Tunai", new Color(204, 146, 46));
        withdrawButton.addActionListener(e -> performTransaction("withdrawal"));

        buttonPanel.add(withdrawButton);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    public JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(65, 105, 225));
        footerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Panel untuk informasi footer
        JPanel footerInfoPanel = new JPanel(new GridLayout(3, 1, 0, 2));
        footerInfoPanel.setOpaque(false);

        // Baris pertama - Tagline
        JLabel taglineLabel = new JLabel("🏦 Bank Plecit - Rentenir Terpercaya");
        taglineLabel.setForeground(Color.WHITE);
        taglineLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        taglineLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Baris kedua - Keamanan
        JLabel securityLabel = new JLabel("🔒 Aman dan Terpercaya | 24 Jam Layanan");
        securityLabel.setForeground(new Color(220, 230, 255));
        securityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        securityLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Baris ketiga - Copyright
        JLabel copyrightLabel = new JLabel("© 2024 Bank Plecit. @copyright.");
        copyrightLabel.setForeground(new Color(200, 215, 255));
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);

        footerInfoPanel.add(taglineLabel);
        footerInfoPanel.add(securityLabel);
        footerInfoPanel.add(copyrightLabel);

        footerPanel.add(footerInfoPanel, BorderLayout.CENTER);

        return footerPanel;
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

    private void performTransaction(String type) {
        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                showMessage("Jumlah harus lebih besar dari nol.", new Color(231, 76, 60));
                return;
            }
        } catch (NumberFormatException ex) {
            showMessage("Masukkan jumlah yang valid.", new Color(231, 76, 60));
            return;
        }

        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
            connection.setAutoCommit(false);
            SavingsAccount account = SavingsAccount.loadFromDB(loggedInAccountNumber, connection);

            if (account == null) {
                showMessage("Akun tidak ditemukan.", new Color(231, 76, 60));
                connection.rollback();
                return;
            }

            Transaction t1 = new Transaction(loggedInAccountNumber, type, amount,
                    type.equals("deposit") ? "Setoran tunai" : "Tarik tunai");
            boolean success = t1.process(account, connection);

            if (success) {
                connection.commit();
                showMessage(String.format("✅ %s sebesar Rp %,.2f berhasil!",
                                type.equals("deposit") ? "Setoran" : "Penarikan", amount),
                        new Color(46, 204, 113));

                updateBalanceDisplay();
                amountField.setText("");

                // Kembali ke dashboard setelah 2 detik
                Timer timer = new Timer(2000, e -> {
                    dispose();
                    new Dashboard(loggedInAccountNumber, loggedInUsername, loggedInCustomerId).setVisible(true);
                });
                timer.setRepeats(false);
                timer.start();

            } else {
                connection.rollback();
                showMessage(String.format("❌ %s gagal. Saldo tidak mencukupi.",
                                type.equals("deposit") ? "Setoran" : "Penarikan"),
                        new Color(231, 76, 60));

                Timer timer = new Timer(2000, e -> {
                    dispose();
                    new Dashboard(loggedInAccountNumber, loggedInUsername, loggedInCustomerId).setVisible(true);
                });
                timer.setRepeats(false);
                timer.start();
            }

        } catch (SQLException e) {
            showMessage("Kesalahan transaksi: " + e.getMessage(), new Color(231, 76, 60));
            e.printStackTrace();
        }
    }

    private void showMessage(String message, Color color) {
        transactionMessageLabel.setText(message);
        transactionMessageLabel.setForeground(color);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TransaksiTarik(1234567890L, "user", 1).setVisible(true));
    }
}