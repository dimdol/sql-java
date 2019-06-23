package com.dimdol.sql;

class StringCondition extends Condition implements Parameter {

    private String operand1;

    private String operand2;

    StringCondition(Op operator, Bind bind, String operand1, String operand2) {
        super(operator, bind);
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    @Override
    public boolean prepared() {
        return getBind() == Bind.PARAM;
    }

    public String getOperand1() {
        return operand1;
    }

    @Override
    void writeSql(SqlCodeBuilder builder) {
        switch (getBind()) {
        case PARAM:
            builder.append(operand1 + " " + getOperator().toSql() + " ?");
            break;
        case VALUE:
            builder.append(operand1 + " " + getOperator().toSql() + " '" + operand2 + "'");
            break;
        case COLUMN:
            builder.append(operand1 + " " + getOperator().toSql() + " " + operand2);
            break;
        default:
            throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s '%s'", operand1, getOperator().toSql(), operand2);
    }

}