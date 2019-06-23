package com.dimdol.sql;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

public class SqlCodeBuilderTest extends Unit {

    @Override
    public void writeSql(SqlCodeBuilder sqlCodeBuilder) {
        sqlCodeBuilder.append("A");
    }

    @Test
    public void build() {
        SqlCodeBuilder builder = new SqlCodeBuilder();

        List<Unit> units = new ArrayList<>();
        units.add(new SqlCodeBuilderTest());
        units.add(new SqlCodeBuilderTest());
        builder.build("SELECT", ",", units);
        assertEquals("SELECT A, A", builder.getCode());

        builder.build("FROM", ",", units);
        assertEquals("SELECT A, A FROM A, A", builder.getCode());

        builder.build("WHERE", " AND", units);
        assertEquals("SELECT A, A FROM A, A WHERE A AND A", builder.getCode());
    }

}