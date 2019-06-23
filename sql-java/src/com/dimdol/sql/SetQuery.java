package com.dimdol.sql;

class SetQuery {

    private Op operator;

    private Sql<?> sql;

    SetQuery(Op operator, Sql<?> sql) {
        this.operator = operator;
        this.sql = sql;
    }

    public Sql<?> getSql() {
        return sql;
    }

    Op getOperator() {
        return operator;
    }

}