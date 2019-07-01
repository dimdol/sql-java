package com.dimdol.sql.runtime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Bind;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class ExistsOperatorTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void exists() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("A", "정대만");
                table.record("B", "윤대협");
            });
            loader.table("XZY_GUEST", table -> {
                table.column("NAME");
                table.record("정대만");
                table.record("송태섭");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.select("NAME");
            sql.from("XZY_USER");
            sql.where(Op.EXISTS, subquery -> {
                subquery.select("NAME");
                subquery.from("XZY_GUEST");
                subquery.where("XZY_USER.NAME", Op.EQUAL, Bind.COLUMN, "NAME");
            });
            sql.each((i, rs) -> {
                assertEquals("A", rs.getString("ID"));
                assertEquals("정대만", rs.getString("NAME"));
            });
        });
    }

    @Test
    public void notExists() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("A", "정대만");
                table.record("B", "윤대협");
            });
            loader.table("XZY_GUEST", table -> {
                table.column("NAME");
                table.record("정대만");
                table.record("송태섭");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.select("NAME");
            sql.from("XZY_USER");
            sql.where(Op.NOT_EXISTS, subquery -> {
                subquery.select("NAME");
                subquery.from("XZY_GUEST");
                subquery.where("XZY_USER.NAME", Op.EQUAL, Bind.COLUMN, "NAME");
            });
            sql.each((i, rs) -> {
                assertEquals("B", rs.getString("ID"));
                assertEquals("윤대협", rs.getString("NAME"));
            });
        });
    }

}
