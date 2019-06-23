package com.dimdol.sql;

import org.junit.Test;

import static org.junit.Assert.*;

public class WhereSqlTest {

    @Test
    public void equal() {
        Sql<?> sql = new Sql<>();
        sql.selectAll();
        sql.from("USER");
        sql.where(Op.EQUAL, Bind.VALUE, "NAME", "Lee");
        assertEquals("SELECT * FROM USER WHERE NAME = 'Lee'", sql.toSql());
        assertEquals(0, sql.getParameters().size());
    }

    @Test
    public void subqueryEqual() {
        Sql<?> sql = new Sql<>();
        sql.selectAll();
        sql.from("USER", "U");
        sql.where(Op.EQUAL, "NAME", "Lee");
        sql.where(Op.EQUAL, s -> {
            s.select("ID");
            s.from("GROUP", "G");
            s.where(Op.EQUAL, "G.NAME", "Sales");
        }, "GROUP_ID");
        assertEquals("SELECT * FROM USER AS U WHERE NAME = ? AND GROUP_ID = (SELECT ID FROM GROUP AS G WHERE G.NAME = ?)", sql.toSql());
        assertEquals(2, sql.getParameters().size());
        assertEquals("NAME = 'Lee'", sql.getParameters().get(0).toString());
        assertEquals("G.NAME = 'Sales'", sql.getParameters().get(1).toString());
    }

    @Test
    public void exists() {
        Sql<?> sql = new Sql<>();
        sql.selectAll();
        sql.from("USER", "U");
        sql.where(Op.EQUAL, Bind.VALUE, "NAME", "Lee");
        sql.where(Op.EXISTS, s -> {
            s.selectAll();
            s.from("GROUP", "G");
            s.where(Op.EQUAL, Bind.COLUMN, "G.ID", "U.GROUP_ID");
        });
        assertEquals("SELECT * FROM USER AS U WHERE NAME = 'Lee' AND EXISTS (SELECT * FROM GROUP AS G WHERE G.ID = U.GROUP_ID)", sql.toSql());
        assertEquals(0, sql.getParameters().size());
    }

    @Test
    public void notExists() {
        Sql<?> sql = new Sql<>();
        sql.selectAll();
        sql.from("USER", "U");
        sql.where(Op.EQUAL, Bind.VALUE, "NAME", "Lee");
        sql.where(Op.NOT_EXISTS, s -> {
            s.selectAll();
            s.from("GROUP", "G");
            s.where(Op.EQUAL, Bind.COLUMN, "G.ID", "U.GROUP_ID");
        });
        assertEquals("SELECT * FROM USER AS U WHERE NAME = 'Lee' AND NOT EXISTS (SELECT * FROM GROUP AS G WHERE G.ID = U.GROUP_ID)", sql.toSql());
        assertEquals(0, sql.getParameters().size());
    }

}