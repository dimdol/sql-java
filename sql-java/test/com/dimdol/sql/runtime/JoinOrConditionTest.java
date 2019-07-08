package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class JoinOrConditionTest {

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
                table.column("GROUP_SUB_ID");
                table.record("A", "정대만", "G1", "S2");
                table.record("B", "윤대협", "G1", "S1");
            });
            loader.table("XZY_GROUP", table -> {
                table.column("ID");
                table.column("SUB_ID");
                table.column("NAME");
                table.record("G1", "S1", "그룹 1");
                table.record("G2", "S2", "그룹 2");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("U.NAME", "USER_NAME");
            sql.select("G.NAME", "GROUP_NAME");
            sql.from("XZY_USER", "U");
            sql.join("XZY_GROUP", "G", on -> {
                on.or("U.GROUP_ID", "G.ID");
                on.or("U.GROUP_SUB_ID", "G.SUB_ID");
            });
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("정대만", rs.getString("USER_NAME"));
                } else if (i == 1) {
                    assertEquals("정대만", rs.getString("USER_NAME"));
                } else {
                    assertEquals("윤대협", rs.getString("USER_NAME"));
                }
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
                table.column("GROUP_SUB_ID");
                table.record("A", "정대만", "G1", "S1");
                table.record("B", "윤대협", "G2", "S3");
            });
            loader.table("XZY_GROUP", table -> {
                table.column("ID");
                table.column("SUB_ID");
                table.column("NAME");
                table.record("G1", "S1", "그룹 1");
                table.record("G2", "S2", "그룹 2");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("U.ID");
            sql.select("G.NAME", "GROUP_NAME");
            sql.from("XZY_USER", "U");
            sql.fullJoin("XZY_GROUP", "G", on -> {
                on.or("U.GROUP_ID", "G.ID");
                on.or("U.GROUP_SUB_ID", "G.SUB_ID");
            });
            sql.orderBy("ID");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("A", rs.getString("ID"));
                    assertEquals("그룹 1", rs.getString("GROUP_NAME"));
                } else {
                    assertEquals("B", rs.getString("ID"));
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
                table.column("GROUP_SUB_ID");
                table.record("A", "정대만", "G1", "S1");
                table.record("B", "윤대협", "G2", "S3");
            });
            loader.table("XZY_GROUP", table -> {
                table.column("ID");
                table.column("SUB_ID");
                table.column("NAME");
                table.record("G1", "S1", "그룹 1");
                table.record("G2", "S2", "그룹 2");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("U.ID");
            sql.select("G.NAME", "GROUP_NAME");
            sql.from("XZY_USER", "U");
            sql.leftJoin("XZY_GROUP", "G", on -> {
                on.or("U.GROUP_ID", "G.ID");
                on.or("U.GROUP_SUB_ID", "G.SUB_ID");
            });
            sql.orderBy("ID");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("A", rs.getString("ID"));
                    assertEquals("그룹 1", rs.getString("GROUP_NAME"));
                } else {
                    assertEquals("B", rs.getString("ID"));
                    assertEquals("그룹 2", rs.getString("GROUP_NAME"));
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
                table.column("GROUP_SUB_ID");
                table.record("A", "정대만", "G1", "S1");
                table.record("B", "윤대협", "G2", "S3");
            });
            loader.table("XZY_GROUP", table -> {
                table.column("ID");
                table.column("SUB_ID");
                table.column("NAME");
                table.record("G1", "S1", "그룹 1");
                table.record("G2", "S2", "그룹 2");
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("U.ID");
            sql.select("G.NAME", "GROUP_NAME");
            sql.from("XZY_USER", "U");
            sql.rightJoin("XZY_GROUP", "G", on -> {
                on.or("U.GROUP_ID", "G.ID");
                on.or("U.GROUP_SUB_ID", "G.SUB_ID");
            });
            sql.orderBy("ID");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("A", rs.getString("ID"));
                    assertEquals("그룹 1", rs.getString("GROUP_NAME"));
                } else {
                    assertEquals("B", rs.getString("ID"));
                    assertEquals("그룹 2", rs.getString("GROUP_NAME"));
                }
            });
        });
    }

}
