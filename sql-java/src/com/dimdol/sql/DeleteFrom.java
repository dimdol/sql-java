package com.dimdol.sql;

class DeleteFrom extends Unit {

    private final String tableName;

    DeleteFrom(String tableName) {
        this.tableName = tableName;
    }

    @Override
    void writeSql(SqlCodeBuilder builder) {
        builder.append(String.format("DELETE FROM %s", tableName));
    }

}