package com.dimdol.sql;

import java.util.List;

class SqlInsertBuilder {

    private Table table;

    private List<Column> columns;

    void into(String tablename) {
        table = new Table(tablename);
    }

    void value(String columnValue) {
    }

    void value(String columName, String columnValue) {
    }

    public void value(int columnValue) {
    }

    public void value(String columName, int columnValue) {
    }

    public String toSql() {
        return null;
    }

}
