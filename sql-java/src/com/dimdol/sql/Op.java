package com.dimdol.sql;

public enum Op {

    EQUAL("="),

    NOT_EQUAL("<>"), // TODO ISO ǥ���� �ƴ� != �� ���� ó��

    GREATER_THAN(">"),

    LESS_THAN("<"),

    GREATER_THAN_OR_EQUAL(">="),

    LESS_THAN_OR_EQUAL("<="),

    NOT_LESS_THAN("!<"), // ISO ǥ���� �ƴ�

    NOT_GREATER_THAN("!>"), // ISO ǥ���� �ƴ�

    IS_NULL("IS NULL"),

    IS_NOT_NULL("IS NOT NULL"),

    BETWEEN("BETWEEN"),

    NOT_BETWEEN("NOT BETWEEN"),

    LIKE("LIKE"),

    NOT_LIKE("NOT LIKE"),

    IN("IN"),

    NOT_IN("NOT IN"),

    EXISTS("EXISTS"),

    NOT_EXISTS("NOT EXISTS"),

    ANY("ANY"),

    ALL("ALL"),

    AND("AND"),

    OR("OR"),

    NOT("NOT"),

    UNION("UNION"),

    UNION_ALL("UNION ALL"),

    EXCEPT("EXCEPT"),

    INTERSECT("INTERSECT"),

    ASC("ASC"),

    DESC("DESC");

    private String op;

    private Op(String op) {
        this.op = op;
    }

    public String toSql() {
        return op;
    }

}