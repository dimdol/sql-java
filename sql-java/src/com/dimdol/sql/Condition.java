package com.dimdol.sql;

abstract class Condition extends Unit {

    private Op operator;

    private Bind bind;

    Condition(Op operator, Bind bind) {
        this.operator = operator;
        this.bind = bind;
    }

    Op getOperator() {
        return operator;
    }

    Bind getBind() {
        return bind;
    }

}