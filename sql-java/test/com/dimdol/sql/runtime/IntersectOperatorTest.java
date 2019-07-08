package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class IntersectOperatorTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void intersect() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("NAME");
                table.record("강백호");
                table.record("정대만");
                table.record("채치수");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.union(subquery -> {
                subquery.select("NAME");
                subquery.from("XZY_USER");
            });
            sql.intersect(subquery -> {
                subquery.select("NAME");
                subquery.from("XZY_USER");
                subquery.where("NAME", Op.EQUAL, "채치수");
            });
            sql.orderBy("NAME", Op.DESC);
            sql.each((i, rs) -> {
                assertEquals("채치수", rs.getString("NAME"));
            });
        });
    }

}