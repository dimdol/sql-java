package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class AndTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void and() {
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
            sql.where("NAME", Op.EQUAL, "정대만");
            sql.where("AGE", Op.EQUAL, 20);
            sql.orderBy("NAME");
            sql.each((i, rs) -> {
                assertEquals("정대만", rs.getString("NAME"));
            });
        });
    }

    @Test
    public void compositeAnd() {
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
            sql.and(and -> {
                and.where("NAME", Op.EQUAL, "정대만");
                and.where("AGE", Op.EQUAL, 20);
            });
            sql.orderBy("NAME");
            sql.each((i, rs) -> {
                assertEquals("정대만", rs.getString("NAME"));
            });
        });
    }

    @Test
    public void andOr() {
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
            sql.and(and -> {
                and.where("NAME", Op.EQUAL, "윤대협");
                and.or(or -> {
                    or.where("AGE", Op.EQUAL, 20);
                    or.where("AGE", Op.EQUAL, 21);
                });
            });
            sql.orderBy("NAME");
            sql.each((i, rs) -> {
                assertEquals("윤대협", rs.getString("NAME"));
            });
        });
    }

}
