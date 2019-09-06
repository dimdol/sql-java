package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.example.T;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class TableTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void enumTable() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("A", "정대만");
                table.record("B", "윤대협");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("COUNT(*)");
            sql.from(T.XZY_USER);
            sql.each((i, rs) -> {
                assertEquals(2, rs.getInt(1));
            });

            sql = new Sql();
            sql.select("COUNT(U.NAME)");
            sql.from(T.XZY_USER, "U");
            sql.each((i, rs) -> {
                assertEquals(2, rs.getInt(1));
            });
        });
    }

    @Test
    public void subqueryTable() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("A", "정대만");
                table.record("B", "윤대협");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("NICKNAME");
            sql.from(subquery -> {
                subquery.select("NAME", "NICKNAME");
                subquery.from("XZY_USER");
                subquery.where("NAME", Op.EQUAL, "정대만");
            });
            sql.each((i, rs) -> {
                assertEquals("정대만", rs.getString("NICKNAME"));
            });
        });
    }

}
