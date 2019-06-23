package com.dimdol.sql;

public interface WhereClause {

    public void where(Op operator, String operand1, String operand2);

    public void where(Op operator, Bind bind, String operand1, String operand2);

    public void where(Op operator, SubqueryBuilder builder);

    public void where(Op operator, SubqueryBuilder builder, String operand1);

    public void or(CompositeConditionBuilder builder);

    public void and(CompositeConditionBuilder builder);

    public void not(CompositeConditionBuilder builder);

}