package uas.oop.database;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Dashboard extends JFrame {

    // 🔵 Deklarasi variabel di dalam class, bukan di dalam constructor
    private long loggedInAccountNumber;
    private String loggedInUsername;
    private int loggedInCustomerId;
    private JLabel currentBalanceLabel;

    // 🔵 Constructor tambahan untuk inisialisasi data
    public Dashboard(long accountNumber, String username, int customerId) {
        this.loggedInAccountNumber = accountNumber;
        this.loggedInUsername = username;
        this.loggedInCustomerId = customerId;

        buildGUI();
    }

    public void buildGUI(){
        setTitle("Dashboard Mobile Banking");
        setSize(450, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(183, 208, 255));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();

        // Content Panel (tengah)
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

        JLabel headerLabel = new JLabel(" Bank Plecit");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JButton logoutButton = new JButton("logout/login ");
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
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(216, 228, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Info Panel
        JPanel infoPanel = createInfoPanel();
        JPanel wrapperInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapperInfoPanel.setOpaque(false);
        wrapperInfoPanel.add(infoPanel);

        // Menu Panel
        JPanel menuPanel = createMenuPanel();

        contentPanel.add(wrapperInfoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        contentPanel.add(menuPanel);

        return contentPanel;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // Tambah shadow effect
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(2, 2, getWidth(), getHeight(), 25, 25);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };

        infoPanel.setBackground(Color.WHITE);
        infoPanel.setPreferredSize(new Dimension(380, 160));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        // Icon
        ImageIcon icon = new ImageIcon("src/main/resources/assets/admin.png");
        Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(img));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nama
        JLabel infoLabel = new JLabel();
        infoLabel.setForeground(new Color(45, 45, 45));
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Saldo
        currentBalanceLabel = new JLabel();
        currentBalanceLabel.setForeground(new Color(65, 105, 225));
        currentBalanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        currentBalanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tambahkan komponen
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(iconLabel);
        infoPanel.add(Box.createVerticalStrut(12));
        infoPanel.add(infoLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(currentBalanceLabel);
        infoPanel.add(Box.createVerticalStrut(15));

        // Atur teks sesuai status login
        if (loggedInAccountNumber != 0) {
            infoLabel.setText(loggedInUsername);

            try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
                SavingsAccount account = SavingsAccount.loadFromDB(loggedInAccountNumber, connection);
                if (account != null) {
                    currentBalanceLabel.setText(String.format("Saldo: Rp %,.2f", account.getBalance()));
                } else {
                    currentBalanceLabel.setText("Saldo: N/A");
                }
            } catch (SQLException e) {
                currentBalanceLabel.setText("Gagal memuat saldo");
                e.printStackTrace();
            }
        } else {
            infoLabel.setText("Login Terlebih Dahulu!");
            currentBalanceLabel.setText("Silakan login untuk melihat saldo");
        }

        return infoPanel;
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new GridLayout(3, 2, 25, 25));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));

        menuPanel.add(createMenuCard("Akun", "src/main/resources/assets/admin.png", () -> {
            new Account(loggedInAccountNumber, loggedInUsername, loggedInCustomerId).setVisible(true);
        }));
        menuPanel.add(createMenuCard("Riwayat", "src/main/resources/assets/img.png", () -> {
            new TransactionHistory(loggedInAccountNumber, loggedInUsername, loggedInCustomerId).setVisible(true);
        }));
        menuPanel.add(createMenuCard("Tarik Tunai", "src/main/resources/assets/transfer.png", () -> {
            new TransaksiTarik(loggedInAccountNumber, loggedInUsername, loggedInCustomerId).setVisible(true);
        }));
        menuPanel.add(createMenuCard("Setor Tunai", "src/main/resources/assets/payment.png", () -> {
            new TransaksiSetor(loggedInAccountNumber, loggedInUsername, loggedInCustomerId).setVisible(true);
        }));
        menuPanel.add(createMenuCard("Transfer", "src/main/resources/assets/trnsfr.png", null));
        menuPanel.add(createMenuCard("Pulsa & Data", "src/main/resources/assets/chart.png", null));

        return menuPanel;
    }

    private JPanel createFooterPanel() {
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

    /**
     * Membuat satu card menu bergaya RoundedPanel dengan gambar icon dan label
     */
    private RoundedPanel createMenuCard(String text, String imagePath, Runnable onClick) {
        RoundedPanel card = new RoundedPanel(20) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow effect
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(2, 2, getWidth(), getHeight(), 20, 20);

                // Card background
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };

        card.setPreferredSize(new Dimension(160, 120));
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // Icon
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(img), SwingConstants.CENTER);

        // Text label
        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        textLabel.setForeground(new Color(60, 60, 60));

        card.add(iconLabel, BorderLayout.CENTER);
        card.add(textLabel, BorderLayout.SOUTH);

        // Tambah hover effect
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(248, 250, 255));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (onClick != null) {
                    onClick.run();
                }
            }
        });

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard(0, null, 0).setVisible(true));
    }
}