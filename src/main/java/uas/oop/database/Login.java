package uas.oop.database;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Login extends JFrame {
    public Login() {
        setTitle("Bank Plecit");
        setSize(450, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel utama menggunakan BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(180, 200, 245));

        // 🔵 Header Panel
        JPanel headerPanel = createHeaderPanel();

        // 🔵 Panel Login di tengah
        JPanel contentPanel = createContentPanel();

        // 🔵 Footer Panel
        JPanel footerPanel = createFooterPanel();

        // Tambahkan semua panel ke mainPanel
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
                headerLabel.setForeground(Color.WHITE); // Warna hover link
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 21));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                headerLabel.setForeground(Color.WHITE); // Kembali ke warna semula
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            }
        });

        // Tambahkan ke headerPanel
        headerPanel.add(headerLabel, BorderLayout.WEST);

        // Aksi klik → buka halaman Dashboard
        headerLabel.addActionListener(e -> {
            dispose(); // tutup frame Register
            new Dashboard(0, null, 0).setVisible(true);
        });

        //TOMBOL REGISTER
        JButton regButton = new JButton("Register");
        regButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        regButton.setForeground(Color.WHITE); // warna teks link
        regButton.setContentAreaFilled(false); // hilangkan background
        regButton.setBorderPainted(false);     // hilangkan border
        regButton.setFocusPainted(false);      // hilangkan outline focus
        regButton.setOpaque(false);            // hilangkan opasitas background
        regButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Ubah kursor hover seperti link
        regButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                regButton.setForeground(Color.WHITE); // Warna hover link
                regButton.setFont(new Font("Tahoma", Font.BOLD, 14));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                regButton.setForeground(Color.WHITE); // Kembali ke warna semula
                regButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
            }
        });

        // Tambahkan ke headerPanel
        headerPanel.add(regButton, BorderLayout.EAST);

        // Aksi klik → buka halaman Register
        regButton.addActionListener(e -> {
            dispose(); // tutup frame Register
            new Register().setVisible(true);
        });

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel background = new JPanel(new GridBagLayout());
        background.setOpaque(false); // supaya warna header tidak tertutup
        RoundedPanel container = new RoundedPanel(20);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        container.setPreferredSize(new Dimension(350, 300));

        JLabel title = new JLabel("Please Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(25, 25, 112));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(title);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        JTextField usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(usernameLabel);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        container.add(usernameField);
        container.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(passwordLabel);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        container.add(passwordField);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(65, 105, 225));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(100, 30));
        container.add(loginButton);

        background.add(container);

        // Aksi tombol login
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            try (Connection conn = ConnectionUtil.getDataSource().getConnection()) {
                var stmt = conn.prepareStatement("""
                SELECT customers.id, users.username, users.password_hash, a.account_number
                FROM customers
                JOIN users ON customers.user_id = users.id
                JOIN uasoop.accounts a ON customers.id = a.customer_id
                WHERE users.username = ?;
            """);
                stmt.setString(1, username);
                var rs = stmt.executeQuery();

                if (rs.next()) {
                    String passwordHashDB = rs.getString("password_hash");
                    long accountNumber = rs.getLong("account_number");
                    int customerId = rs.getInt("id");

                    if (password.equals(passwordHashDB)) {
                        JOptionPane.showMessageDialog(this, "Login " + username + " berhasil (" + accountNumber + ")");
                        dispose();
                        new Dashboard(accountNumber, username, customerId).setVisible(true);
//                        new Dashboard().setVisible(true);
//                        new AppTransaksi(accountNumber, username, customerId).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Password salah!");
                        usernameField.setText("");
                        passwordField.setText("");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Username tidak ditemukan!");
                    usernameField.setText("");
                    passwordField.setText("");
                }

                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Gagal terhubung ke database!");
                ex.printStackTrace();
            }
        });

        return background;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}