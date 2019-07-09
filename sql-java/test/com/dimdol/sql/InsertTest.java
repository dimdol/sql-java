package com.dimdol.sql;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class InsertTest {

    @Test
    public void insert() {
        Sql sql = new Sql();
        sql.insertInto("USER");
        sql.value("NAME", "정대만");
        sql.value("AGE", 33);
        assertEquals("INSERT INTO USER (NAME, AGE) VALUES (?, ?)", sql.toSql());
        List<Parameter> parameters = sql.getParameters();
        assertEquals(2, parameters.size());
    }

}