package com.dimdol.sql;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class DeleteTest {

    @Test
    public void delete() {
        Sql sql = new Sql();
        sql.deleteFrom("USER");
        sql.where("ID", Op.EQUAL, "A");
        assertEquals("DELETE FROM USER WHERE ID = ?", sql.toSql());
        List<Parameter> parameters = sql.getParameters();
        assertEquals(1, parameters.size());
    }

}