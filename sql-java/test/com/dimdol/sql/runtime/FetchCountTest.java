package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class FetchCountTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void getFetchCount() {
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
            sql.where("AGE", Op.GREATER_THAN, 22);
            sql.each((i, rs) -> {
                // do nothing
            });
            assertEquals(2, sql.getFetchCount());
        });
    }

}