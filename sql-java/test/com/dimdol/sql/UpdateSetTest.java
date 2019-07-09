package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class UpdateSetTest {

    @Test
    public void singleParam() {
        UpdateSet update = new UpdateSet("USER");
        update.set(Bind.PARAM, "NAME", "정대만");
        assertEquals("UPDATE USER SET NAME = ?", update.toSql());
    }

    @Test
    public void doubleParam() {
        UpdateSet update = new UpdateSet("USER");
        update.set(Bind.PARAM, "NAME", "정대만");
        update.set(Bind.PARAM, "AGE", 32);
        assertEquals("UPDATE USER SET NAME = ?, AGE = ?", update.toSql());
    }

    @Test
    public void singleColumn() {
        UpdateSet update = new UpdateSet("USER");
        update.set(Bind.COLUMN, "NAME", "NICKNAME");
        assertEquals("UPDATE USER SET NAME = NICKNAME", update.toSql());
    }

    @Test
    public void singleValue() {
        UpdateSet update = new UpdateSet("USER");
        update.set(Bind.VALUE, "NAME", "정대만");
        assertEquals("UPDATE USER SET NAME = '정대만'", update.toSql());
    }

}