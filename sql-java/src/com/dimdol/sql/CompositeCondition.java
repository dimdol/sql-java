package com.dimdol.sql;

import java.util.ArrayList;
import java.util.List;

public class CompositeCondition extends Condition implements WhereClause {

    private List<Condition> conditions;

    CompositeCondition(Op operator) {
        super(operator, Bind.COLUMN);
    }

    @Override
    public void where(String columnName, Op operator) {
        addCondition(new UnaryCondition(operator, columnName));
    }

    // @Override
    // public void where(Op operator, String operand1, Number operand2) {
    // where(operator, Bind.PARAM, operand1, operand2);
    // }
    //
    // @Override
    // public void where(Op operator, Bind bind, String operand1, Number operand2) {
    // addCondition(new NumberCondition(operator, bind, operand1, operand2));
    // }

    @Override
    public void where(String columnName, Op operator, Object... values) {
        where(columnName, operator, Bind.PARAM, values);
    }

    @Override
    public void where(String columnName, Op operator, Bind bind, Object... values) {
        addCondition(new TernaryCondition(operator, bind, columnName, values));
    }

    @Override
    public void where(Op operator, SubqueryBuilder builder) {
        if (operator != Op.EXISTS && operator != Op.NOT_EXISTS) {
            throw new IllegalArgumentException();
        }

        Sql subquery = new Sql();
        builder.build(subquery);
        addCondition(new SubqueryCondition(operator, subquery));
    }

    @Override
    public void where(String columnName, Op operator, SubqueryBuilder builder) {
        if (operator == Op.EXISTS || operator == Op.NOT_EXISTS) {
            throw new IllegalArgumentException();
        }

        Sql subquery = new Sql();
        builder.build(subquery);
        addCondition(new SubqueryCondition(operator, subquery, columnName));
    }

    @Override
    public void where(String columName, Op operator, Op anyOrAll, SubqueryBuilder builder) {
        if (operator == Op.EXISTS || operator == Op.NOT_EXISTS) {
            throw new IllegalArgumentException();
        }

        Sql subquery = new Sql();
        builder.build(subquery);
        addCondition(new SubqueryCondition(columName, operator, anyOrAll, subquery));
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