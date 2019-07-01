package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class WhereSqlTest {

    @Test
    public void equal() {
        Sql sql = new Sql();
        sql.selectAll();
        sql.from("USER");
        sql.where("NAME", Op.EQUAL, Bind.VALUE, "Lee");
        assertEquals("SELECT * FROM USER WHERE NAME = 'Lee'", sql.toSql());
        assertEquals(0, sql.getParameters().size());
    }

    @Test
    public void subqueryEqual() {
        Sql sql = new Sql();
        sql.selectAll();
        sql.from("USER", "U");
        sql.where("NAME", Op.EQUAL, "Lee");
        sql.where("GROUP_ID", Op.EQUAL, s -> {
            s.select("ID");
            s.from("GROUP", "G");
            s.where("G.NAME", Op.EQUAL, "Sales");
        });
        assertEquals("SELECT * FROM USER U WHERE NAME = ? AND GROUP_ID = (SELECT ID FROM GROUP G WHERE G.NAME = ?)", sql.toSql());
        assertEquals(2, sql.getParameters().size());
        assertEquals("NAME = 'Lee'", sql.getParameters().get(0).toString());
        assertEquals("G.NAME = 'Sales'", sql.getParameters().get(1).toString());
    }

    @Test
    public void exists() {
        Sql sql = new Sql();
        sql.selectAll();
        sql.from("USER", "U");
        sql.where("NAME", Op.EQUAL, Bind.VALUE, "Lee");
        sql.where(Op.EXISTS, s -> {
            s.selectAll();
            s.from("GROUP", "G");
            s.where("G.ID", Op.EQUAL, Bind.COLUMN, "U.GROUP_ID");
        });
        assertEquals("SELECT * FROM USER U WHERE NAME = 'Lee' AND EXISTS (SELECT * FROM GROUP G WHERE G.ID = U.GROUP_ID)", sql.toSql());
        assertEquals(0, sql.getParameters().size());
    }

    @Test
    public void notExists() {
        Sql sql = new Sql();
        sql.selectAll();
        sql.from("USER", "U");
        sql.where("NAME", Op.EQUAL, Bind.VALUE, "Lee");
        sql.where(Op.NOT_EXISTS, s -> {
            s.selectAll();
            s.from("GROUP", "G");
            s.where("G.ID", Op.EQUAL, Bind.COLUMN, "U.GROUP_ID");
        });
        assertEquals("SELECT * FROM USER U WHERE NAME = 'Lee' AND NOT EXISTS (SELECT * FROM GROUP G WHERE G.ID = U.GROUP_ID)", sql.toSql());
        assertEquals(0, sql.getParameters().size());
    }

}