package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class InsertTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void insert() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.numberColumn("AGE");
                table.record("A", "정대만", 23);
                table.record("B", "윤대협", 22);
            });
        }, () -> {
            Sql sql = new Sql();
            sql.insertInto("XZY_USER");
            sql.value("ID", "C");
            sql.value("NAME", "강백호");
            sql.value("AGE", 21);
            assertEquals(1, sql.execute());

            sql = new Sql();
            sql.select("NAME");
            sql.select("AGE");
            sql.from("XZY_USER");
            sql.where("ID", Op.EQUAL, "C");
            sql.each((i, rs) -> {
                assertEquals("강백호", rs.getString("NAME"));
                assertEquals(21, rs.getInt("AGE"));
            });
        });
    }

}
