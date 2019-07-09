package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class UpdateTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void update() {
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
            sql.update("XZY_USER");
            sql.set("NAME", "송태섭");
            sql.set("AGE", 25);
            sql.where("ID", Op.EQUAL, "A");
            sql.where("AGE", Op.EQUAL, 23);
            assertEquals(1, sql.execute());

            sql = new Sql();
            sql.select("NAME");
            sql.select("AGE");
            sql.from("XZY_USER");
            sql.where("ID", Op.EQUAL, "A");
            sql.each((i, rs) -> {
                assertEquals("송태섭", rs.getString("NAME"));
                assertEquals(25, rs.getInt("AGE"));
            });
        });
    }

}
