package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class UnionOperatorTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void base() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("NAME");
                table.record("윤대협");
                table.record("정대만");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.union(subquery -> {
                subquery.select("NAME");
                subquery.from("XZY_USER");
                subquery.where("NAME", Op.EQUAL, "윤대협");
            });
            sql.each((i, rs) -> {
                assertEquals("윤대협", rs.getString("NAME"));
            });
        });
    }

    @Test
    public void union() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("NAME");
                table.record("정대만");
                table.record("윤대협");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.union(subquery -> {
                subquery.select("NAME");
                subquery.from("XZY_USER");
                subquery.where("NAME", Op.EQUAL, "정대만");
            });
            sql.union(subquery -> {
                subquery.select("NAME");
                subquery.from("XZY_USER");
                subquery.where("NAME", Op.EQUAL, "윤대협");
            });
            sql.orderBy("NAME", Op.DESC);
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("정대만", rs.getString("NAME"));
                } else {
                    assertEquals("윤대협", rs.getString("NAME"));
                }
            });
        });
    }

}
