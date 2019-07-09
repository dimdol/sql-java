package com.dimdol.example;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.dimdol.sql.ConnectionFactory;

public class OracleConnectionFactory implements ConnectionFactory {

    private DataSource ds;

    @Override
    public Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    @Override
    public Connection getConnection(Enum<?> connectionType) throws SQLException {
        if (connectionType == DB.DERBY) {
            throw new IllegalArgumentException();
        }
        return getConnection();
    }

    private synchronized DataSource getDataSource() {
        if (ds == null) {
            String dbURL = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
            ds = new DataSourceCreator().setupDataSource(dbURL, "SYSTEM", "1988kim");
        }
        return ds;
    }

}