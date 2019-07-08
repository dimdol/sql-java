package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class JoinTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void innerJoin() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.column("GROUP_ID");
                table.record("A", "정대만", "G1");
                table.record("B", "윤대협", "G1");
                table.record("C", "서태웅", "G2");
                table.record("D", "강백호", "G3");
                table.record("E", "채치수", "G3");
            });
            loader.table("XZY_GROUP", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("G1", "그룹 1");
                table.record("G2", "그룹 2");
                table.record("G3", "그룹 3");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("G.NAME", "GROUP_NAME");
            sql.from("XZY_USER", "U");
            sql.join("XZY_GROUP", "G", "U.GROUP_ID", "G.ID");
            sql.where("U.NAME", Op.EQUAL, "서태웅");
            sql.each((i, rs) -> {
                assertEquals("그룹 2", rs.getString("GROUP_NAME"));
            });
        });
    }

    @Test
    public void fullJoin() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.column("GROUP_ID");
                table.record("A", "정대만", "G1");
                table.record("B", "윤대협", "G3");
            });
            loader.table("XZY_GROUP", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("G1", "그룹 1");
                table.record("G2", "그룹 2");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("U.ID");
            sql.select("G.NAME", "GROUP_NAME");
            sql.from("XZY_USER", "U");
            sql.fullJoin("XZY_GROUP", "G", "U.GROUP_ID", "G.ID");
            sql.orderBy("ID");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("A", rs.getString("ID"));
                    assertEquals("그룹 1", rs.getString("GROUP_NAME"));
                } else if (i == 1) {
                    assertEquals("B", rs.getString("ID"));
                    assertNull(rs.getString("GROUP_NAME"));
                } else {
                    assertNull(rs.getString("ID"));
                    assertEquals("그룹 2", rs.getString("GROUP_NAME"));
                }
            });
        });
    }

    @Test
    public void leftJoin() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.column("GROUP_ID");
                table.record("A", "정대만", "G1");
                table.record("B", "윤대협", "G3");
            });
            loader.table("XZY_GROUP", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("G1", "그룹 1");
                table.record("G2", "그룹 2");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("U.ID");
            sql.select("G.NAME", "GROUP_NAME");
            sql.from("XZY_USER", "U");
            sql.leftJoin("XZY_GROUP", "G", "U.GROUP_ID", "G.ID");
            sql.orderBy("ID");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("A", rs.getString("ID"));
                    assertEquals("그룹 1", rs.getString("GROUP_NAME"));
                } else {
                    assertEquals("B", rs.getString("ID"));
                    assertNull(rs.getString("GROUP_NAME"));
                }
            });
        });
    }

    @Test
    public void rightJoin() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.column("GROUP_ID");
                table.record("A", "정대만", "G1");
                table.record("B", "윤대협", "G3");
            });
            loader.table("XZY_GROUP", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("G1", "그룹 1");
                table.record("G2", "그룹 2");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("U.ID");
            sql.select("G.NAME", "GROUP_NAME");
            sql.from("XZY_USER", "U");
            sql.rightJoin("XZY_GROUP", "G", "U.GROUP_ID", "G.ID");
            sql.orderBy("ID");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("A", rs.getString("ID"));
                    assertEquals("그룹 1", rs.getString("GROUP_NAME"));
                } else {
                    assertNull(rs.getString("ID"));
                    assertEquals("그룹 2", rs.getString("GROUP_NAME"));
                }
            });
        });
    }

}
