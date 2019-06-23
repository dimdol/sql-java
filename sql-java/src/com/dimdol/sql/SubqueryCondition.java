package com.dimdol.sql;

class SubqueryCondition extends Condition {

    private Sql<?> subquery;

    private String operand1;

    SubqueryCondition(Op operator, Sql<?> subquery) {
        this(operator, subquery, null);
    }

    SubqueryCondition(Op operator, Sql<?> subquery, String operand1) {
        super(operator, Bind.VALUE);
        this.operand1 = operand1;
        this.subquery = subquery;
    }

    @Override
    public void writeSql(SqlCodeBuilder builder) {
        if (operand1 != null) {
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