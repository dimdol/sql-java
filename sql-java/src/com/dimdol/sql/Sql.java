package com.dimdol.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Sql implements WhereClause {

    private Op whereMode;

    private Set<Syntax> syntaxes = new HashSet<>();

    private List<Column> columns;

    private List<Table> tables;

    private List<Condition> conditions;

    private List<SetQuery> setQueries;

    private List<OrderBy> orders;

    private SqlCodeBuilder codeBuilder;

    public Sql() {
        this(Op.AND);
    }

    public Sql(Op whereMode) {
        this.whereMode = whereMode;
    }

    public void distinct() {
        syntaxes.add(Syntax.DISTINCT);
    }

    public void selectAll() {
        select("*");
    }

    public void select(String columnName) {
        addColumn(new Column(columnName));
    }

    public void select(String columnName, String alias) {
        addColumn(new Column(columnName, alias));
    }

    public void select(SubqueryBuilder builder) {
        select(builder, null);
    }

    public void select(SubqueryBuilder builder, String alias) {
        Sql subquery = new Sql();
        builder.build(subquery);
        addColumn(new SubqueryColumn(subquery, alias));
    }

    private void addColumn(Column column) {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        columns.add(column);
    }

    public void from(String tableName) {
        addTable(new Table(tableName));
    }

    public void from(String tableName, String alias) {
        addTable(new Table(tableName, alias));
    }

    public void from(SubqueryBuilder builder) {
        from(builder, null);
    }

    public void from(SubqueryBuilder builder, String alias) {
        Sql subquery = new Sql();
        builder.build(subquery);
        addTable(new SubqueryTable(subquery, alias));
    }

    private void addTable(Table table) {
        if (tables == null) {
            tables = new ArrayList<>();
        }
        tables.add(table);
    }

    public void join() {
        /*
         * sql.join(j -> { j.from("USER"); j.from("GROUP"); j.on("GROUP_ID", "ID"); });
         * 
         * sql.join(Op.INNER_JOIN, j -> { j.from("USER"); j.from("GROUP"); j.on("GROUP_ID", "ID"); });
         */
    }

    @Override
    public void where(String columnName, Op operator) {
        addCondition(new UnaryCondition(operator, columnName));
    }

    @Override
    public void where(String columnName, Op operator, Object... values) {
        where(columnName, operator, Bind.PARAM, values);
    }

    @Override
    public void where(String columnName, Op operator, Bind bind, Object... values) {
        addCondition(new TernaryCondition(operator, bind, columnName, values));
    }

    @Override
    public void where(Op operator, SubqueryBuilder builder) {
        if (operator != Op.EXISTS && operator != Op.NOT_EXISTS) {
            throw new IllegalArgumentException();
        }

        Sql subquery = new Sql();
        builder.build(subquery);
        addCondition(new SubqueryCondition(operator, subquery));
    }

    @Override
    public void where(String columnName, Op operator, SubqueryBuilder builder) {
        if (operator == Op.EXISTS || operator == Op.NOT_EXISTS) {
            throw new IllegalArgumentException();
        }

        Sql subquery = new Sql();
        builder.build(subquery);
        addCondition(new SubqueryCondition(operator, subquery, columnName));
    }

    @Override
    public void where(String columName, Op operator, Op anyOrAll, SubqueryBuilder builder) {
        Sql subquery = new Sql();
        builder.build(subquery);
        addCondition(new SubqueryCondition(columName, operator, anyOrAll, subquery));
    }

    private void addCondition(Condition condition) {
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        conditions.add(condition);
    }

    @Override
    public void or(CompositeConditionBuilder builder) {
        CompositeCondition condition = new CompositeCondition(Op.OR);
        builder.build(condition);
        addCondition(condition);
    }

    @Override
    public void and(CompositeConditionBuilder builder) {
        CompositeCondition condition = new CompositeCondition(Op.AND);
        builder.build(condition);
        addCondition(condition);
    }

    @Override
    public void not(CompositeConditionBuilder builder) {
        CompositeCondition condition = new CompositeCondition(Op.NOT);
        builder.build(condition);
        addCondition(condition);
    }

    public void union(SubqueryBuilder builder) {
        addSetQuery(Op.UNION, builder);
    }

    public void unionAll(SubqueryBuilder builder) {
        addSetQuery(Op.UNION_ALL, builder);
    }

    public void except(SubqueryBuilder builder) {
        addSetQuery(Op.EXCEPT, builder);
    }

    public void minus(SubqueryBuilder builder) {
        addSetQuery(Op.MINUS, builder);
    }

    public void intersect(SubqueryBuilder builder) {
        addSetQuery(Op.INTERSECT, builder);
    }

    private void addSetQuery(Op operator, SubqueryBuilder builder) {
        Sql subquery = new Sql();
        builder.build(subquery);
        if (setQueries == null) {
            setQueries = new ArrayList<>();
        }
        setQueries.add(new SetQuery(operator, subquery));
    }

    public void orderBy(String orderBy) {
        if (orders == null) {
            orders = new ArrayList<>();
        }
        orders.add(new OrderBy(orderBy));
    }

    public void orderBy(String orderBy, Op op) {
        if (op != Op.ASC && op != Op.DESC) {
            throw new IllegalArgumentException();
        }
        orderBy(orderBy + " " + op.toSql());
    }

    public String toSql() {
        return getSqlCodeBuilder().getCode();
    }

    public List<Parameter> getParameters() {
        return getSqlCodeBuilder().getParameters();
    }

    private SqlCodeBuilder getSqlCodeBuilder() {
        if (codeBuilder == null) {
            codeBuilder = new SqlCodeBuilder();
            buildQuery(codeBuilder);
        }
        return codeBuilder;
    }

    void buildQuery(SqlCodeBuilder codeBuilder) {
        if (setQueries == null || setQueries.isEmpty()) {
            if (columns != null) {
                codeBuilder.build(syntaxes.contains(Syntax.DISTINCT) ? "SELECT DISTINCT" : "SELECT", ",", columns);
            }
            if (tables != null) {
                codeBuilder.build("FROM", ",", tables);
            }
            if (conditions != null) {
                codeBuilder.build("WHERE", " " + whereMode.toSql(), conditions);
            }
            if (orders != null) {
                codeBuilder.build("ORDER BY", ",", orders);
            }
        } else {
            int i = 0;
            int size = setQueries.size();
            if (size > 1) {
                codeBuilder.append("(");
            }
            for (SetQuery each : setQueries) {
                if (i++ == 0) {
                    codeBuilder.append(each.getSql());
                } else {
                    codeBuilder.append(" " + each.getOperator().toSql() + " ");
                    codeBuilder.append(each.getSql());
                }
            }
            if (size > 1) {
                codeBuilder.append(")");
            }
            if (orders != null) {
                codeBuilder.build("ORDER BY", ",", orders);
            }
        }
    }

    public void each(ResultEach<ResultSet> consumer) {
        each(consumer, Option.MAX_FETCH_LIMT.getInt());
    }

    public void each(ResultEach<ResultSet> consumer, int limit) {
        try (Connection con = SqlRuntime.getInstance().getConnectionFactory().getConnection()) {
            each(con, consumer, limit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void each(Enum<?> connectionType, ResultEach<ResultSet> consumer) {
        each(connectionType, consumer, Option.MAX_FETCH_LIMT.getInt());
    }

    public void each(Enum<?> connectionType, ResultEach<ResultSet> consumer, int limit) {
        try (Connection con = SqlRuntime.getInstance().getConnectionFactory().getConnection(connectionType)) {
            each(con, consumer, limit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void each(Connection connection, ResultEach<ResultSet> consumer) {
        each(connection, consumer, Option.MAX_FETCH_LIMT.getInt());
    }

    public void each(Connection connection, ResultEach<ResultSet> consumer, int limit) {
        if (connection == null) {
            throw new NullPointerException();
        }

        try {
            if (connection.isClosed()) {
                throw new RuntimeException("java.sql.Connection is closed");
            }
            String sql = toSql();
            if (Option.DEBUG_SQL.on()) {
                System.out.println(sql);
            }
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                int index = 0;
                for (Parameter each : getParameters()) {
                    if (each.prepared()) {
                        for (Object value : each.getValues()) {
                            pstmt.setObject(++index, value);
                        }
                    }
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    int i = 0;
                    while (rs.next()) {
                        consumer.accept(i++, rs);
                        if (i >= limit) {
                            break;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}