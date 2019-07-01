package com.dimdol.sql;

class TernaryCondition extends Condition implements Parameter {

    private String columnName;

    private Object[] values;

    TernaryCondition(Op operator, Bind bind, String operand1, Object... values) {
        super(operator, bind);
        if ((operator == Op.IN || operator == Op.NOT_IN) && bind == Bind.COLUMN) {
            throw new IllegalArgumentException();
        }
        if ((operator == Op.BETWEEN || operator == Op.NOT_BETWEEN) && bind == Bind.COLUMN) {
            throw new IllegalArgumentException();
        }

        this.columnName = operand1;
        this.values = values;
    }

    @Override
    public boolean prepared() {
        return getBind() == Bind.PARAM;
    }

    @Override
    public Object[] getValues() {
        return values;
    }

    public String getOperand1() {
        return columnName;
    }

    @Override
    void writeSql(SqlCodeBuilder builder) {
        Op operator = getOperator();
        if (operator == Op.IN || operator == Op.NOT_IN) {
            switch (getBind()) {
            case PARAM:
                builder.append(columnName + " " + getOperator().toSql() + " (");
                for (int i = 0; i < values.length; i++) {
                    if (i != 0) {
                        builder.append(", ");
                    }
                    builder.append("?");
                }
                builder.append(")");
                break;
            case VALUE:
                builder.append(toString());
                break;
            default:
                throw new IllegalStateException();
            }
        } else if (operator == Op.BETWEEN || operator == Op.NOT_BETWEEN) {
            switch (getBind()) {
            case PARAM:
                builder.append(columnName + " " + getOperator().toSql() + " ? AND ?");
                break;
            case VALUE:
                builder.append(toString());
                break;
            default:
                throw new IllegalStateException();
            }
        } else {
            switch (getBind()) {
            case PARAM:
                builder.append(columnName + " " + getOperator().toSql() + " ?");
                break;
            case VALUE:
                builder.append(columnName + " " + getOperator().toSql() + " '" + values[0] + "'");
                break;
            case COLUMN:
                builder.append(columnName + " " + getOperator().toSql() + " " + values[0]);
                break;
            default:
                throw new IllegalStateException();
            }
        }
    }

    @Override
    public String toString() {
        Op operator = getOperator();
        if (operator == Op.IN || operator == Op.NOT_IN) {
            StringBuilder result = new StringBuilder();
            result.append(columnName + " " + getOperator().toSql() + " (");
            for (int i = 0; i < values.length; i++) {
                if (i != 0) {
                    result.append(", ");
                }
                if (values[0] instanceof String) {
                    result.append(String.format("'%s'", values[i]));
                } else {
                    result.append(String.format("%s", values[i]));
                }
            }
            result.append(")");
            return result.toString();
        } else if (operator == Op.BETWEEN || operator == Op.NOT_BETWEEN) {
            if (values[0] instanceof String) {
                return String.format("%s %s '%s' AND '%s'", columnName, getOperator().toSql(), values[0], values[1]);
            } else {
                return String.format("%s %s %s AND %s", columnName, getOperator().toSql(), values[0], values[1]);
            }
        } else {
            return String.format("%s %s '%s'", columnName, getOperator().toSql(), values[0]);
        }

    }

}