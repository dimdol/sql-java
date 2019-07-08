package com.dimdol.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class JoinTableTest {

    @Test(expected = IllegalArgumentException.class)
    public void unsupportedKeyword() {
        new JoinTable("GROUP", "G", Keyword.BETWEEN);
    }

    @Test(expected = NullPointerException.class)
    public void joinTypeRequired() {
        new JoinTable("GROUP", "G", null);
    }

    @Test
    public void join() {
        JoinTable table = new JoinTable("GROUP", "G", Keyword.JOIN);
        table.and("U.GROUP_ID", "G.ID");
        assertEquals("JOIN GROUP G ON U.GROUP_ID = G.ID", table.toSql());
    }

    @Test
    public void innerJoin() {
        JoinTable table = new JoinTable("GROUP", "G", Keyword.INNER_JOIN);
        table.and("U.GROUP_ID", "G.ID");
        assertEquals("INNER JOIN GROUP G ON U.GROUP_ID = G.ID", table.toSql());
    }

    @Test
    public void fullOuterJoin() {
        JoinTable table = new JoinTable("GROUP", "G", Keyword.FULL_OUTER_JOIN);
        table.and("U.GROUP_ID", "G.ID");
        assertEquals("FULL OUTER JOIN GROUP G ON U.GROUP_ID = G.ID", table.toSql());
    }

    @Test
    public void leftJoin() {
        JoinTable table = new JoinTable("GROUP", "G", Keyword.LEFT_JOIN);
        table.and("U.GROUP_ID", "G.ID");
        assertEquals("LEFT JOIN GROUP G ON U.GROUP_ID = G.ID", table.toSql());
    }

    @Test
    public void rightJoin() {
        JoinTable table = new JoinTable("GROUP", "G", Keyword.RIGHT_JOIN);
        table.and("U.GROUP_ID", "G.ID");
        assertEquals("RIGHT JOIN GROUP G ON U.GROUP_ID = G.ID", table.toSql());
    }

    @Test
    public void andCondition() {
        JoinTable table = new JoinTable("GROUP", "G", Keyword.JOIN);
        table.and("U.GROUP_ID", "G.ID");
        table.and("U.SUB_GROUP_ID", "G.SUB_ID");
        assertEquals("JOIN GROUP G ON U.GROUP_ID = G.ID AND U.SUB_GROUP_ID = G.SUB_ID", table.toSql());
    }

    @Test
    public void orCondition() {
        JoinTable table = new JoinTable("GROUP", "G", Keyword.JOIN);
        table.and("U.GROUP_ID", "G.ID");
        table.or("U.SUB_GROUP_ID", "G.SUB_ID");
        assertEquals("JOIN GROUP G ON U.GROUP_ID = G.ID OR U.SUB_GROUP_ID = G.SUB_ID", table.toSql());
    }

}
