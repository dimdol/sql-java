package com.dimdol.sql;

import java.util.ArrayList;
import java.util.List;

class UpdateSet extends Unit {

    private final String tableName;

    private final List<Set> sets = new ArrayList<>();

    UpdateSet(String tableName) {
        this.tableName = tableName;
    }

    void set(Bind bind, String columnName, Object value) {
        sets.add(new Set(bind, columnName, value));
    }

    @Override
    void writeSql(SqlCodeBuilder builder) {
        builder.append(String.format("UPDATE %s ", tableName));
        builder.build("SET", ",", sets);
    }

    private static class Set extends Unit implements Parameter {

        private final Bind bind;

        private final String columnName;

        private final Object value;

        public Set(Bind bind, String columnName, Object value) {
            this.bind = bind;
            this.columnName = columnName;
            this.value = value;
        }

        @Override
        void writeSql(SqlCodeBuilder builder) {
            if (bind == Bind.PARAM) {
                builder.append(String.format("%s = ?", columnName));
            } else if (bind == Bind.COLUMN) {
                // TODO value가 null이거나 String이 아닐 때 처리
                builder.append(String.format("%s = %s", columnName, value.toString()));
            } else {
                // TODO value가 null일 때 처리
                if (value instanceof Number) {
                    builder.append(String.format("%s = %s", columnName, value.toString()));
                } else {
                    builder.append(String.format("%s = '%s'", columnName, value.toString()));
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
