package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class SetSqlTest {

    @Test
    public void single() {
        Sql sql = new Sql();
        sql.union(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("USER");
        });
        assertEquals("SELECT ID, NAME FROM USER", sql.toSql());
    }

    @Test
    public void union() {
        Sql sql = new Sql();
        sql.union(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("USER");
        });
        sql.union(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("GROUP");
        });
        assertEquals("(SELECT ID, NAME FROM USER UNION SELECT ID, NAME FROM GROUP)", sql.toSql());
    }

    @Test
    public void unionAll() {
        Sql sql = new Sql();
        sql.unionAll(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("USER");
        });
        sql.unionAll(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("GROUP");
        });
        assertEquals("(SELECT ID, NAME FROM USER UNION ALL SELECT ID, NAME FROM GROUP)", sql.toSql());
    }

    @Test
    public void except() {
        Sql sql = new Sql();
        sql.except(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("USER");
        });
        sql.except(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("GROUP");
        });
        assertEquals("(SELECT ID, NAME FROM USER EXCEPT SELECT ID, NAME FROM GROUP)", sql.toSql());
    }

    @Test
    public void intersect() {
        Sql sql = new Sql();
        sql.intersect(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("USER");
        });
        sql.intersect(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("GROUP");
        });
        assertEquals("(SELECT ID, NAME FROM USER INTERSECT SELECT ID, NAME FROM GROUP)", sql.toSql());
    }

    @Test
    public void all() {
        Sql sql = new Sql();
        sql.union(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("A");
        });
        sql.union(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("B");
        });
        sql.unionAll(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("C");
        });
        sql.except(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("D");
        });
        sql.intersect(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("E");
        });
        assertEquals("(SELECT ID, NAME FROM A UNION SELECT ID, NAME FROM B UNION ALL SELECT ID, NAME FROM C EXCEPT SELECT ID, NAME FROM D INTERSECT SELECT ID, NAME FROM E)", sql.toSql());
    }

    @Test
    public void nexted() {
        Sql sql = new Sql();
        sql.union(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("A");
        });
        sql.unionAll(s -> {
            s.union(n -> {
                n.select("ID");
                n.select("NAME");
                n.from("B");
            });
            s.except(n -> {
                n.select("ID");
                n.select("NAME");
                n.from("C");
            });
        });
        sql.intersect(s -> {
            s.select("ID");
            s.select("NAME");
            s.from("D");
        });
        assertEquals("(SELECT ID, NAME FROM A UNION ALL (SELECT ID, NAME FROM B EXCEPT SELECT ID, NAME FROM C) INTERSECT SELECT ID, NAME FROM D)", sql.toSql());
    }

}