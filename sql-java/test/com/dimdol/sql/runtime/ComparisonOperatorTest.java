package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class ComparisonOperatorTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void equal() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("A", "정대만");
                table.record("B", "윤대협");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.select("NAME");
            sql.from("XZY_USER");
            sql.where("NAME", Op.EQUAL, "정대만");
            sql.each((i, rs) -> {
                assertEquals("A", rs.getString("ID"));
                assertEquals("정대만", rs.getString("NAME"));
            });
        });
    }

    @Test
    public void notEqual() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("A", "정대만");
                table.record("B", "윤대협");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.select("NAME");
            sql.from("XZY_USER");
            sql.where("NAME", Op.NOT_EQUAL, "정대만");
            sql.each((i, rs) -> {
                assertEquals("B", rs.getString("ID"));
                assertEquals("윤대협", rs.getString("NAME"));
            });
        });
    }

}
