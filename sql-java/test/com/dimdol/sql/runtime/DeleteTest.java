package com.dimdol.sql.runtime;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.Examples;
import com.dimdol.sql.Op;
import com.dimdol.sql.Sql;
import com.dimdol.sql.loader.DataLoader;

public class DeleteTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void delete() {
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
            sql.deleteFrom("XZY_USER");
            sql.where("ID", Op.EQUAL, "A");
            assertEquals(1, sql.execute());

            sql = new Sql();
            sql.select("COUNT(*)");
            sql.from("XZY_USER");
            sql.where("ID", Op.EQUAL, "A");
            sql.each((i, rs) -> {
                assertEquals(0, rs.getInt(1));
            });
        });
    }

}
