package com.dimdol.sql;

@FunctionalInterface
public interface SubqueryBuilder {

    void build(Sql sql);

}