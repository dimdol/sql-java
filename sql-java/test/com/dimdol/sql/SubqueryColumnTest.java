package com.dimdol.sql;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubqueryColumnTest {

    private Sql<?> subquery;

    @Before
    public void setUp() {
        subquery = new Sql<>();
        subquery.select("NAME");
        subquery.from("USER", "U");
        subquery.where(Op.EQUAL, Bind.COLUMN, "U.GROUP_ID", "G.ID");
    }

    @Test
    public void base() {
        assertEquals("(SELECT NAME FROM USER AS U WHERE U.GROUP_ID = G.ID)", new SubqueryColumn(subquery, null).toSql());
    }

    @Test
    public void alias() {
        assertEquals("(SELECT NAME FROM USER AS U WHERE U.GROUP_ID = G.ID) AS GROUP_NAME", new SubqueryColumn(subquery, "GROUP_NAME").toSql());
    }

}