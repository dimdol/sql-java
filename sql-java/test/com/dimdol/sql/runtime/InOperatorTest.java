package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Bind;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class InOperatorTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void characterParam() {
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
            sql.where("NAME", Op.IN, "윤대협", "채치수");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("윤대협", rs.getString("NAME"));
                } else {
                    assertEquals("채치수", rs.getString("NAME"));
                }
            });
        });
    }

    @Test
    public void characterValue() {
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
            sql.where("NAME", Op.IN, Bind.VALUE, "윤대협", "채치수");
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("윤대협", rs.getString("NAME"));
                } else {
                    assertEquals("채치수", rs.getString("NAME"));
                }
            });
        });
    }

    @Test
    public void numberParam() {
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
            sql.where("AGE", Op.IN, 20, 23);
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("정대만", rs.getString("NAME"));
                } else {
                    assertEquals("강백호", rs.getString("NAME"));
                }
            });
        });
    }

    @Test
    public void numberValue() {
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
            sql.where("AGE", Op.IN, Bind.VALUE, 20, 23);
            sql.each((i, rs) -> {
                if (i == 0) {
                    assertEquals("정대만", rs.getString("NAME"));
                } else {
                    assertEquals("강백호", rs.getString("NAME"));
                }
            });
        });
    }

}
