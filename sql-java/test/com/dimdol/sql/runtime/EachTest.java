package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class EachTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void limit() {
        DataLoader.run(loader -> {
            loader.table("XZY_TABLE", table -> {
                table.column("ID");
                for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
                    table.record(Character.toString(alphabet));
                }
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.from("XZY_TABLE");
            sql.each((i, rs) -> {
                if (i > 4) {
                    fail();
                }
            }, 5);
        });
    }

}
