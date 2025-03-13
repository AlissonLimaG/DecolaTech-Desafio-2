package com.DecolaTech.D2.persistence.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConnectionConfig {

    public static Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        try (InputStream input = ConnectionConfig.class.getClassLoader().getResourceAsStream("connection.properties")) {
            if (input == null) {
                throw new SQLException("O arquivo connection.properties não foi encontrado");
            }
            properties.load(input);
        } catch (Exception e) {
            throw new SQLException("Erro ao carregar as propriedades", e);
        }

        String url = properties.getProperty("DB_URL");
        String user = properties.getProperty("DB_USERNAME");
        String password = properties.getProperty("DB_PASSWORD");

        Connection connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);
        return connection;
    }
}
