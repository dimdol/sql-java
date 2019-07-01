package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class SubqueryTableTest {

    @Test
    public void base() {
        Sql subquery = new Sql();
        subquery.selectAll();
        subquery.from("USER");
        subquery.where("TYPE", Op.EQUAL, Bind.PARAM, "A");

        assertEquals("(SELECT * FROM USER WHERE TYPE = ?)", new SubqueryTable(subquery).toSql());
        assertEquals(1, subquery.getParameters().size());
        assertEquals("TYPE = 'A'", subquery.getParameters().get(0).toString());
    }

}