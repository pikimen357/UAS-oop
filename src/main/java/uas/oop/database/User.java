package uas.oop.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
    private int idUser;

    private String username;
    private String passwordHash;
    private String role;
    private String email;

    public User( String username, String passwordHash, String role, String email) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.email = email;
    }

    public int getIdUser() { return idUser; }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {  this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username;}

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }


    public void showInfo() {
        System.out.println("User: " + username + " (Role: " + role + ")");
    }

    // Method overload - untuk transaksi (gunakan koneksi yang sudah ada)
    public int insertToUser(Connection connection) throws SQLException {
        String sql = """
        INSERT INTO users (username, password_hash, role, email)
        VALUES (?, ?, ?, ?)
        """;

        var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, username);
        preparedStatement.setString(2, passwordHash);
        preparedStatement.setString(3, role);
        preparedStatement.setString(4, email);

        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Inserting user failed, no rows affected.");
        }

        try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                this.idUser = generatedId;  // simpan ke objek
                System.out.println("Insert User successful, ID = " + idUser);
            } else {
                throw new SQLException("Inserting user failed, no ID obtained.");
            }
        }

        preparedStatement.close();
        // JANGAN tutup connection - biarkan App.java yang mengelola

        return this.idUser;
    }
}




