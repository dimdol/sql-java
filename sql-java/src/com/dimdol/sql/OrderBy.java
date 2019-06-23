package com.dimdol.sql;

class OrderBy extends Unit {

    private String name;

    OrderBy(String name) {
        this.name = name;
    }

    @Override
    public void writeSql(SqlCodeBuilder builder) {
        builder.append(name);
    }

}