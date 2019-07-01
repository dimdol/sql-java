package com.dimdol.sql;

public interface WhereClause {

    public void where(String columnName, Op operator);

    public void where(String columnName, Op operator, Object... values);

    public void where(String columnName, Op operator, Bind bind, Object... values);

    public void where(Op operator, SubqueryBuilder builder);

    public void where(String columnName, Op operator, SubqueryBuilder builder);

    public void where(String columnName, Op operator, Op allOrAny, SubqueryBuilder builder);

    public void or(CompositeConditionBuilder builder);

    public void and(CompositeConditionBuilder builder);

    public void not(CompositeConditionBuilder builder);

}