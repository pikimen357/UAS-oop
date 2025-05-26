package uas.oop.database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionTest {

    @BeforeAll
    static void beforeAll(){
        try{
            Driver mysqldriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void testConnection() {

        String jdbcUrl = "jdbc:mysql://localhost:3306/uasoop?serverTimezone=Asia/Jakarta";
        String username = "root";
        String password = "Hesoyam1!";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            System.out.println("Connection successful!!");
        }catch (SQLException e){
            Assertions.fail(e);
        }
    }

    @Test
    void testConnectionClose() {

        String jdbcUrl = "jdbc:mysql://localhost:3306/uasoop?serverTimezone=Asia/Jakarta";
        String username = "root";
        String password = "Hesoyam1!";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)){
            System.out.println("Connection successful!!");

            // connection.close();
        }catch (SQLException e){
            Assertions.fail(e);
        }
    }
}
