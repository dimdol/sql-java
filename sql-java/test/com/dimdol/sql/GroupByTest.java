package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class GroupByTest {

    @Test
    public void base() {
        GroupBy groupBy = new GroupBy("USER");
        assertEquals("GROUP BY USER", groupBy.toSql());

        groupBy = new GroupBy("USER", "TYPE");
        assertEquals("GROUP BY USER, TYPE", groupBy.toSql());
    }

    @Test
    public void orderBy() {
        Sql sql = new Sql();
        sql.select("TYPE");
        sql.select("MIN(AGE)");
        sql.from("USER");
        sql.groupBy("TYPE");
        sql.orderBy("TYPE");
        assertEquals("SELECT TYPE, MIN(AGE) FROM USER GROUP BY TYPE ORDER BY TYPE", sql.toSql());
    }

    @Test
    public void multiple() {
        Sql sql = new Sql();
        sql.select("TYPE");
        sql.select("LOCATION");
        sql.select("MIN(AGE)");
        sql.from("USER");
        sql.groupBy("TYPE", "LOCATION");
        sql.orderBy("TYPE");
        assertEquals("SELECT TYPE, LOCATION, MIN(AGE) FROM USER GROUP BY TYPE, LOCATION ORDER BY TYPE", sql.toSql());
    }

}