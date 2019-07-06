package com.dimdol.sql.runtime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class OrTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void or() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.numberColumn("AGE");
                table.record("A", "정대만", 20);
                table.record("B", "윤대협", 21);
                table.record("C", "서태웅", 22);
                table.record("D", "강백호", 23);
                table.record("E", "채치수", 24);
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.select("NAME");
            sql.from("XZY_USER");
            sql.or(or -> {
                or.where("NAME", Op.EQUAL, "정대만");
                or.where("AGE", Op.EQUAL, 22);
            });
            sql.orderBy("NAME");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("서태웅", rs.getString("NAME"));
                } else {
                    assertEquals("정대만", rs.getString("NAME"));
                }
            });
        });
    }

    @Test
    public void orAnd() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.numberColumn("AGE");
                table.record("A", "정대만", 20);
                table.record("B", "윤대협", 21);
                table.record("C", "서태웅", 22);
                table.record("D", "강백호", 23);
                table.record("E", "채치수", 24);
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.select("NAME");
            sql.from("XZY_USER");
            sql.or(or -> {
                or.where("NAME", Op.EQUAL, "정대만");
                or.and(and -> {
                    and.where("NAME", Op.EQUAL, "서태웅");
                    and.where("AGE", Op.EQUAL, 21);
                });
            });
            sql.orderBy("NAME");
            sql.each((i, rs) -> {
                assertEquals("정대만", rs.getString("NAME"));
            });
        });
    }

}
