package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class OrTest {

    @Test
    public void byOption() {
        Sql sql = new Sql(Option.OR);
        sql.select("NAME");
        sql.from("USER");
        sql.where("AGE", Op.GREATER_THAN, 60);
        sql.where("AGE", Op.LESS_THAN, 10);
        assertEquals("SELECT NAME FROM USER WHERE AGE > ? OR AGE < ?", sql.toSql());
    }

}