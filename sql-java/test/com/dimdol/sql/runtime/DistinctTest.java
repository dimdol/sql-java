package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Option;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class DistinctTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void distinct() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.numberColumn("AGE");
                table.record("A", "정대만", 20);
                table.record("B", "윤대협", 21);
                table.record("C", "정대만", 22);
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("NAME");
            sql.from("XZY_USER");
            sql.each((i, rs) -> {
            });
            assertEquals(3, sql.getFetchCount());

            sql = new Sql(Option.DISTINCT);
            sql.select("NAME");
            sql.from("XZY_USER");
            sql.each((i, rs) -> {
            });
            assertEquals(2, sql.getFetchCount());
        });
    }

}
