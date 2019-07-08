package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class AliasTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void column() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("A", "정대만");
                table.record("B", "윤대협");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("NAME", "NICKNAME");
            sql.from("XZY_USER");
            sql.where("NAME", Op.EQUAL, "윤대협");
            sql.each((i, rs) -> {
                assertEquals("윤대협", rs.getString("NICKNAME"));
            });
        });
    }

    @Test
    public void table() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("A", "정대만");
                table.record("B", "윤대협");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("NAME");
            sql.from("XZY_USER", "A");
            sql.where("A.NAME", Op.EQUAL, "윤대협");
            sql.each((i, rs) -> {
                assertEquals("윤대협", rs.getString("NAME"));
            });
        });
    }

}
