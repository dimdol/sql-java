package com.dimdol.sql;

import java.util.ArrayList;
import java.util.List;

class InsertInto extends Unit {

    private final String tableName;

    private final List<Value> values = new ArrayList<>();

    InsertInto(String tableName) {
        this.tableName = tableName;
    }

    void value(Bind bind, String columnName, Object value) {
        values.add(new Value(bind, columnName, value));
    }

    @Override
    void writeSql(SqlCodeBuilder builder) {
        builder.append(String.format("INSERT INTO %s", tableName));
        builder.append(" (");
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(values.get(i).columnName);
        }
        builder.append(")");
        builder.build("VALUES (", ",", values);
        builder.append(")");
    }

    private static class Value extends Unit implements Parameter {

        private final Bind bind;

        private final String columnName;

        private final Object value;

        public Value(Bind bind, String columnName, Object value) {
            this.bind = bind;
            this.columnName = columnName;
            this.value = value;
        }

        @Override
        void writeSql(SqlCodeBuilder builder) {
            if (bind == Bind.PARAM) {
                builder.append("?");
            } else {
                // TODO value가 null일 때 처리
                if (value instanceof Number) {
                    builder.append(value.toString());
                } else {
                    builder.append(String.format("'%s'", value.toString()));
                }
            }
        }

        @Override
        public boolean prepared() {
            return this.bind == Bind.PARAM;
        }

        @Override
        public Object[] getValues() {
            return new Object[] { value };
        }

    }

}
