package com.dimdol.sql;

import org.junit.Test;

import static org.junit.Assert.*;

public class OrderByTest {

    @Test
    public void orderBy() {
        Sql<?> sql = new Sql<>();
        sql.select("UUID");
        sql.select("FIRST_NAME");
        sql.from("USER");
        sql.orderBy("FIRST_NAME");
        assertEquals("SELECT UUID, FIRST_NAME FROM USER ORDER BY FIRST_NAME", sql.toSql());
    }

    @Test
    public void multiple() {
        Sql<?> sql = new Sql<>();
        sql.select("UUID");
        sql.select("FIRST_NAME");
        sql.from("USER");
        sql.orderBy("FIRST_NAME");
        sql.orderBy("UUID");
        assertEquals("SELECT UUID, FIRST_NAME FROM USER ORDER BY FIRST_NAME, UUID", sql.toSql());
    }

    @Test
    public void asc() {
        Sql<?> sql = new Sql<>();
        sql.select("UUID");
        sql.select("FIRST_NAME");
        sql.from("USER");
        sql.orderBy("FIRST_NAME", Op.ASC);
        assertEquals("SELECT UUID, FIRST_NAME FROM USER ORDER BY FIRST_NAME ASC", sql.toSql());
    }

    @Test
    public void desc() {
        Sql<?> sql = new Sql<>();
        sql.select("UUID");
        sql.select("FIRST_NAME");
        sql.from("USER");
        sql.orderBy("FIRST_NAME", Op.DESC);
        assertEquals("SELECT UUID, FIRST_NAME FROM USER ORDER BY FIRST_NAME DESC", sql.toSql());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void unsupportedAscendingMode() {
        Sql<?> sql = new Sql<>();
        sql.select("UUID");
        sql.select("FIRST_NAME");
        sql.from("USER");
        sql.orderBy("FIRST_NAME", Op.EQUAL);
    }

}