package com.dimdol.sql.runtime;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dimdol.example.DB;
import com.dimdol.example.Examples;
import com.dimdol.sql.Console;
import com.dimdol.sql.Op;
import com.dimdol.sql.loader.DataLoader;

public class ConsoleTest {

    @BeforeClass
    public static void setUp() {
        Examples.configure();
    }

    @Test
    public void log() {
        DataLoader.run(loader -> {
            loader.table("XZY_COMPANY", table -> {
                table.column("ID");
                table.column("NAME");
                table.numberColumn("START_YEAR");
                table.numberColumn("TIME");
                table.record("A", "3Rabbitz", 2010, null);
                table.record("C", "Walt Diseny", 1933, System.currentTimeMillis());
                table.record("B", "Apple", 1975, System.currentTimeMillis());
            });
            loader.table("XZY_GROUP", table -> {
                table.column("ID");
                table.column("NAME");
                table.numberColumn("TIME");
                table.record("1", "Group 1", null);
                table.record("2", "Group 2", System.currentTimeMillis());
                table.record("3", "Group 3", System.currentTimeMillis());
            });
        }, () -> {
            Console console = new Console();
            console.setFormatter((name, value) -> {
                if (value == null) {
                    return null;
                }
                if ("TIME".equals(name)) {
                    return String.format("%1$tF %1$tT", new Date(((BigDecimal) value).longValue()));
                } else if ("NAME".equals(name)) {
                    return String.format("<%s>", value);
                } else {
                    return value;
                }
            });
            console.setSorter("ID", Op.ASC);
            console.add(DB.ORACLE, sql -> {
                sql.select("ID");
                sql.select("NAME");
                sql.select("START_YEAR");
                sql.select("TIME");
                sql.from("XZY_COMPANY");
                sql.orderBy("NAME");
            }, 1);
            console.add(sql -> {
                sql.select("ID");
                sql.select("NAME");
                sql.select("TIME");
                sql.from("XZY_GROUP");
                sql.orderBy("TIME");
            }, 1);
            console.log();
        });
    }

    @Test
    public void desc() {
        DataLoader.run(loader -> {
            loader.table("XZY_COMPANY", table -> {
                table.column("ID");
                table.column("NAME");
                table.numberColumn("START_YEAR");
                table.numberColumn("TIME");
            });
        }, () -> {
            Console console = new Console();
            console.desc("XZY_COMPANY");
        });
    }

    @Test
    public void time() {
        DataLoader.run(loader -> {
            loader.table("XZY_COMPANY", table -> {
                table.column("ID");
                table.column("NAME");
                table.numberColumn("START_YEAR");
                table.numberColumn("TIME");
                table.record("A", "3Rabbitz", 2010, null);
                table.record("C", "Walt Diseny", 1933, System.currentTimeMillis());
                table.record("B", "Apple", 1975, System.currentTimeMillis());
            });
        }, () -> {
            Console console = new Console();
            console.add(sql -> {
                sql.select("ID");
                sql.select("NAME");
                sql.select("TIME");
                sql.from("XZY_COMPANY");
                sql.where("NAME", Op.EQUAL, "Apple");
                sql.orderBy("TIME");
            }, 1);
            console.add(sql -> {
                sql.select("ID");
                sql.select("NAME");
                sql.select("TIME");
                sql.from("XZY_COMPANY");
                sql.where("ID", Op.EQUAL, "C");
                sql.orderBy("TIME");
            }, 1);
            console.time(10);
        });
    }

}
