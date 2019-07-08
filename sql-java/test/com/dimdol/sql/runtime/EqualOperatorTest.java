package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class EqualOperatorTest {

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
            sql.select("NAME");
            sql.from("XZY_USER");
            sql.where("NAME", Op.EQUAL, "윤대협");
            sql.each((i, rs) -> {
                assertEquals("윤대협", rs.getString("NAME"));
            });
        });
    }

    @Test
    public void subquery() {
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
            sql.from("XZY_USER");
            sql.where("ID", Op.EQUAL, subquery -> {
                subquery.select("ID");
                subquery.from("XZY_USER");
                subquery.where("NAME", Op.EQUAL, "정대만");
            });
            sql.each((i, rs) -> {
                assertEquals("정대만", rs.getString("NAME"));
            });
        });
    }

    @Test
    public void twoColumnSubquery() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("A", "정대만");
                table.record("A", "윤대협");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("NAME");
            sql.from("XZY_USER");
            sql.where("(ID, NAME)", Op.EQUAL, subquery -> {
                subquery.select("ID");
                subquery.select("NAME");
                subquery.from("XZY_USER");
                subquery.where("ID", Op.EQUAL, "A");
                subquery.where("NAME", Op.EQUAL, "정대만");
            });
            sql.each((i, rs) -> {
                assertEquals("정대만", rs.getString("NAME"));
            });
        });
    }

}
