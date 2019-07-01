package com.dimdol.sql;

class Table extends Unit {

    private String name;

    private Alias alias;

    Table(String name) {
        this.name = name;
    }

    Table(String name, String alias) {
        this.name = name;
        if (alias != null) {
            this.alias = new Alias(alias, true);
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