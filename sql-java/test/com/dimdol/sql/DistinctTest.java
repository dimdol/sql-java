package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistinctTest {

    @Test
    public void distinct() {
        Sql sql = new Sql();
        sql.select("NAME");
        sql.from("USER");
        assertEquals("SELECT NAME FROM USER", sql.toSql());

        sql = new Sql(Option.DISTINCT);
        sql.select("NAME");
        sql.from("USER");
        assertEquals("SELECT DISTINCT NAME FROM USER", sql.toSql());
    }

}