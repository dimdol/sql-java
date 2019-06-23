package com.dimdol.sql;

class Column extends Unit {

    private String name;

    private Alias alias;

    Column(String name) {
        this.name = name;
    }

    Column(String name, String alias) {
        this.name = name;
        if (alias != null) {
            this.alias = new Alias(alias);
        }
    }

    Alias getAlias() {
        return alias;
    }

    @Override
    public void writeSql(SqlCodeBuilder builder) {
        if (alias != null) {
            builder.append(name + alias.toSql());
        } else {
            builder.append(name);
        }
    }

}