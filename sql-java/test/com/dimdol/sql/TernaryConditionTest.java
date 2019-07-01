package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class TernaryConditionTest {

    @Test(expected = IllegalArgumentException.class)
    public void unsupportedOperator() {
        new TernaryCondition(Op.EQUAL, Bind.PARAM, "AGE", 10, 19);
    }

    @Test(expected = IllegalArgumentException.class)
    public void unsupportedBind() {
        new TernaryCondition(Op.BETWEEN, Bind.COLUMN, "AGE", 10, 19);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValue() {
        new UnaryCondition(Op.IS_NULL, "NAME").getValues();
    }

    @Test
    public void numberParam() {
        TernaryCondition condition = new TernaryCondition(Op.BETWEEN, Bind.PARAM, "AGE", 10, 19);
        assertTrue(condition.prepared());
        assertEquals("AGE BETWEEN ? AND ?", condition.toSql());
        assertEquals("AGE BETWEEN 10 AND 19", condition.toString());
    }

    @Test
    public void numberValue() {
        TernaryCondition condition = new TernaryCondition(Op.BETWEEN, Bind.VALUE, "AGE", 10, 19);
        assertFalse(condition.prepared());
        assertEquals("AGE BETWEEN 10 AND 19", condition.toSql());
        assertEquals("AGE BETWEEN 10 AND 19", condition.toString());
    }

    @Test
    public void characterParam() {
        TernaryCondition condition = new TernaryCondition(Op.BETWEEN, Bind.PARAM, "DATE", "20190101", "20190131");
        assertTrue(condition.prepared());
        assertEquals("DATE BETWEEN ? AND ?", condition.toSql());
        assertEquals("DATE BETWEEN '20190101' AND '20190131'", condition.toString());
    }

    @Test
    public void characterValue() {
        TernaryCondition condition = new TernaryCondition(Op.BETWEEN, Bind.VALUE, "DATE", "20190101", "20190131");
        assertFalse(condition.prepared());
        assertEquals("DATE BETWEEN '20190101' AND '20190131'", condition.toSql());
        assertEquals("DATE BETWEEN '20190101' AND '20190131'", condition.toString());
    }

}