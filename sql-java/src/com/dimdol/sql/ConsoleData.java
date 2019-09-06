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

class ConsoleData {

    private Map<String, ColumnEntry> columnEntries = new LinkedHashMap<>();

    private List<Map<String, Object>> values = new ArrayList<>();

    private Comparator<Map<String, Object>> comparator;

    private BiFunction<String, Object, Object> formatter;

    ConsoleData() {
    }

    ConsoleData(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        for (int i = 1; i <= count; i++) {
            addEntry(rsmd.getColumnLabel(i));
        }
        while (rs.next()) {
            addValue(rs);
        }
    }

    void setSorter(Comparator<Map<String, Object>> comparator) {
        this.comparator = comparator;
    }

    void setFormatter(BiFunction<String, Object, Object> formatter) {
        this.formatter = formatter;
    }

    void addEntry(String columnLabel) {
        if (!columnEntries.containsKey(columnLabel)) {
            columnEntries.put(columnLabel, new ColumnEntry(columnLabel));
        }
    }

    void addValue(ResultSet rs) {
        Map<String, Object> value = new HashMap<>();
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
                value.put(columnEntry.label, object);
            }
            columnEntry.configure(object);
        }
        values.add(value);
    }

    void addValue(Map<String, Object> value) {
        for (ColumnEntry columnEntry : columnEntries.values()) {
            Object object = value.get(columnEntry.label);
            if (object != null) {
                if (formatter != null) {
                    object = formatter.apply(columnEntry.label, object);
                    value.put(columnEntry.label, object);
                }
            }
            columnEntry.configure(object);
        }
        values.add(value);
    }

    void log() {
        String linePattern = getLinePattern();
        header(linePattern);
        values(linePattern);
    }

    private String getLinePattern() {
        StringBuilder result = new StringBuilder();
        for (ColumnEntry columnEntry : columnEntries.values()) {
            if (result.length() > 0) {
                result.append("    ");
            }
            result.append(columnEntry.getFormatPattern());
        }
        result.append("\n");
        return result.toString();
    }

    private void header(String linePattern) {
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

    private void values(String linePattern) {
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
                displaySize = Math.max(displaySize, 4);
            } else if (value instanceof String) {
                displaySize = Math.max(displaySize, ((String) value).length());
            } else {
                displaySize = Math.max(displaySize, (value.toString()).length());
            }

        }

    }

}
