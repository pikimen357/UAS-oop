package uas.oop.database;

import javax.swing.*;

public class BankPlecitApp {
    public static void main(String[] args) {
        // Set Look and Feel hanya sekali di sini
        SwingUtilities.invokeLater(() -> {
            try {
                // Pilih salah satu LaF yang konsisten
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // Atau gunakan cross-platform LaF untuk konsistensi
                // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            } catch (Exception e) {
                System.err.println("Could not set look and feel: " + e.getMessage());
                // Gunakan default LaF jika gagal
            }

            // Jalankan aplikasi dengan Login sebagai entry point
            new Login().setVisible(true);
        });
    }
}