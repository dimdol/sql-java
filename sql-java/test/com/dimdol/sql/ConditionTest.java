package com.dimdol.sql;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConditionTest {

    @Test
    public void param() {
        assertEquals("NAME = ?", new StringCondition(Op.EQUAL, Bind.PARAM, "NAME", "Lee").toSql());
    }

    @Test
    public void value() {
        assertEquals("NAME = 'Lee'", new StringCondition(Op.EQUAL, Bind.VALUE, "NAME", "Lee").toSql());
    }

    @Test
    public void column() {
        assertEquals("NAME = FIRST_NAME", new StringCondition(Op.EQUAL, Bind.COLUMN, "NAME", "FIRST_NAME").toSql());
    }

}