package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class SelectTest {

    @Test
    public void selectAll() {
        Sql sql = new Sql();
        sql.selectAll();
        sql.from("USER");
        assertEquals("SELECT * FROM USER", sql.toSql());
        assertEquals(0, sql.getParameters().size());

        sql = new Sql();
        sql.select("*");
        sql.from("USER");
        assertEquals("SELECT * FROM USER", sql.toSql());
        assertEquals(0, sql.getParameters().size());
    }

    @Test
    public void select() {
        Sql sql = new Sql();
        sql.select("NAME");
        sql.from("USER");
        assertEquals("SELECT NAME FROM USER", sql.toSql());
        assertEquals(0, sql.getParameters().size());

        sql = new Sql();
        sql.select("NAME");
        sql.select("AGE");
        sql.from("USER");
        assertEquals("SELECT NAME, AGE FROM USER", sql.toSql());
        assertEquals(0, sql.getParameters().size());
    }

    @Test
    public void selectAlias() {
        Sql sql = new Sql();
        sql.select("UUID", "ID");
        sql.select("FIRST_NAME", "NAME");
        sql.from("USER");
        assertEquals("SELECT UUID AS ID, FIRST_NAME AS NAME FROM USER", sql.toSql());
        assertEquals(0, sql.getParameters().size());
    }

    @Test
    public void distinct() {
        Sql sql = new Sql();
        sql.distinct();
        sql.select("NAME");
        sql.from("USER");
        assertEquals("SELECT DISTINCT NAME FROM USER", sql.toSql());
        assertEquals(0, sql.getParameters().size());

        sql = new Sql();
        sql.distinct();
        sql.select("NAME");
        sql.select("AGE");
        sql.from("USER");
        assertEquals("SELECT DISTINCT NAME, AGE FROM USER", sql.toSql());
        assertEquals(0, sql.getParameters().size());
    }

    @Test
    public void subquery() {
        Sql sql = new Sql();
        sql.select("NAME");
        sql.select(s -> {
            s.select("NAME");
            s.from("GROUP", "G");
            s.where("G.ID", Op.EQUAL, Bind.COLUMN, "U.ID");
        });
        sql.from("USER", "U");
        assertEquals("SELECT NAME, (SELECT NAME FROM GROUP G WHERE G.ID = U.ID) FROM USER U", sql.toSql());
        assertEquals(0, sql.getParameters().size());
    }

    @Test
    public void subqueryAlias() {
        Sql sql = new Sql();
        sql.select("NAME");
        sql.select(s -> {
            s.select("NAME");
            s.from("GROUP", "G");
            s.where("G.ID", Op.EQUAL, Bind.COLUMN, "U.ID");
        }, "GROUP_NAME");
        sql.from("USER", "U");
        assertEquals("SELECT NAME, (SELECT NAME FROM GROUP G WHERE G.ID = U.ID) AS GROUP_NAME FROM USER U", sql.toSql());
        assertEquals(0, sql.getParameters().size());
    }

}