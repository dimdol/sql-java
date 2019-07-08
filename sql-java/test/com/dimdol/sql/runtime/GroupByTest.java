package com.dimdol.sql.runtime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class GroupByTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void groupBy() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.column("POSITION");
                table.numberColumn("AGE");
                table.record("B", "윤대협", "SF", 23);
                table.record("B", "정대만", "SG", 25);
                table.record("B", "서태웅", "SF", 21);
                table.record("B", "이정환", "SG", 20);
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("POSITION");
            sql.select("MAX(AGE)");
            sql.select("MIN(AGE)");
            sql.from("XZY_USER");
            sql.groupBy("POSITION");
            sql.orderBy("POSITION");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("SF", rs.getString(1));
                    assertEquals(23, rs.getInt(2));
                    assertEquals(21, rs.getInt(3));
                } else {
                    assertEquals("SG", rs.getString(1));
                    assertEquals(25, rs.getInt(2));
                    assertEquals(20, rs.getInt(3));
                }
            });
        });
    }

    @Test
    public void multiple() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.column("POSITION");
                table.column("REGULAR");
                table.numberColumn("AGE");
                table.record("B", "윤대협", "SF", "Y", 23);
                table.record("B", "정대만", "SF", "N", 25);
                table.record("B", "서태웅", "SF", "Y", 21);
                table.record("B", "이정환", "SF", "N", 20);
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("POSITION");
            sql.select("REGULAR");
            sql.select("MAX(AGE)");
            sql.select("MIN(AGE)");
            sql.from("XZY_USER");
            sql.groupBy("POSITION", "REGULAR");
            sql.orderBy("POSITION");
            sql.orderBy("REGULAR");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("SF", rs.getString(1));
                    assertEquals("N", rs.getString(2));
                    assertEquals(25, rs.getInt(3));
                    assertEquals(20, rs.getInt(4));
                } else {
                    assertEquals("SF", rs.getString(1));
                    assertEquals("Y", rs.getString(2));
                    assertEquals(23, rs.getInt(3));
                    assertEquals(21, rs.getInt(4));
                }
            });
        });
    }

}