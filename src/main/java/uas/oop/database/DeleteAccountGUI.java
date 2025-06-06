package uas.oop.database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class DeleteAccountGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private JButton deleteButton;
    private JButton cancelButton;
    private JPanel mainPanel;

    public DeleteAccountGUI() {
        setTitle("Delete Account - Bank Plecit");
        setSize(450, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initializeComponents();
        setupLayout();
        setupEventListeners();
    }

    private void initializeComponents() {
        // Panel utama
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(180, 200, 245));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(65, 105, 225));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));

        // Header Label
        JButton headerLabel = new JButton("  Bank Plecit");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setContentAreaFilled(false);
        headerLabel.setBorderPainted(false);
        headerLabel.setFocusPainted(false);
        headerLabel.setOpaque(false);
        headerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Header hover effect
        headerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 21));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            }
        });

        headerPanel.add(headerLabel, BorderLayout.WEST);

        // Back to Dashboard
        headerLabel.addActionListener(e -> {
            dispose();
            new Dashboard(0, null, 0).setVisible(true);
        });

        // Main content panel
        JPanel background = new JPanel(new GridBagLayout());
        background.setOpaque(false);

        RoundedPanel container = new RoundedPanel(20);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        container.setPreferredSize(new Dimension(400, 500));

        // Title
        JLabel title = new JLabel("Delete Account");
        title.setFont(new Font("Times New Roman", Font.BOLD, 24));
        title.setForeground(new Color(220, 20, 60)); // Crimson color for delete
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Warning message
        JLabel warningLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<b>⚠️ WARNING ⚠️</b><br>"
                + "This action will permanently delete your account<br>"
                + "and all associated data. This cannot be undone!"
                + "</div></html>");
        warningLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        warningLabel.setForeground(new Color(255, 69, 0)); // Orange red
        warningLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        warningLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 69, 0), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Username field
        usernameField = createField(container, "Username");

        // Password field
        passwordField = new JPasswordField();
        addPasswordField(container, "Password", passwordField);

        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        deleteButton = new JButton("Delete Account");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteButton.setBackground(new Color(207, 32, 67));
        deleteButton.setForeground(Color.white);
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteButton.setPreferredSize(new Dimension(140, 35));



        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(new Color(46, 67, 204)); // Gray
        cancelButton.setForeground(Color.white);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);

        // Add components to container
        container.add(title);
        container.add(Box.createRigidArea(new Dimension(0, 20)));
        container.add(warningLabel);
        container.add(Box.createRigidArea(new Dimension(0, 30)));

        // Add username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(usernameLabel);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        container.add(usernameField);
        container.add(Box.createRigidArea(new Dimension(0, 15)));

        // Add password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(passwordLabel);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        container.add(passwordField);
        container.add(Box.createRigidArea(new Dimension(0, 15)));

        container.add(statusLabel);
        container.add(Box.createRigidArea(new Dimension(0, 20)));
        container.add(buttonPanel);

        background.add(container);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(background, BorderLayout.CENTER);

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

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void setupLayout() {
        // Layout is already set up in initializeComponents()
    }

    private void setupEventListeners() {
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Dashboard(0, null, 0).setVisible(true);
            }
        });

        // Enter key support
        usernameField.addActionListener(e -> passwordField.requestFocus());
        passwordField.addActionListener(e -> deleteAccount());
    }

    private void deleteAccount() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Input validation
        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please fill in all fields!", Color.RED);
            return;
        }

        // Confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you absolutely sure you want to delete this account?\n" +
                        "Username: " + username + "\n\n" +
                        "This action cannot be undone!",
                "Confirm Account Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            showStatus("Account deletion cancelled.", Color.BLUE);
            return;
        }

        // Disable button during processing
        deleteButton.setEnabled(false);
        deleteButton.setText("Deleting...");
        showStatus("Processing deletion...", Color.BLUE);

        // Perform deletion in background thread
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private String resultMessage;
            private boolean success = false;

            @Override
            protected Void doInBackground() throws Exception {
                try (Connection conn = ConnectionUtil.getDataSource().getConnection()) {
                    // Validate login first
                    var stmt = conn.prepareStatement("""
                            SELECT customers.id, users.username, users.password_hash, a.account_number
                            FROM customers
                            JOIN users ON customers.user_id = users.id
                            JOIN accounts a ON customers.id = a.customer_id
                            WHERE users.username = ?;
                        """);
                    stmt.setString(1, username);
                    var rs = stmt.executeQuery();

                    if (rs.next()) {
                        int idCustomer = rs.getInt("id");
                        String passwordHashDB = rs.getString("password_hash");
                        long accountNumber = rs.getLong("account_number");

                        // Validate password
                        if (!password.equals(passwordHashDB)) {
                            resultMessage = "Invalid password!";
                            return null;
                        }

                        rs.close();
                        stmt.close();

                        // Start deletion transaction
                        conn.setAutoCommit(false);

                        try {
                            // 1. Delete from accounts table first
                            var deleteAccountStmt = conn.prepareStatement("""
                                    DELETE FROM accounts
                                    WHERE customer_id = ?
                                """);
                            deleteAccountStmt.setInt(1, idCustomer);
                            int accountsDeleted = deleteAccountStmt.executeUpdate();

                            // 2. Delete from customers table
                            var deleteCustomerStmt = conn.prepareStatement("""
                                    DELETE FROM customers
                                    WHERE id = ?
                                """);
                            deleteCustomerStmt.setInt(1, idCustomer);
                            int customersDeleted = deleteCustomerStmt.executeUpdate();

                            // 3. Delete from users table
                            var deleteUserStmt = conn.prepareStatement("""
                                    DELETE FROM users
                                    WHERE username = ?
                                """);
                            deleteUserStmt.setString(1, username);
                            int usersDeleted = deleteUserStmt.executeUpdate();

                            // Commit transaction
                            conn.commit();

                            success = true;
                            resultMessage = String.format(
                                    "Account successfully deleted!\n" +
                                            "Accounts deleted: %d\n" +
                                            "Customers deleted: %d\n" +
                                            "Users deleted: %d",
                                    accountsDeleted, customersDeleted, usersDeleted
                            );

                            deleteAccountStmt.close();
                            deleteCustomerStmt.close();
                            deleteUserStmt.close();

                        } catch (SQLException e) {
                            conn.rollback();
                            resultMessage = "Failed to delete account: " + e.getMessage();
                        } finally {
                            conn.setAutoCommit(true);
                        }

                    } else {
                        resultMessage = "Username not found!";
                    }

                } catch (SQLException e) {
                    resultMessage = "Database error: " + e.getMessage();
                }
                return null;
            }

            @Override
            protected void done() {
                // Re-enable button
                deleteButton.setEnabled(true);
                deleteButton.setText("Delete Account");

                if (success) {
                    JOptionPane.showMessageDialog(
                            DeleteAccountGUI.this,
                            resultMessage,
                            "Account Deleted",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    dispose();
                    new Dashboard(0, null, 0).setVisible(true);
                } else {
                    showStatus(resultMessage, Color.RED);
                }
            }
        };

        worker.execute();
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    private JTextField createField(JPanel container, String labelText) {
        JTextField textField = new JTextField();
        return textField;
    }

    private void addPasswordField(JPanel container, String labelText, JPasswordField field) {
        // Method ini tidak digunakan lagi karena kita langsung add ke container
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DeleteAccountGUI().setVisible(true);
        });
    }
}