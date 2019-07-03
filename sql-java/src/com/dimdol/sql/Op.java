package com.dimdol.sql;

public enum Op {

    EQUAL("="),

    NOT_EQUAL("<>"), // TODO ISO 표준이 아닌 != 에 대한 처리

    GREATER_THAN(">"),

    LESS_THAN("<"),

    GREATER_THAN_OR_EQUAL(">="),

    LESS_THAN_OR_EQUAL("<="),

    NOT_LESS_THAN("!<"), // ISO 표준이 아님

    NOT_GREATER_THAN("!>"), // ISO 표준이 아님

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

    SOME("SOME"),

    ALL("ALL"),

    AND("AND"),

    OR("OR"),

    NOT("NOT"),

    UNION("UNION"),

    UNION_ALL("UNION ALL"),

    EXCEPT("EXCEPT"),

    MINUS("MINUS"),

    INTERSECT("INTERSECT"),

    ASC("ASC"),

    DESC("DESC");

    private final String op;

    private Op(String op) {
        this.op = op;
    }

    public String toSql() {
        return op;
    }

}