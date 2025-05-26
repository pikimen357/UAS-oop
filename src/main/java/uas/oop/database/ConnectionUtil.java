package uas.oop.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

// Untuk koneksi MySQL universal

public class ConnectionUtil {

    public static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/uasoop?serverTimezone=Asia/Jakarta");
        config.setUsername("root");
        config.setPassword("Hesoyam1!");

        config.setMaximumPoolSize(20);
        config.setMinimumIdle(10);

        config.setIdleTimeout(60_000);
        config.setMaxLifetime(10 * 60_000);

        dataSource = new HikariDataSource(config);
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}
