package com.dimdol.sql;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class UpdateTest {

    @Test
    public void update() {
        Sql sql = new Sql();
        sql.update("USER");
        sql.set("NAME", "정대만");
        sql.set("AGE", 33);
        sql.where("ID", Op.EQUAL, "A");
        assertEquals("UPDATE USER SET NAME = ?, AGE = ? WHERE ID = ?", sql.toSql());
        List<Parameter> parameters = sql.getParameters();
        assertEquals(3, parameters.size());
    }

}