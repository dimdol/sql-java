package com.dimdol.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.dimdol.sql.ConnectionFactory;

public class OracleConnectionFactory implements ConnectionFactory {

    @Override
    public Connection getConnection() throws SQLException {
        String dbURL = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
        return DriverManager.getConnection(dbURL, "SYSTEM", "1988kim");
    }

    @Override
    public Connection getConnection(Enum<?> connectionType) throws SQLException {
        if (connectionType == DB.DERBY) {
            throw new IllegalArgumentException();
        }
        return getConnection();
    }

}