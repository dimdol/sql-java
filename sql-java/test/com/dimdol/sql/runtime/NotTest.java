package com.dimdol.sql.runtime;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class NotTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void not() {
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
            sql.not(not -> {
                not.where("NAME", Op.EQUAL, "윤대협");
            });
            sql.each((i, rs) -> {
                assertEquals("정대만", rs.getString("NAME"));
            });
        });
    }

    @Test
    public void notWithAnd() {
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
            sql.not(not -> {
                not.and(and -> {
                    and.where("ID", Op.EQUAL, "A");
                    and.where("NAME", Op.EQUAL, "윤대협");
                });
            });
            sql.orderBy("NAME");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("윤대협", rs.getString("NAME"));
                } else {
                    assertEquals("정대만", rs.getString("NAME"));
                }
            });
        });
    }

    @Test
    public void notWithOr() {
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
            sql.not(not -> {
                not.or(or -> {
                    or.where("ID", Op.EQUAL, "A");
                    or.where("NAME", Op.EQUAL, "윤대협");
                });
            });
            sql.each((i, rs) -> {
                fail();
            });
        });
    }

}
