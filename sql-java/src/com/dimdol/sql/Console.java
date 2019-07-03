package com.dimdol.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Console {

    private List<SqlEntry> sqlEntries = new ArrayList<>();

    private Comparator<Map<String, Object>> comparator;

    private BiFunction<String, Object, Object> formatter;

    public void add(Sql sql) {
        add(null, sql);
    }

    public void add(Enum<?> connectionType, Sql sql) {
        add(connectionType, sql, Option.MAX_FETCH_LIMT.getInt());
    }

    public void add(Enum<?> connectionType, Sql sql, int limit) {
        sqlEntries.add(new SqlEntry(connectionType, sql, limit));
    }

    public void add(Consumer<Sql> consumer) {
        add(null, consumer);
    }

    public void add(Consumer<Sql> consumer, int limit) {
        add(null, consumer, limit);
    }

    public void add(Enum<?> connectionType, Consumer<Sql> consumer) {
        add(connectionType, consumer, Option.MAX_FETCH_LIMT.getInt());
    }

    public void add(Enum<?> connectionType, Consumer<Sql> consumer, int limit) {
        Sql sql = new Sql();
        consumer.accept(sql);
        add(connectionType, sql, limit);
    }

    public void setSorter(String columnName, Op op) {
        setSorter((a, b) -> {
            Object v1 = op == Op.ASC ? a.get(columnName) : b.get(columnName);
            Object v2 = op == Op.ASC ? b.get(columnName) : a.get(columnName);
            if (v1 == null && v2 == null) {
                return 0;
            } else if (v1 == null) {
                return -1;
            } else if (v2 == null) {
                return 1;
            } else {
                return v1.toString().compareTo(v2.toString());
            }
        });
    }

    public void setSorter(String columnName) {
        setSorter(columnName, Op.ASC);
    }

    public void setSorter(Comparator<Map<String, Object>> comparator) {
        this.comparator = comparator;
    }

    public void setFormatter(BiFunction<String, Object, Object> formatter) {
        this.formatter = formatter;
    }

    public void log() {
        if (sqlEntries.isEmpty()) {
            throw new IllegalStateException();
        }

        Map<String, ColumnEntry> columnEntries = new LinkedHashMap<>();
        List<Map<String, Object>> values = new ArrayList<>();
        for (SqlEntry sqlEntry : sqlEntries) {
            sqlEntry.sql.each(sqlEntry.connectionType, (i, rs) -> {
                if (i == 0) {
                    configureColumnEntries(columnEntries, rs);
                }
                values.add(loadObject(columnEntries, rs));
            }, sqlEntry.limit);
        }

        String linePattern = getLinePattern(columnEntries);
        header(columnEntries, linePattern);
        values(values, columnEntries, linePattern);
    }

    private void configureColumnEntries(Map<String, ColumnEntry> columnEntries, ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int size = rsmd.getColumnCount();
        for (int columnIndex = 1; columnIndex <= size; columnIndex++) {
            String columnLabel = rsmd.getColumnLabel(columnIndex);
            if (!columnEntries.containsKey(columnLabel)) {
                columnEntries.put(columnLabel, new ColumnEntry(columnLabel));
            }
        }
    }

    private Map<String, Object> loadObject(Map<String, ColumnEntry> columnEntries, ResultSet rs) {
        Map<String, Object> result = new HashMap<>();
        for (ColumnEntry columnEntry : columnEntries.values()) {
            Object object = null;
            try {
                object = rs.getObject(columnEntry.label);
            } catch (SQLException ignore) {
                // label이 존재하지 않을 때 발생하는 예외 무시
            }
            if (object != null) {
                if (formatter != null) {
                    object = formatter.apply(columnEntry.label, object);
                }
                columnEntry.configure(object);
                result.put(columnEntry.label, object);
            }
        }
        return result;
    }

    private String getLinePattern(Map<String, ColumnEntry> columnEntries) {
        StringBuilder result = new StringBuilder();
        for (ColumnEntry columnEntry : columnEntries.values()) {
            if (result.length() > 0) {
                result.append("  ");
            }
            result.append(columnEntry.getFormatPattern());
        }
        result.append("\n");
        return result.toString();
    }

    private void header(Map<String, ColumnEntry> columnEntries, String linePattern) {
        int columnIndex = 0;
        Object[] args = new Object[columnEntries.size()];
        for (ColumnEntry columnEntry : columnEntries.values()) {
            args[columnIndex++] = columnEntry.label;
        }
        System.out.printf(linePattern, args);
        columnIndex = 0;
        args = new Object[columnEntries.size()];
        while (columnIndex < args.length) {
            args[columnIndex++] = "==";
        }
        System.out.printf(linePattern, args);
    }

    private void values(List<Map<String, Object>> values, Map<String, ColumnEntry> columnEntries, String linePattern) {
        if (values.isEmpty()) {
            return;
        }

        if (comparator != null) {
            values.sort(comparator);
        }
        for (Map<String, Object> value : values) {
            Object[] args = new Object[columnEntries.size()];
            int i = 0;
            for (ColumnEntry columnEntry : columnEntries.values()) {
                args[i++] = value.get(columnEntry.label);
            }
            System.out.printf(linePattern, args);
        }
    }

    private class SqlEntry {

        Enum<?> connectionType;

        Sql sql;

        int limit;

        SqlEntry(Enum<?> connectionType, Sql sql, int limit) {
            this.connectionType = connectionType;
            this.sql = sql;
            this.limit = limit;
        }

    }

    private class ColumnEntry {

        String label;

        int displaySize;

        ColumnEntry(String label) {
            this.label = label;
            configure(label);
        }

        String getFormatPattern() {
            return "%-" + displaySize + "s";
        }

        void configure(Object value) {
            if (value == null) {
                return;
            } else if (value instanceof String) {
                displaySize = Math.max(displaySize, ((String) value).length());
            }

        }

    }

}
