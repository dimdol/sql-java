package com.dimdol.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CompositeCondition extends Condition implements WhereClause {

    private List<Condition> conditions;

    CompositeCondition(Op operator) {
        super(operator, Bind.COLUMN);
    }

    @Override
    public void where(String columnName, Op operator) {
        addCondition(new UnaryCondition(operator, columnName));
    }

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
    public void or(Consumer<CompositeCondition> or) {
        CompositeCondition condition = new CompositeCondition(Op.OR);
        or.accept(condition);
        addCondition(condition);
    }

    @Override
    public void and(Consumer<CompositeCondition> and) {
        CompositeCondition condition = new CompositeCondition(Op.AND);
        and.accept(condition);
        addCondition(condition);
    }

    @Override
    public void not(Consumer<CompositeCondition> not) {
        CompositeCondition condition = new CompositeCondition(Op.NOT);
        not.accept(condition);
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