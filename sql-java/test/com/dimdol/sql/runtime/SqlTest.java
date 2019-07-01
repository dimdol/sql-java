package com.dimdol.sql.runtime;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class SqlTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void base() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("A", "정대만");
                table.record("B", "윤대협");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.select("NAME");
            sql.from("XZY_USER");
            sql.where("NAME", Op.EQUAL, "정대만");
            sql.orderBy("NAME", Op.DESC);
            sql.each((i, rs) -> {
                System.out.printf("%s %s\n", rs.getString("ID"), rs.getString("NAME"));
            });
        });

    }

}