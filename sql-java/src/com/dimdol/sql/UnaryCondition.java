package com.dimdol.sql;

class UnaryCondition extends Condition implements Parameter {

    private String operand1;

    UnaryCondition(Op operator, String operand1) {
        super(operator, Bind.COLUMN);
        if (operator != Op.IS_NULL && operator != Op.IS_NOT_NULL) {
            throw new IllegalArgumentException();
        }
        this.operand1 = operand1;
    }

    @Override
    public boolean prepared() {
        return false;
    }

    @Override
    public Object[] getValues() {
        throw new UnsupportedOperationException();
    }

    public String getOperand1() {
        return operand1;
    }

    @Override
    void writeSql(SqlCodeBuilder builder) {
        builder.append(toString());
    }

    @Override
    public String toString() {
        return String.format("%s %s", operand1, getOperator().toSql());
    }

}