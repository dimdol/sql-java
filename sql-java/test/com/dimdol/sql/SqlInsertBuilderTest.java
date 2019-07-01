package com.dimdol.sql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SqlInsertBuilderTest {

    @Test
    public void insert() {
        SqlInsertBuilder sql = new SqlInsertBuilder();
        sql.into("USER");
        sql.value("NAME", "Kim Seung Hwank");
        sql.value("AGE", 23);
        assertEquals("INSERT INTO USER (NAME, AGE) VALUES (?, ?)", sql.toSql());
    }

    @Test
    public void insertWithoutColumnName() {
        SqlInsertBuilder sql = new SqlInsertBuilder();
        sql.into("USER");
        sql.value("Kim Seung Hwank");
        sql.value(23);
        assertEquals("INSERT INTO USER VALUES (?, ?)", sql.toSql());
    }

}
