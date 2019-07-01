package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class FromTest {

    @Test
    public void from() {
        Sql sql = new Sql();
        sql.select("UUID");
        sql.select("FIRST_NAME");
        sql.from("USER");
        assertEquals("SELECT UUID, FIRST_NAME FROM USER", sql.toSql());
    }

    @Test
    public void alias() {
        Sql sql = new Sql();
        sql.select("UUID");
        sql.select("FIRST_NAME");
        sql.from("USER", "U");
        assertEquals("SELECT UUID, FIRST_NAME FROM USER U", sql.toSql());
    }

    @Test
    public void subquery() {
        Sql sql = new Sql();
        sql.selectAll();
        sql.from(s -> {
            s.selectAll();
            s.from("ACCOUNT");
            s.where("TYPE", Op.EQUAL, Bind.PARAM, "A");
        });
        sql.where("NAME", Op.EQUAL, Bind.PARAM, "Lee");
        assertEquals("SELECT * FROM (SELECT * FROM ACCOUNT WHERE TYPE = ?) WHERE NAME = ?", sql.toSql());
    }

    @Test
    public void subqueryAlias() {
        Sql sql = new Sql();
        sql.selectAll();
        sql.from(s -> {
            s.selectAll();
            s.from("ACCOUNT");
            s.where("TYPE", Op.EQUAL, Bind.PARAM, "A");
        }, "A");
        sql.where("NAME", Op.EQUAL, Bind.PARAM, "Lee");
        assertEquals("SELECT * FROM (SELECT * FROM ACCOUNT WHERE TYPE = ?) A WHERE NAME = ?", sql.toSql());
    }

}