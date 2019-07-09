package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class InsertIntoTest {

    @Test
    public void singleParam() {
        InsertInto insert = new InsertInto("USER");
        insert.value(Bind.PARAM, "NAME", "정대만");
        assertEquals("INSERT INTO USER (NAME) VALUES (?)", insert.toSql());
    }

    @Test
    public void doubleParam() {
        InsertInto insert = new InsertInto("USER");
        insert.value(Bind.PARAM, "NAME", "정대만");
        insert.value(Bind.PARAM, "AGE", 32);
        assertEquals("INSERT INTO USER (NAME, AGE) VALUES (?, ?)", insert.toSql());
    }

    @Test
    public void singleValue() {
        InsertInto insert = new InsertInto("USER");
        insert.value(Bind.VALUE, "NAME", "정대만");
        insert.value(Bind.VALUE, "AGE", 32);
        assertEquals("INSERT INTO USER (NAME, AGE) VALUES ('정대만', 32)", insert.toSql());
    }

}