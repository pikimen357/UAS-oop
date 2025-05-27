package uas.oop.database;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementTest {

//    @Test
//    void testExecuteUpdate () throws SQLException {
//        Connection connection = ConnectionUtil.getDataSource().getConnection();
//        Statement statement = connection.createStatement();
//
//        String sql = """
//        INSERT INTO uasoop.users (username, password_hash, role, email)
//        VALUES ( 'Galih', '98galih', 'admin', 'galih@gmail.com');
//        """;
//
//        int updateCount = statement.executeUpdate(sql);
//        System.out.println(updateCount);
//
//        statement.close();
//        connection.close();
//    }

    @Test
    void testResult () throws SQLException {
        Connection connection = ConnectionUtil.getDataSource().getConnection();
        Statement statement = connection.createStatement();

        String sql = """
        SELECT * FROM uasoop.users;
        """;

        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            // Misalnya tabel users punya kolom: id (int), name (String), email (String)
            int id = resultSet.getInt("id");
            String username = resultSet.getString("username");
            String role = resultSet.getString("role");
            String email = resultSet.getString("email");

            System.out.println("ID: " + id + ", Name: " + username + ", Email: " + email + ", Role: " +role);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }
}
