package com.dimdol.sql;

import java.util.ArrayList;
import java.util.List;

public class CompositeCondition extends Condition implements WhereClause {

    private List<Condition> conditions;

    CompositeCondition(Op operator) {
        super(operator, Bind.COLUMN);
    }

    @Override
    public void where(Op operator, String operand1, String operand2) {
        where(operator, Bind.PARAM, operand1, operand2);
    }

    @Override
    public void where(Op operator, Bind bind, String operand1, String operand2) {
        addCondition(new StringCondition(operator, bind, operand1, operand2));
    }

    @Override
    public void where(Op operator, SubqueryBuilder builder) {
        if (operator != Op.EXISTS && operator != Op.NOT_EXISTS) {
            throw new IllegalArgumentException();
        }

        Sql<?> subquery = new Sql<>();
        builder.build(subquery);
        addCondition(new SubqueryCondition(operator, subquery));
    }

    @Override
    public void where(Op operator, SubqueryBuilder builder, String operand1) {
        if (operator == Op.EXISTS || operator == Op.NOT_EXISTS) {
            throw new IllegalArgumentException();
        }

        Sql<?> subquery = new Sql<>();
        builder.build(subquery);
        addCondition(new SubqueryCondition(operator, subquery, operand1));
    }

    @Override
    public void or(CompositeConditionBuilder builder) {
        CompositeCondition condition = new CompositeCondition(Op.OR);
        builder.build(condition);
        addCondition(condition);
    }

    @Override
    public void and(CompositeConditionBuilder builder) {
        CompositeCondition condition = new CompositeCondition(Op.AND);
        builder.build(condition);
        addCondition(condition);
    }

    @Override
    public void not(CompositeConditionBuilder builder) {
        CompositeCondition condition = new CompositeCondition(Op.NOT);
        builder.build(condition);
        addCondition(condition);
    }

    private void addCondition(Condition condition) {
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        conditions.add(condition);
    }

    @Override
    void writeSql(SqlCodeBuilder builder) {
        Op operator = getOperator();
        if (operator == Op.NOT) {
            builder.build(Op.NOT.toSql(), "", conditions);
        } else {
            builder.append("(");
            builder.build("", " " + operator.toSql(), conditions);
            builder.append(")");
        }
    }

}