package com.dimdol.sql;

class SubqueryCondition extends Condition {

    private Sql subquery;

    private String operand1;

    private Op allOrAny;

    SubqueryCondition(Op operator, Sql subquery) {
        this(operator, subquery, null);
    }

    SubqueryCondition(Op operator, Sql subquery, String operand1) {
        super(operator, Bind.VALUE);
        this.operand1 = operand1;
        this.subquery = subquery;
    }

    SubqueryCondition(String columnName, Op operator, Op allOrAny, Sql subquery) {
        super(operator, Bind.VALUE);
        if (operator != Op.EQUAL && operator != Op.NOT_EQUAL && operator != Op.GREATER_THAN && operator != Op.GREATER_THAN_OR_EQUAL && operator != Op.LESS_THAN && operator != Op.LESS_THAN_OR_EQUAL) {
            throw new IllegalArgumentException();
        }
        if (allOrAny != Op.ALL && allOrAny != Op.ANY && allOrAny != Op.SOME) {
            throw new IllegalArgumentException();
        }
        this.operand1 = columnName;
        this.allOrAny = allOrAny;
        this.subquery = subquery;
    }

    @Override
    public void writeSql(SqlCodeBuilder builder) {
        if (allOrAny != null) {
            builder.append(operand1 + " " + getOperator().toSql());
            builder.append(" " + allOrAny.toSql());
            builder.append(" (");
            builder.append(subquery);
            builder.append(")");
        } else if (operand1 != null) {
            builder.append(operand1 + " " + getOperator().toSql());
            builder.append(" (");
            builder.append(subquery);
            builder.append(")");
        } else {
            builder.append(getOperator().toSql());
            builder.append(" (");
            builder.append(subquery);
            builder.append(")");
        }
    }

}