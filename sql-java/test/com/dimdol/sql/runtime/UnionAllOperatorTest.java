package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class UnionAllOperatorTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void unionAll() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("NAME");
                table.record("강백호");
                table.record("서태웅");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.unionAll(subquery -> {
                subquery.select("NAME");
                subquery.from("XZY_USER");
                subquery.where("NAME", Op.EQUAL, "강백호");
            });
            sql.unionAll(subquery -> {
                subquery.select("NAME");
                subquery.from("XZY_USER");
            });
            sql.orderBy("NAME");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("강백호", rs.getString("NAME"));
                } else if (i == 1) {
                    assertEquals("강백호", rs.getString("NAME"));
                } else {
                    assertEquals("서태웅", rs.getString("NAME"));
                }
            });
        });
    }

}
