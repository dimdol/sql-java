package com.dimdol.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

        ConsoleData cd = new ConsoleData();
        cd.setSorter(comparator);
        cd.setFormatter(formatter);
        for (SqlEntry sqlEntry : sqlEntries) {
            sqlEntry.sql.each(sqlEntry.connectionType, (i, rs) -> {
                if (i == 0) {
                    configureColumnEntries(cd, rs);
                }
                cd.addValue(rs);
            }, sqlEntry.limit);
        }
        cd.log();
    }

    public void desc(Enum<?> tableName) {
        desc(tableName.toString());
    }

    public void desc(String tableName) {
        try (Connection con = SqlRuntime.getInstance().getConnectionFactory().getConnection()) {
            desc(con, tableName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void desc(Enum<?> connectionType, Enum<?> tableName) {
        desc(connectionType, tableName.toString());
    }

    public void desc(Enum<?> connectionType, String tableName) {
        try (Connection con = SqlRuntime.getInstance().getConnectionFactory().getConnection(connectionType)) {
            desc(con, tableName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void desc(Connection con, Enum<?> tableName) {
        desc(con, tableName.toString());
    }

    public void desc(Connection con, String tableName) {
        Sql sql = new Sql();
        sql.select("*");
        sql.from(tableName);
        sql.handle(con, pstmt -> {
            ConsoleData cd = new ConsoleData();
            cd.addEntry("NAME");
            cd.addEntry("TYPE");
            cd.addEntry("PRECISION");
            cd.addEntry("SCALE");
            cd.addEntry("IS_NULL");
            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int size = rsmd.getColumnCount();
                for (int i = 1; i <= size; i++) {
                    Map<String, Object> value = new HashMap<>();
                    value.put("NAME", rsmd.getColumnName(i));
                    value.put("TYPE", rsmd.getColumnTypeName(i));
                    value.put("PRECISION", rsmd.getPrecision(i));
                    value.put("SCALE", rsmd.getScale(i));
                    if (rsmd.isNullable(i) == ResultSetMetaData.columnNoNulls) {
                        value.put("IS_NULL", "IS NOT NULL");
                    } else {
                        value.put("IS_NULL", "");
                    }
                    cd.addValue(value);
                }
            }
            cd.log();
        });
    }

    private void configureColumnEntries(ConsoleData cd, ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int size = rsmd.getColumnCount();
        for (int columnIndex = 1; columnIndex <= size; columnIndex++) {
            String columnLabel = rsmd.getColumnLabel(columnIndex);
            cd.addEntry(columnLabel);
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

}
