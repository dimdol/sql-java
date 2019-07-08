package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class BetweenOperatorTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void betweenCharacter() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("A", "정대만");
                table.record("B", "윤대협");
                table.record("C", "서태웅");
                table.record("D", "강백호");
                table.record("E", "채치수");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.select("NAME");
            sql.from("XZY_USER");
            sql.where("ID", Op.BETWEEN, "C", "D");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("C", rs.getString("ID"));
                    assertEquals("서태웅", rs.getString("NAME"));
                } else {
                    assertEquals("D", rs.getString("ID"));
                    assertEquals("강백호", rs.getString("NAME"));
                }
            });
        });
    }

    @Test
    public void notBetweenCharacter() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("A", "정대만");
                table.record("B", "윤대협");
                table.record("C", "서태웅");
                table.record("D", "강백호");
                table.record("E", "채치수");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.select("NAME");
            sql.from("XZY_USER");
            sql.where("ID", Op.NOT_BETWEEN, "B", "D");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("A", rs.getString("ID"));
                    assertEquals("정대만", rs.getString("NAME"));
                } else {
                    assertEquals("E", rs.getString("ID"));
                    assertEquals("채치수", rs.getString("NAME"));
                }
            });
        });
    }

}
