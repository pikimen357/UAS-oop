package uas.oop.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
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

    public String getEmail() { return email; }

    public void setEmail(String email) {  this.email = email; }

    public void setRole(String role) { this.role = role; }

    public String getPasswordHash() { return passwordHash; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public void setUsername(String username) { this.username = username;}

    public String getUsername() { return username; }
    public String getRole() { return role; }

    public void showInfo() {
        System.out.println("User: " + username + " (Role: " + role + ")");
    }

    public void testExecuteUpdate () throws SQLException {
        Connection connection = ConnectionUtil.getDataSource().getConnection();
        Statement statement = connection.createStatement();

        String sql = """
        INSERT INTO customer (username, password_hash, role, email)
        VALUES (username, password, passwordHash, email);
        """;

        int updateCount = statement.executeUpdate(sql);
        System.out.println(updateCount);

        statement.close();
        connection.close();
    }

}

