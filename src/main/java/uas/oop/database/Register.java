package uas.oop.database;

import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;

public class Register extends JFrame {
    public Register() {
        setTitle("Bank Plecit");
        setSize(450, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(180, 200, 245));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(65, 105, 225));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));

        JLabel headerLabel = new JLabel("  Bank Plecit"); // spasi = margin kiri
        headerLabel.setForeground(Color.BLACK);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerPanel.add(headerLabel, BorderLayout.WEST);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setContentAreaFilled(false); // supaya transparan
        loginButton.setBorderPainted(false);     // hilangkan border
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Tambahkan ke headerPanel di posisi EAST
        headerPanel.add(loginButton, BorderLayout.EAST);

        // Aksi klik → buka halaman Login
        loginButton.addActionListener(e -> {
            // Contoh: tutup frame ini, buka Login baru
            dispose(); // tutup frame Register
            new Login().setVisible(true);
        });


        // Panel input di tengah
        JPanel background = new JPanel(new GridBagLayout());
        background.setOpaque(false);

        RoundedPanel container = new RoundedPanel(20);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        container.setPreferredSize(new Dimension(400, 700));

        JLabel title = new JLabel("Daftar Akun");
        title.setFont(new Font("Times New Roman", Font.BOLD, 20));
        title.setForeground(new Color(25, 25, 112));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(title);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        // Utility method untuk membuat label dan field
        JTextField usernameField = createField(container, "Username");
        JPasswordField passwordField = new JPasswordField();
        addField(container, "Password", passwordField);

        JTextField emailField = createField(container, "Email");
        JTextField fullNameField = createField(container, "Full Name");
        JTextField nikField = createField(container, "NIK");
        JTextField phoneField = createField(container, "Phone");
        JTextField addressField = createField(container, "Address");
        JTextField saldoField = createField(container, "Saldo Awal");
        saldoField.setFont(new Font("Segoe UI", Font.BOLD, 20));

        // Tombol daftar
        JButton daftarButton = new JButton("Daftar");
        daftarButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        daftarButton.setBackground(new Color(65, 105, 225));
        daftarButton.setForeground(Color.WHITE);
        daftarButton.setFocusPainted(false);
        daftarButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        daftarButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        daftarButton.setPreferredSize(new Dimension(100, 30));
        container.add(Box.createRigidArea(new Dimension(0, 20)));
        container.add(daftarButton);

        background.add(container);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(background, BorderLayout.CENTER);

        add(mainPanel);

        // Aksi tombol daftar
        daftarButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());
            String email = emailField.getText();
            String fullName = fullNameField.getText();
            String nik = nikField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();
            String saldoText = saldoField.getText();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() ||
                    fullName.isEmpty() || nik.isEmpty() || phone.isEmpty() ||
                    address.isEmpty() || saldoText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double saldoAwal;
            try {
                saldoAwal = Double.parseDouble(saldoText);
                if (saldoAwal < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Saldo harus berupa angka positif!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
                connection.setAutoCommit(false);

                // Simpan user dan customer
                Customer customer = new Customer(username, password, "customer", email, fullName, nik, phone, address);
                customer.insertToUser(connection);
                customer.insertToCustomer(connection);

                // Generate nomor akun random
                long accountNumber = 100_000_000_000L + (Math.abs(new SecureRandom().nextLong()) % 900_000_000_000L);

                // Simpan akun
                SavingsAccount account = new SavingsAccount(accountNumber, saldoAwal, customer.getIdUser());
                account.insertToAccounts(connection);

                connection.commit();

                JOptionPane.showMessageDialog(this,
                        "Registrasi berhasil!\nAccount Number: " + accountNumber + "\nSaldo: " + saldoAwal,
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                dispose();

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal registrasi: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JTextField createField(JPanel container, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(label);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        container.add(textField);
        container.add(Box.createRigidArea(new Dimension(0, 15)));

        return textField;
    }

    private void addField(JPanel container, String labelText, JPasswordField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(label);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        container.add(field);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Register().setVisible(true));
    }
}
