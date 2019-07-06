package com.dimdol.sql;

import java.util.function.Consumer;

public interface WhereClause {

    public void where(String columnName, Op operator);

    public void where(String columnName, Op operator, Object... values);

    public void where(String columnName, Op operator, Bind bind, Object... values);

    public void where(Op operator, SubqueryBuilder builder);

    public void where(String columnName, Op operator, SubqueryBuilder builder);

    public void where(String columnName, Op operator, Op allOrAny, SubqueryBuilder builder);

    public void or(Consumer<CompositeCondition> or);

    public void and(Consumer<CompositeCondition> and);

    public void not(Consumer<CompositeCondition> not);

}