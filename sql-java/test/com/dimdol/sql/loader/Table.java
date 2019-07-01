package com.dimdol.sql.loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dimdol.sql.SqlRuntime;

public class Table {

    private String name;

    private List<Column> columns = new ArrayList<>();

    private List<Record> records = new ArrayList<>();

    Table(String name) {
        this.name = name;
    }

    public void column(String columnName) {
        columns.add(new Column(columnName, false));
    }

    public void numberColumn(String columnName) {
        columns.add(new Column(columnName, true));
    }

    public void record(Object... values) {
        records.add(new Record(values));
    }

    public void load() {
        try (Connection con = SqlRuntime.getInstance().getConnectionFactory().getConnection()) {
            try (Statement stmt = con.createStatement()) {
                stmt.execute(toDdl());
            }
            String sql = getInsertSql();
            for (Record each : records) {
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    for (int i = 0; i < each.values.length; i++) {
                        pstmt.setObject(i + 1, each.values[i]);
                    }
                    pstmt.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private String toDdl() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("CREATE TABLE %s (", name));
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                result.append(",");
            }
            result.append(columns.get(i).toSql());
        }
        result.append("\r\n)");
        return result.toString();
    }

    private String getInsertSql() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("INSERT INTO %s (", name));
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                result.append(",");
            }
            result.append(columns.get(i).columnName);
        }
        result.append(") VALUES (");
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                result.append(",");
            }
            result.append("?");
        }
        result.append(")");
        return result.toString();
    }

    private static class Column {
        String columnName;
        boolean isNumber;

        Column(String columnName, boolean isNumber) {
            this.columnName = columnName;
            this.isNumber = isNumber;
        }

        public String toSql() {
            if (isNumber) {
                return String.format("\r\n\t%s INT", columnName);
            } else {
                return String.format("\r\n\t%s VARCHAR2(100)", columnName);
            }
        }
    }

    private static class Record {
        Object[] values;

        Record(Object[] values) {
            this.values = values;
        }
    }

    public void drop() {
        try (Connection con = SqlRuntime.getInstance().getConnectionFactory().getConnection(); Statement stmt = con.createStatement()) {
            stmt.executeQuery(String.format("DROP TABLE %s", name));
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

}
