package com.dimdol.sql;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubqueryConditionTest {

    private Sql<?> subquery;

    @Before
    public void setUp() {
        subquery = new Sql<>();
        subquery.select("NAME");
        subquery.from("USER");
        subquery.where(Op.EQUAL, "ID", "A001");
    }

    @Test
    public void equal() {
        assertEquals("NAME = (SELECT NAME FROM USER WHERE ID = ?)", new SubqueryCondtion(Op.EQUAL, subquery, "NAME").toSql());
    }

    @Test
    public void exists() {
        assertEquals("EXISTS (SELECT NAME FROM USER WHERE ID = ?)", new SubqueryCondtion(Op.EXISTS, subquery).toSql());
    }

    @Test
    public void notExists() {
        assertEquals("NOT EXISTS (SELECT NAME FROM USER WHERE ID = ?)", new SubqueryCondtion(Op.NOT_EXISTS, subquery).toSql());
    }

}