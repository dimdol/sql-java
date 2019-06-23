package com.dimdol.sql;

import org.junit.Test;

import static org.junit.Assert.*;

public class AliasTest {

    @Test
    public void base() {
        assertEquals(" AS A", new Alias("A").toSql());
    }

    @Test
    public void quote() {
        assertEquals(" AS \"OUR CUSTOMER\"", new Alias("OUR CUSTOMER").toSql());
    }

}