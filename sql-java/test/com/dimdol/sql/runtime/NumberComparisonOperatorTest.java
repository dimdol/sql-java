package com.dimdol.sql.runtime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class NumberComparisonOperatorTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void equal() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.numberColumn("AGE");
                table.record("A", "정대만", 18);
                table.record("B", "윤대협", 17);
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.select("NAME");
            sql.select("AGE");
            sql.from("XZY_USER");
            sql.where("AGE", Op.EQUAL, 17);
            sql.each((i, rs) -> {
                assertEquals("B", rs.getString("ID"));
                assertEquals("윤대협", rs.getString("NAME"));
                assertEquals(17, rs.getInt("AGE"));
            });
        });
    }

    @Test
    public void notEqual() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.numberColumn("AGE");
                table.record("A", "정대만", 18);
                table.record("B", "윤대협", 17);
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.select("NAME");
            sql.select("AGE");
            sql.from("XZY_USER");
            sql.where("AGE", Op.NOT_EQUAL, 17);
            sql.each((i, rs) -> {
                assertEquals("A", rs.getString("ID"));
                assertEquals("정대만", rs.getString("NAME"));
                assertEquals(18, rs.getInt("AGE"));
            });
        });
    }

    @Test
    public void greaterThan() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.numberColumn("AGE");
                table.record("A", "정대만", 18);
                table.record("B", "윤대협", 17);
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.select("NAME");
            sql.select("AGE");
            sql.from("XZY_USER");
            sql.where("AGE", Op.GREATER_THAN, 17);
            sql.each((i, rs) -> {
                assertEquals("A", rs.getString("ID"));
                assertEquals("정대만", rs.getString("NAME"));
                assertEquals(18, rs.getInt("AGE"));
            });
        });
    }

    @Test
    public void greaterThanOrEqual() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.numberColumn("AGE");
                table.record("A", "정대만", 18);
                table.record("B", "윤대협", 17);
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.select("NAME");
            sql.select("AGE");
            sql.from("XZY_USER");
            sql.where("AGE", Op.GREATER_THAN_OR_EQUAL, 18);
            sql.each((i, rs) -> {
                assertEquals("A", rs.getString("ID"));
                assertEquals("정대만", rs.getString("NAME"));
                assertEquals(18, rs.getInt("AGE"));
            });
        });
    }

    @Test
    public void lessThan() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.numberColumn("AGE");
                table.record("A", "정대만", 18);
                table.record("B", "윤대협", 17);
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.select("NAME");
            sql.select("AGE");
            sql.from("XZY_USER");
            sql.where("AGE", Op.LESS_THAN, 18);
            sql.each((i, rs) -> {
                assertEquals("B", rs.getString("ID"));
                assertEquals("윤대협", rs.getString("NAME"));
                assertEquals(17, rs.getInt("AGE"));
            });
        });
    }

    @Test
    public void lessThanOrEqual() {
        DataLoader.run(loader -> {
            loader.table("XZY_USER", table -> {
                table.column("ID");
                table.column("NAME");
                table.numberColumn("AGE");
                table.record("A", "정대만", 18);
                table.record("B", "윤대협", 17);
            });
        }, () -> {
            Sql sql = new Sql();
            sql.select("ID");
            sql.select("NAME");
            sql.select("AGE");
            sql.from("XZY_USER");
            sql.where("AGE", Op.LESS_THAN_OR_EQUAL, 17);
            sql.each((i, rs) -> {
                assertEquals("B", rs.getString("ID"));
                assertEquals("윤대협", rs.getString("NAME"));
                assertEquals(17, rs.getInt("AGE"));
            });
        });
    }

}
