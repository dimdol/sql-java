package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class HavingTest {

    @Test
    public void having() {
        Sql sql = new Sql();
        sql.select("TYPE");
        sql.select("MIN(AGE)");
        sql.from("USER");
        sql.groupBy("TYPE");
        sql.having(where -> {
            where.where("MIN(AGE)", Op.GREATER_THAN, Bind.VALUE, 20);
            where.where("TYPE", Op.NOT_EQUAL, Bind.VALUE, "SF");
        });
        sql.orderBy("TYPE");
        assertEquals("SELECT TYPE, MIN(AGE) FROM USER GROUP BY TYPE HAVING MIN(AGE) > 20 AND TYPE <> 'SF' ORDER BY TYPE", sql.toSql());
    }

    @Test
    public void or() {
        Sql sql = new Sql();
        sql.select("TYPE");
        sql.select("MIN(AGE)");
        sql.from("USER");
        sql.groupBy("TYPE");
        sql.having(where -> {
            where.or(or -> {
                or.where("MIN(AGE)", Op.GREATER_THAN, Bind.VALUE, 20);
                or.where("TYPE", Op.NOT_EQUAL, Bind.VALUE, "SF");
            });
        });
        sql.orderBy("TYPE");
        assertEquals("SELECT TYPE, MIN(AGE) FROM USER GROUP BY TYPE HAVING (MIN(AGE) > 20 OR TYPE <> 'SF') ORDER BY TYPE", sql.toSql());
    }

}