package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class AliasTest {

    @Test
    public void column() {
        assertEquals(" AS A", new Alias("A").toSql());
    }

    @Test
    public void table() {
        assertEquals(" A", new Alias("A", true).toSql());
    }

    @Test
    public void quote() {
        assertEquals(" AS \"OUR CUSTOMER\"", new Alias("OUR CUSTOMER").toSql());
    }

}