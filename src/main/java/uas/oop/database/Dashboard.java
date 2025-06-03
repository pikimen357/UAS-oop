package uas.oop.database;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {


    // 🔵 Deklarasi variabel di dalam class, bukan di dalam constructor
    private long loggedInAccountNumber;
    private String loggedInUsername;
    private int loggedInCustomerId;
    // 🔵 Constructor tambahan untuk inisialisasi data
    public Dashboard(long accountNumber, String username, int customerId) {
        this(); // Panggil constructor default
        this.loggedInAccountNumber = accountNumber;
        this.loggedInUsername = username;
        this.loggedInCustomerId = customerId;
    }

    public Dashboard() {


        setTitle("Dashboard Mobile Banking");
        setSize(400, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel utama (pakai BorderLayout supaya header di atas)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(180, 200, 245));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(65, 105, 225));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50));

        JLabel headerLabel = new JLabel("  Bank Plecit"); // spasi margin kiri
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerPanel.add(headerLabel, BorderLayout.WEST);

        JLabel headerLabel2 = new JLabel("Logout  "); // spasi margin kiri
        headerLabel2.setForeground(Color.WHITE);
        headerLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        headerPanel.add(headerLabel2, BorderLayout.EAST);

        // Panel background untuk menu (pakai GridLayout agar rapi)
        JPanel menuPanel = new JPanel(new GridLayout(3, 2, 30, 20)); // 3 baris, 2 kolom
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // padding

        // Contoh card menu
        menuPanel.add(createMenuCard("Akun", "src/main/resources/assets/admin.png"));
        menuPanel.add(createMenuCard("Info", "src/main/resources/assets/img.png"));
        menuPanel.add(createMenuCard("Tarik Tunai", "src/main/resources/assets/transfer.png"));
        menuPanel.add(createMenuCard("Setor Tunai", "src/main/resources/assets/payment.png"));
        menuPanel.add(createMenuCard("Transfer", "src/main/resources/assets/trnsfr.png"));
        menuPanel.add(createMenuCard("Pulsa & Data", "src/main/resources/assets/chart.png"));


        // Tambahkan header dan menuPanel ke mainPanel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    /**
     * Membuat satu card menu bergaya RoundedPanel dengan gambar icon dan label
     */
    private RoundedPanel createMenuCard(String text, String imagePath) {
        RoundedPanel card = new RoundedPanel(20);
        card.setPreferredSize(new Dimension(90, 75));
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 🔵 Gunakan gambar sebagai icon
        ImageIcon icon = new ImageIcon(imagePath);

        // 🔵 Ukuran icon (resize agar tidak terlalu besar)
        Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(img), SwingConstants.CENTER);

        // 🔵 Text label
        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));

        card.add(iconLabel, BorderLayout.CENTER);
        card.add(textLabel, BorderLayout.SOUTH);

        return card;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}
