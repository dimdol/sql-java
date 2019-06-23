package com.dimdol.sql;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColumnTest {

    @Test
    public void column() {
        assertEquals("ID", new Column("ID").toSql());
    }

    @Test
    public void alias() {
        assertEquals("ID AS USER_ID", new Column("ID", "USER_ID").toSql());
    }

}