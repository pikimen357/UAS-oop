package uas.oop.database;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;



public class Login extends JFrame {
    public Login() {
        setTitle("Bank Plecit");
        setSize(400, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(new Color(180, 200, 245));

        add(background);

        // Gunakan RoundedPanel
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
                        new AppTransaksi(accountNumber, username, customerId).setVisible(true);
//                        new Dashboard().setVisible(true);
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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}