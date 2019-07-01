package com.dimdol.sql;

public final class SqlRuntime {

    private static final SqlRuntime INSTANCE = new SqlRuntime();

    public static final SqlRuntime getInstance() {
        return INSTANCE;
    }

    private ConnectionFactory connectionFactory;

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

}