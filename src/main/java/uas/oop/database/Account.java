package uas.oop.database;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Account extends JFrame {

    private long loggedInAccountNumber;
    private String loggedInUsername;
    private int loggedInCustomerId;
    private Customer loggedInCustomer;
//    private JLabel currentBalanceLabel;

    public Account(long accountNumber, String username, int customerId) {
        this.loggedInAccountNumber = accountNumber;
        this.loggedInUsername = username;
        this.loggedInCustomerId = customerId;

        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
            this.loggedInCustomer = Customer.showCustomer(loggedInCustomerId, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        buildGUI();
    }

    public void buildGUI() {
        setTitle("Dashboard Mobile Banking");
        setSize(450, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(183, 208, 255));

        JPanel headerPanel = createHeaderPanel();
        JPanel contentPanel = createContentPanel();
        JPanel footerPanel = createFooterPanel();

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

        //TOMBOL Menu
        JButton headerLabel = new JButton("  Bank Plecit");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE); // warna teks link
        headerLabel.setContentAreaFilled(false); // hilangkan background
        headerLabel.setBorderPainted(false);     // hilangkan border
        headerLabel.setFocusPainted(false);      // hilangkan outline focus
        headerLabel.setOpaque(false);            // hilangkan opasitas background
        headerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Ubah kursor hover seperti link
        headerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                headerLabel.setForeground(Color.WHITE); // Warna hover
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 21));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                headerLabel.setForeground(Color.WHITE); // Warna semula
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20)); // Ukuran semula
            }
        });

        // Tambahkan ke headerPanel
        headerPanel.add(headerLabel, BorderLayout.WEST);

        // Aksi klik → buka halaman Dashboard
        headerLabel.addActionListener(e -> {
            dispose(); // tutup frame Register
            new Dashboard(loggedInAccountNumber,loggedInUsername ,loggedInCustomerId).setVisible(true);
        });

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

        JPanel infoPanel = createInfoPanel();
        JPanel wrapperInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapperInfoPanel.setOpaque(false);
        wrapperInfoPanel.add(infoPanel);

        contentPanel.add(wrapperInfoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

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
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(2, 2, getWidth(), getHeight(), 25, 25);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };

        infoPanel.setBackground(Color.WHITE);
        infoPanel.setPreferredSize(new Dimension(380, 590));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        ImageIcon icon = new ImageIcon("src/main/resources/assets/admin.png");
        Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(img));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel infoLabel = new JLabel();
        infoLabel.setForeground(new Color(45, 45, 45));
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label tambahan info customer
        JLabel nikLabel = new JLabel();
        nikLabel.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        nikLabel.setForeground(new Color(45, 45, 45));
        nikLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Menampilkan alamat
        JLabel addressLabel = new JLabel();
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        addressLabel.setForeground(new Color(45, 45, 45));
        addressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Menampilkan Email
        JLabel emailLabel = new JLabel();
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        emailLabel.setForeground(new Color(45, 45, 45));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Menampilkan phone
        JLabel phoneLabel = new JLabel();
        phoneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        phoneLabel.setForeground(new Color(45, 45, 45));
        phoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(iconLabel);
        infoPanel.add(Box.createVerticalStrut(35));
        infoPanel.add(infoLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(emailLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(phoneLabel);
        infoPanel.add(Box.createVerticalStrut(15));
//        infoPanel.add(currentBalanceLabel);
//        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(nikLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(addressLabel);
        infoPanel.add(Box.createVerticalStrut(15));

        if (loggedInCustomer != null) {
            infoLabel.setText( "    "+ loggedInCustomer.getFullName());
            nikLabel.setText("    NIK: " + loggedInCustomer.getNik());
            addressLabel.setText("    Alamat: " + loggedInCustomer.getAddress());
            emailLabel.setText("    Email: " + loggedInCustomer.getEmail());
            phoneLabel.setText("    Phone Number: " + loggedInCustomer.getPhone());
        } else {
            infoLabel.setText("    Login Terlebih Dahulu!");
        }

        return infoPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(65, 105, 225));
        footerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel footerInfoPanel = new JPanel(new GridLayout(3, 1, 0, 2));
        footerInfoPanel.setOpaque(false);

        JLabel taglineLabel = new JLabel("🏦 Bank Plecit - Rentenir Terpercaya");
        taglineLabel.setForeground(Color.WHITE);
        taglineLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        taglineLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel securityLabel = new JLabel("🔒 Aman dan Terpercaya | 24 Jam Layanan");
        securityLabel.setForeground(new Color(220, 230, 255));
        securityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        securityLabel.setHorizontalAlignment(SwingConstants.CENTER);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Account(0, null, 0).setVisible(true));
    }
}
