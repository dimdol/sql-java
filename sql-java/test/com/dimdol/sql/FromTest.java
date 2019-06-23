package com.dimdol.sql;

import org.junit.Test;

import static org.junit.Assert.*;

public class FromTest {

    @Test
    public void from() {
        Sql<?> sql = new Sql<>();
        sql.select("UUID");
        sql.select("FIRST_NAME");
        sql.from("USER");
        assertEquals("SELECT UUID, FIRST_NAME FROM USER", sql.toSql());
    }

    @Test
    public void alias() {
        Sql<?> sql = new Sql<>();
        sql.select("UUID");
        sql.select("FIRST_NAME");
        sql.from("USER", "U");
        assertEquals("SELECT UUID, FIRST_NAME FROM USER AS U", sql.toSql());
    }

    @Test
    public void subquery() {
        Sql<?> sql = new Sql<>();
        sql.selectAll();
        sql.from(s -> {
            s.selectAll();
            s.from("ACCOUNT");
            s.where(Op.EQUAL, Bind.PARAM, "TYPE", "A");
        });
        sql.where(Op.EQUAL, Bind.PARAM, "NAME", "Lee");
        assertEquals("SELECT * FROM (SELECT * FROM ACCOUNT WHERE TYPE = ?) WHERE NAME = ?", sql.toSql());
    }

    @Test
    public void subqueryAlias() {
        Sql<?> sql = new Sql<>();
        sql.selectAll();
        sql.from(s -> {
            s.selectAll();
            s.from("ACCOUNT");
            s.where(Op.EQUAL, Bind.PARAM, "TYPE", "A");
        }, "A");
        sql.where(Op.EQUAL, Bind.PARAM, "NAME", "Lee");
        assertEquals("SELECT * FROM (SELECT * FROM ACCOUNT WHERE TYPE = ?) AS A WHERE NAME = ?", sql.toSql());
    }

}