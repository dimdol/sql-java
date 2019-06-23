package com.dimdol.sql;

import org.junit.Test;

import static org.junit.Assert.*;

public class SubqueryTableTest {

    @Test
    public void base() {
        Sql<?> subquery = new Sql<>();
        subquery.selectAll();
        subquery.from("USER");
        subquery.where(Op.EQUAL, Bind.PARAM, "TYPE", "A");

        assertEquals("(SELECT * FROM USER WHERE TYPE = ?)", new SubqueryTable(subquery).toSql());
        assertEquals(1, subquery.getParameters().size());
        assertEquals("TYPE = 'A'", subquery.getParameters().get(0).toString());
    }

}