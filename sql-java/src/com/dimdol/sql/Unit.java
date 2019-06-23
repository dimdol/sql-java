package com.dimdol.sql;

public abstract class Unit {

    abstract void writeSql(SqlCodeBuilder builder);

    String toSql() {
        SqlCodeBuilder builder = new SqlCodeBuilder();
        writeSql(builder);
        return builder.getCode();
    }

}
