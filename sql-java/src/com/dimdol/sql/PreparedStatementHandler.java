package com.dimdol.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementHandler {

    void accept(PreparedStatement pstmt) throws SQLException;

}