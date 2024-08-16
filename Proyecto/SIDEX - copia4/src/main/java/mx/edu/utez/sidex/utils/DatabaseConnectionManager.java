package mx.edu.utez.sidex.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private static final String JDBC_URL = "jdbc:mysql://sidex.ca9j43mxxj9t.us-east-1.rds.amazonaws.com:3306/SIDEX";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "#$BybeDeby8484$#";

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource dataSource;

    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new RuntimeException("Error", e); }
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        // Ajustes del pool
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(100);
        config.setIdleTimeout(60000); // 60 segundos de inactividad antes de cerrar una conexión
        config.setMaxLifetime(1800000); // 30 minutos antes de reciclar una conexión
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private DatabaseConnectionManager() {
        // Private constructor to prevent instantiation
    }
}
