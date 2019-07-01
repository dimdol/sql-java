package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SubqueryColumnTest {

    private Sql subquery;

    @Before
    public void setUp() {
        subquery = new Sql();
        subquery.select("NAME");
        subquery.from("USER", "U");
        subquery.where("U.GROUP_ID", Op.EQUAL, Bind.COLUMN, "G.ID");
    }

    @Test
    public void base() {
        assertEquals("(SELECT NAME FROM USER U WHERE U.GROUP_ID = G.ID)", new SubqueryColumn(subquery, null).toSql());
    }

    @Test
    public void alias() {
        assertEquals("(SELECT NAME FROM USER U WHERE U.GROUP_ID = G.ID) AS GROUP_NAME", new SubqueryColumn(subquery, "GROUP_NAME").toSql());
    }

}