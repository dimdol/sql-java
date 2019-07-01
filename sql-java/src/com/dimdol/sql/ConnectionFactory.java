package com.dimdol.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {

    Connection getConnection() throws SQLException;

    Connection getConnection(Enum<?> type) throws SQLException;

}
