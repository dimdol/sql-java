package com.dimdol.sql;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

public class SqlDeleterTest {

    @Test
    public void base() {
        SqlDeleter sql = new SqlDeleter();
        sql.delete("USER");
        sql.where(Op.EQUAL, "ID", "32222");
        sql.run((Connection) null);
    }

}
