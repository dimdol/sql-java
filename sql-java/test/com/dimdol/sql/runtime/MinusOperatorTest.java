package com.dimdol.sql.runtime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class MinusOperatorTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void minus() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("NAME");
                table.record("강백호");
                table.record("정대만");
                table.record("채치수");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.minus(subquery -> {
                subquery.select("NAME");
                subquery.from("XZY_USER");
            });
            sql.minus(subquery -> {
                subquery.select("NAME");
                subquery.from("XZY_USER");
                subquery.where("NAME", Op.EQUAL, "채치수");
            });
            sql.orderBy("NAME");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("강백호", rs.getString("NAME"));
                } else if (i == 1) {
                    assertEquals("정대만", rs.getString("NAME"));
                }
            });
        });
    }

}