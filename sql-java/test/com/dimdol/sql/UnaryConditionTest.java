package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class UnaryConditionTest {

    @Test(expected = IllegalArgumentException.class)
    public void unsupportedOperator() {
        new UnaryCondition(Op.EQUAL, "NAME");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValue() {
        new UnaryCondition(Op.IS_NULL, "NAME").getValues();
    }

    @Test
    public void base() {
        UnaryCondition condition = new UnaryCondition(Op.IS_NULL, "NAME");
        assertFalse(condition.prepared());
        assertEquals("NAME IS NULL", condition.toSql());
        assertEquals("NAME IS NULL", condition.toString());
    }

}