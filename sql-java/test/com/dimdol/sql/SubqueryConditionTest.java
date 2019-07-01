package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SubqueryConditionTest {

    private Sql subquery;

    @Before
    public void setUp() {
        subquery = new Sql();
        subquery.select("NAME");
        subquery.from("USER");
        subquery.where("ID", Op.EQUAL, "A001");
    }

    @Test
    public void equal() {
        assertEquals("NAME = (SELECT NAME FROM USER WHERE ID = ?)", new SubqueryCondition(Op.EQUAL, subquery, "NAME").toSql());
    }

    @Test
    public void exists() {
        assertEquals("EXISTS (SELECT NAME FROM USER WHERE ID = ?)", new SubqueryCondition(Op.EXISTS, subquery).toSql());
    }

    @Test
    public void notExists() {
        assertEquals("NOT EXISTS (SELECT NAME FROM USER WHERE ID = ?)", new SubqueryCondition(Op.NOT_EXISTS, subquery).toSql());
    }

    @Test
    public void all() {
        assertEquals("NAME > ALL (SELECT NAME FROM USER WHERE ID = ?)", new SubqueryCondition("NAME", Op.GREATER_THAN, Op.ALL, subquery).toSql());
    }

    @Test
    public void any() {
        assertEquals("NAME > ANY (SELECT NAME FROM USER WHERE ID = ?)", new SubqueryCondition("NAME", Op.GREATER_THAN, Op.ANY, subquery).toSql());
    }

    @Test
    public void some() {
        assertEquals("NAME > SOME (SELECT NAME FROM USER WHERE ID = ?)", new SubqueryCondition("NAME", Op.GREATER_THAN, Op.SOME, subquery).toSql());
    }

}