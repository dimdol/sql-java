package com.dimdol.sql;

@FunctionalInterface
public interface CompositeConditionBuilder {

    void build(CompositeCondition condition);

}
