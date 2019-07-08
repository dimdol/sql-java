package com.dimdol.sql;

final class GroupBy extends Unit {

    private final String[] columnNames;

    GroupBy(String... columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public void writeSql(SqlCodeBuilder builder) {
        builder.append("GROUP BY ");
        for (int i = 0; i < columnNames.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(columnNames[i]);
        }
    }

}