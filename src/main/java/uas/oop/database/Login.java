
package uas.oop.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.security.SecureRandom;
import javax.swing.*;
import java.awt.*;


public class Login extends JFrame {
    public Login() {
        setTitle("Sistem Informasi Akademis");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);

//        bikin panel dengan title login
        JPanel panel = new JPanel(new BorderLayout());

        add(panel);

        JLabel label = new JLabel("Please Login", JLabel.CENTER);
        label.setFont(new Font("Tahoma", Font.BOLD, 19));
        panel.add(label, BorderLayout.NORTH);

        JButton button = new JButton("Login");
        panel.add(button, BorderLayout.SOUTH);

//        bikin field
        JPanel fieldPanel = new JPanel(new GridLayout(2, 2));

//        bikin field username
        fieldPanel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        fieldPanel.add(usernameField);

//        bikin field password
        fieldPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        fieldPanel.add(passwordField);

        panel.add(fieldPanel);

        button.addActionListener(e -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            String passwordHashDB = null;
            long accountNumber;
            int idCustomer = -1;

            try (Connection conn = ConnectionUtil.getDataSource().getConnection()) {
                var stmt = conn.prepareStatement("""
                    SELECT customers.id, users.username, users.password_hash, a.account_number
                    FROM customers
                    JOIN users ON customers.user_id = users.id
                    JOIN uasoop.accounts a on customers.id = a.customer_id
                    WHERE users.username = ?;
                """);
                stmt.setString(1, username);
                var rs = stmt.executeQuery();

                if (rs.next()) {
                    idCustomer = rs.getInt("id");
                    passwordHashDB = rs.getString("password_hash");
                    accountNumber = rs.getLong("account_number");

                    // Validasi password
                    if (password.equals(passwordHashDB)) {
                        System.out.println("Login berhasil. ");
                        System.out.println("Username : " + username);
                        System.out.println("Account Number : " + accountNumber);
                        String msg = "Login " + username + " berhasil (" + accountNumber + ")";
                        JOptionPane.showMessageDialog(this, msg);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Wrong Username or Password");
                        usernameField.setText("");
                        passwordField.setText("");
                        return;
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Wrong Username");
                    usernameField.setText("");
                    passwordField.setText("");
                    return;
                }

                rs.close();
                stmt.close();
            } catch (SQLException err) {
                System.out.println("Gagal mengambil data: " + err.getMessage());
                return;
            }

//            if (username.equals("Admin") && password.equals("admin123")) {
//                JOptionPane.showMessageDialog(this, "GREAT!! Login Successful");
//                dispose();
////                new Dashboard().setVisible(true);
//            } else {
//                JOptionPane.showMessageDialog(this, "Wrong Username or Password");
//                usernameField.setText("");
//                passwordField.setText("");
//            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> new Login().setVisible(true)
        );
    }
}
