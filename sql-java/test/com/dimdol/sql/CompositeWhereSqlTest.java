package com.dimdol.sql;

import org.junit.Test;

import static org.junit.Assert.*;

public class CompositeWhereSqlTest {

    @Test
    public void or() {
        Sql<?> sql = new Sql<>();
        sql.selectAll();
        sql.from("USER");
        sql.where(Op.EQUAL, "TYPE", "A");
        sql.or(s -> {
            s.where(Op.EQUAL, "TEAM", "Sales");
            s.where(Op.EQUAL, "JOB", "Programmer");
        });
        assertEquals("SELECT * FROM USER WHERE TYPE = ? AND (TEAM = ? OR JOB = ?)", sql.toSql());
        assertEquals(3, sql.getParameters().size());
        assertEquals("TYPE = 'A'", sql.getParameters().get(0).toString());
        assertEquals("TEAM = 'Sales'", sql.getParameters().get(1).toString());
        assertEquals("JOB = 'Programmer'", sql.getParameters().get(2).toString());
    }
    
    @Test
    public void and() {
        Sql<?> sql = new Sql<>(Op.OR);
        sql.selectAll();
        sql.from("USER");
        sql.where(Op.EQUAL, "TYPE", "A");
        sql.and(s -> {
            s.where(Op.EQUAL, "TEAM", "Sales");
            s.where(Op.EQUAL, "JOB", "Programmer");
        });
        assertEquals("SELECT * FROM USER WHERE TYPE = ? OR (TEAM = ? AND JOB = ?)", sql.toSql());
        assertEquals(3, sql.getParameters().size());
        assertEquals("TYPE = 'A'", sql.getParameters().get(0).toString());
        assertEquals("TEAM = 'Sales'", sql.getParameters().get(1).toString());
        assertEquals("JOB = 'Programmer'", sql.getParameters().get(2).toString());
    }
    
    @Test
    public void not() {
        Sql<?> sql = new Sql<>(Op.OR);
        sql.selectAll();
        sql.from("USER");
        sql.where(Op.EQUAL, "TYPE", "A");
        sql.not(s -> {
            s.where(Op.EQUAL, "TEAM", "Sales");
        });
        assertEquals("SELECT * FROM USER WHERE TYPE = ? OR NOT TEAM = ?", sql.toSql());
        assertEquals(2, sql.getParameters().size());
        assertEquals("TYPE = 'A'", sql.getParameters().get(0).toString());
        assertEquals("TEAM = 'Sales'", sql.getParameters().get(1).toString());
    }
    
    @Test
    public void nested() {
        Sql<?> sql = new Sql<>();
        sql.selectAll();
        sql.from("USER");
        sql.or(s -> {
            s.and(ss -> {
                ss.where(Op.EQUAL, "TEAM", "Sales");
                ss.where(Op.EQUAL, "TYPE", "A");
            });
            s.where(Op.EQUAL, "JOB", "Programmer");
        });
        assertEquals("SELECT * FROM USER WHERE ((TEAM = ? AND TYPE = ?) OR JOB = ?)", sql.toSql());
        assertEquals(3, sql.getParameters().size());
        assertEquals("TEAM = 'Sales'", sql.getParameters().get(0).toString());
        assertEquals("TYPE = 'A'", sql.getParameters().get(1).toString());
        assertEquals("JOB = 'Programmer'", sql.getParameters().get(2).toString());
    }

}