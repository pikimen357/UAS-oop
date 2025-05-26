package uas.oop.database;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementTest {

    @Test
    void testExecuteUpdate () throws SQLException {
        Connection connection = ConnectionUtil.getDataSource().getConnection();
        Statement statement = connection.createStatement();

        String sql = """
        INSERT INTO uasoop.users (username, password_hash, role) 
        VALUES ( 'Endang', 'endang02', 'admin');
        """;

        int updateCount = statement.executeUpdate(sql);
        System.out.println(updateCount);

        statement.close();
        connection.close();
    }

    @Test
    void testResult () throws SQLException {
        Connection connection = ConnectionUtil.getDataSource().getConnection();
        Statement statement = connection.createStatement();

        String sql = """
        SELECT * FROM uasoop.users;
        """;

        ResultSet resultSet = statement.executeQuery(sql);

        resultSet.close();
        statement.close();
        connection.close();
    }
}
