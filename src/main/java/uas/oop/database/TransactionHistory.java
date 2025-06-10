package uas.oop.database;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionHistory extends JFrame {

    private long loggedInAccountNumber;
    private String loggedInUsername;
    private int loggedInCustomerId;
    private JLabel currentBalanceLabel;
    private JPanel transactionCardsPanel;
    private JScrollPane scrollPane;
    private JLabel statusLabel;
    private JComboBox<String> sortComboBox;

    public TransactionHistory(long accountNumber, String username, int customerId) {
        this.loggedInAccountNumber = accountNumber;
        this.loggedInUsername = username;
        this.loggedInCustomerId = customerId;

        buildGUI();
        loadAllTransactions();
    }

    public void buildGUI(){
        setTitle("Riwayat Transaksi - " + loggedInUsername);
        setSize(450, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel utama dengan proper sizing
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(183, 208, 255));
        mainPanel.setPreferredSize(new Dimension(450, 850));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();

        // Content Panel (tengah)
        JPanel contentPanel = createContentPanel();

        // Footer Panel
        JPanel footerPanel = createFooterPanel();

        // Gabungkan semua dengan sizing yang tepat
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(65, 105, 225));
        headerPanel.setPreferredSize(new Dimension(450, 65));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // TOMBOL Menu
        JButton headerLabel = new JButton("🏦 Bank Plecit");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setContentAreaFilled(false);
        headerLabel.setBorderPainted(false);
        headerLabel.setFocusPainted(false);
        headerLabel.setOpaque(false);
        headerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        headerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                headerLabel.setForeground(new Color(220, 230, 255));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                headerLabel.setForeground(Color.WHITE);
            }
        });

        headerLabel.addActionListener(e -> {
            dispose();
            new Dashboard(loggedInAccountNumber, loggedInUsername, loggedInCustomerId).setVisible(true);
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setOpaque(false);
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setForeground(new Color(255, 200, 200));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setForeground(Color.WHITE);
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
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 248, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Control Panel untuk sorting dan filter
        JPanel controlPanel = createControlPanel();

        // Transaction Cards Panel dengan ScrollPane
        transactionCardsPanel = new JPanel();
        transactionCardsPanel.setLayout(new BoxLayout(transactionCardsPanel, BoxLayout.Y_AXIS));
        transactionCardsPanel.setBackground(new Color(245, 248, 255));
        transactionCardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Custom Modern ScrollPane
        scrollPane = createModernScrollPane(transactionCardsPanel);

        // Status Panel
        JPanel statusPanel = createStatusPanel();

        contentPanel.add(controlPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(statusPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    private JScrollPane createModernScrollPane(JComponent component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(245, 248, 255));

        // Modern scrollbar styling
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(180, 180, 180, 150);
                this.trackColor = new Color(245, 248, 255);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y, thumbBounds.width - 4,
                        thumbBounds.height, 8, 8);
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                // Transparent track
            }
        });

        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));

        return scrollPane;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(new Color(245, 248, 255));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(245, 248, 255));

        JLabel titleLabel = new JLabel("📊 Riwayat Transaksi");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(65, 105, 225));
        titlePanel.add(titleLabel);

        // Control buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonsPanel.setBackground(new Color(245, 248, 255));

        // Modern Sort ComboBox
        JPanel sortPanel = createModernComboBoxPanel();

        // Modern Refresh Button
        JButton refreshButton = createModernButton("🔄 Refresh", new Color(65, 105, 225));
        refreshButton.addActionListener(e -> loadAllTransactions());

        buttonsPanel.add(sortPanel);
        buttonsPanel.add(refreshButton);

        controlPanel.add(titlePanel);
        controlPanel.add(buttonsPanel);

        return controlPanel;
    }

    private JPanel createModernComboBoxPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(new Color(245, 248, 255));

        JLabel sortLabel = new JLabel("Urutkan:");
        sortLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sortLabel.setForeground(new Color(80, 80, 80));

        sortComboBox = new JComboBox<>(new String[]{
                "🕐 Terbaru → Terlama",
                "🕐 Terlama → Terbaru"
        });

        sortComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sortComboBox.setBackground(Color.WHITE);
        sortComboBox.setForeground(new Color(60, 60, 60));
        sortComboBox.setBorder(new RoundedBorder(8, new Color(220, 220, 220)));
        sortComboBox.setPreferredSize(new Dimension(160, 32));
        sortComboBox.setFocusable(false);

        // Custom renderer for modern look
        sortComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                if (isSelected) {
                    setBackground(new Color(65, 105, 225, 50));
                    setForeground(new Color(65, 105, 225));
                } else {
                    setBackground(Color.WHITE);
                    setForeground(new Color(60, 60, 60));
                }
                return this;
            }
        });

        sortComboBox.addActionListener(e -> loadAllTransactions());

        panel.add(sortLabel);
        panel.add(sortComboBox);
        return panel;
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(8, bgColor));
        button.setPreferredSize(new Dimension(100, 32));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = bgColor;

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(originalColor);
            }
        });

        return button;
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setBackground(new Color(245, 248, 255));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        statusLabel = new JLabel("Memuat transaksi...");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(120, 120, 120));

        statusPanel.add(statusLabel);
        return statusPanel;
    }

    private void loadAllTransactions() {
        transactionCardsPanel.removeAll();

        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
            List<Transaction> allTransactions = Transaction.getAllTransactions(loggedInAccountNumber, connection);

            boolean newestFirst = sortComboBox.getSelectedIndex() == 0;
            allTransactions = allTransactions.stream()
                    .sorted((t1, t2) -> newestFirst ?
                            t2.getCreatedAt().compareTo(t1.getCreatedAt()) :
                            t1.getCreatedAt().compareTo(t2.getCreatedAt()))
                    .collect(Collectors.toList());

            for (Transaction transaction : allTransactions) {
                JPanel transactionCard = createTransactionCard(transaction);
                transactionCardsPanel.add(transactionCard);
                transactionCardsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }

            statusLabel.setText("Total: " + allTransactions.size() + " transaksi");

            if (allTransactions.isEmpty()) {
                JPanel emptyPanel = createEmptyStatePanel();
                transactionCardsPanel.add(emptyPanel);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal memuat riwayat transaksi: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            statusLabel.setText("Gagal memuat transaksi");
        }

        transactionCardsPanel.revalidate();
        transactionCardsPanel.repaint();
    }

    private JPanel createTransactionCard(Transaction transaction) {
        RoundedPanel card = new RoundedPanel(12) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Subtle shadow
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(1, 2, getWidth() - 1, getHeight() - 1, 12, 12);

                // Card background
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 2, 12, 12);

                // Border
                g2d.setColor(new Color(230, 235, 245));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 2, 12, 12);
            }
        };

        card.setMaximumSize(new Dimension(400, 75));
        card.setPreferredSize(new Dimension(400, 75));
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Left panel - Icon dan Type
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(55, 50));

        String iconText = transaction.getType().equals("deposit") ? "💰" : "💸";
        JLabel iconLabel = new JLabel(iconText);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        String typeText = transaction.getType().equals("deposit") ? "MASUK" : "KELUAR";
        JLabel typeLabel = new JLabel(typeText);
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 9));
        typeLabel.setForeground(transaction.getType().equals("deposit") ?
                new Color(34, 139, 34) : new Color(220, 20, 60));
        typeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        leftPanel.add(iconLabel, BorderLayout.CENTER);
        leftPanel.add(typeLabel, BorderLayout.SOUTH);

        // Center panel - Description dan Date/Time
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        JLabel descLabel = new JLabel(transaction.getDescription());
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        descLabel.setForeground(new Color(50, 50, 50));

        String dateTimeText = transaction.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy • HH:mm"));
        JLabel dateTimeLabel = new JLabel(dateTimeText);
        dateTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        dateTimeLabel.setForeground(new Color(130, 130, 130));

        centerPanel.add(descLabel, BorderLayout.NORTH);
        centerPanel.add(dateTimeLabel, BorderLayout.SOUTH);

        // Right panel - Amount
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(110, 50));

        String amountText = transaction.getType().equals("deposit") ?
                String.format("+Rp %,.0f", transaction.getAmount()) :
                String.format("-Rp %,.0f", transaction.getAmount());

        JLabel amountLabel = new JLabel(amountText);
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        amountLabel.setForeground(transaction.getType().equals("deposit") ?
                new Color(34, 139, 34) : new Color(220, 20, 60));
        amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        rightPanel.add(amountLabel, BorderLayout.CENTER);

        card.add(leftPanel, BorderLayout.WEST);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(248, 250, 255));
                card.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.repaint();
            }
        });

        return card;
    }

    private JPanel createEmptyStatePanel() {
        JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        emptyPanel.setOpaque(false);
        emptyPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));

        JLabel emptyIcon = new JLabel("📊");
        emptyIcon.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emptyText = new JLabel("Belum ada transaksi");
        emptyText.setFont(new Font("Segoe UI", Font.BOLD, 15));
        emptyText.setForeground(new Color(120, 120, 120));
        emptyText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emptySubText = new JLabel("Transaksi Anda akan muncul di sini");
        emptySubText.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        emptySubText.setForeground(new Color(150, 150, 150));
        emptySubText.setAlignmentX(Component.CENTER_ALIGNMENT);

        emptyPanel.add(emptyIcon);
        emptyPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        emptyPanel.add(emptyText);
        emptyPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        emptyPanel.add(emptySubText);

        return emptyPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(65, 105, 225));
        footerPanel.setPreferredSize(new Dimension(450, 70));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JPanel footerInfoPanel = new JPanel(new GridLayout(3, 1, 0, 2));
        footerInfoPanel.setOpaque(false);

        JLabel taglineLabel = new JLabel("🏦 Bank Plecit - Rentenir Terpercaya");
        taglineLabel.setForeground(Color.WHITE);
        taglineLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        taglineLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel securityLabel = new JLabel("🔒 Aman dan Terpercaya | 24 Jam Layanan");
        securityLabel.setForeground(new Color(220, 230, 255));
        securityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        securityLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel copyrightLabel = new JLabel("© 2024 Bank Plecit. All rights reserved.");
        copyrightLabel.setForeground(new Color(200, 215, 255));
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);

        footerInfoPanel.add(taglineLabel);
        footerInfoPanel.add(securityLabel);
        footerInfoPanel.add(copyrightLabel);

        footerPanel.add(footerInfoPanel, BorderLayout.CENTER);

        return footerPanel;
    }

    // Method untuk filter berdasarkan tanggal
    public void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        transactionCardsPanel.removeAll();

        try (Connection connection = ConnectionUtil.getDataSource().getConnection()) {
            List<Transaction> allTransactions = Transaction.getAllTransactions(loggedInAccountNumber, connection);

            List<Transaction> filteredTransactions = allTransactions.stream()
                    .filter(t -> {
                        LocalDate transactionDate = t.getCreatedAt().toLocalDate();
                        return !transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate);
                    })
                    .sorted((t1, t2) -> sortComboBox.getSelectedIndex() == 0 ?
                            t2.getCreatedAt().compareTo(t1.getCreatedAt()) :
                            t1.getCreatedAt().compareTo(t2.getCreatedAt()))
                    .collect(Collectors.toList());

            for (Transaction transaction : filteredTransactions) {
                JPanel transactionCard = createTransactionCard(transaction);
                transactionCardsPanel.add(transactionCard);
                transactionCardsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }

            statusLabel.setText("Ditemukan: " + filteredTransactions.size() + " transaksi");

            if (filteredTransactions.isEmpty()) {
                JPanel emptyPanel = createEmptyStatePanel();
                transactionCardsPanel.add(emptyPanel);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memfilter transaksi: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        transactionCardsPanel.revalidate();
        transactionCardsPanel.repaint();
    }

    // Custom rounded border class
    class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 8, 2, 8);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TransactionHistory(123456789L, "TestUser", 1).setVisible(true));
    }
}