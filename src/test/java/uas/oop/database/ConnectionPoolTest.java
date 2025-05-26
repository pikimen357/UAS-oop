package uas.oop.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolTest {

    @Test
    void testHikariCP(){
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/uasoop?serverTimezone=Asia/Jakarta");
        config.setUsername("root");
        config.setPassword("Hesoyam1!");

        config.setMaximumPoolSize(20);
        config.setMinimumIdle(10);

        config.setIdleTimeout(60_000);
        config.setMaxLifetime(10 * 60_000);

        try {
            HikariDataSource ds = new HikariDataSource(config);
            Connection connection = ds.getConnection();

            // mengembalikan ke hikari (belum close)
            connection.close();

            // close connection
            ds.close();

        } catch (SQLException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void testUtil() throws SQLException {
        Connection connection = ConnectionUtil.getDataSource().getConnection();
    }
}
