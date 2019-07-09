package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class DeleteFromTest {

    @Test
    public void deleteFrom() {
        DeleteFrom deleteFrom = new DeleteFrom("USER");
        assertEquals("DELETE FROM USER", deleteFrom.toSql());
    }

}