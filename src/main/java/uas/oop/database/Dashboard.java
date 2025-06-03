package uas.oop.database;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Dashboard Mobile Banking");
        setSize(400, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Background panel
        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(new Color(180, 200, 245));
        add(background);

        // Container/card panel
//        JPanel card = new JPanel();
        RoundedPanel card = new RoundedPanel(20);
        card.setPreferredSize(new Dimension(320, 300));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(30, 20, 30, 20)
        ));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Judul dashboard
        JLabel title = new JLabel("Welcome to Mobile Banking");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(25, 25, 112));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // Simulasi data fetch dari DB
        try (Connection conn = ConnectionUtil.getDataSource().getConnection()) {
            var stmt = conn.prepareStatement("""
                SELECT customers.id, users.username, a.account_number, a.balance
                FROM customers
                JOIN users ON customers.user_id = users.id
                JOIN uasoop.accounts a ON customers.id = a.customer_id
                LIMIT 1;
            """);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                long accountNumber = rs.getLong("account_number");
                double balance = rs.getDouble("balance");

                JLabel userLabel = new JLabel("👤 Username: " + username);
                JLabel accLabel = new JLabel("🏦 Account Number: " + accountNumber);
                JLabel balLabel = new JLabel("💰 Balance: Rp " + String.format("%,.2f", balance));

                for (JLabel label : new JLabel[]{userLabel, accLabel, balLabel}) {
                    label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    label.setAlignmentX(Component.CENTER_ALIGNMENT);
                    label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                    card.add(label);
                }

            } else {
                card.add(new JLabel("Data tidak ditemukan."));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengambil data!");
            e.printStackTrace();
        }

        background.add(card);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}
