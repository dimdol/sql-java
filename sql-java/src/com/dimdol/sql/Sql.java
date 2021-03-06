package com.dimdol.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Sql implements WhereClause {

    private Set<Option> options = new HashSet<>();

    private InsertInto insert;

    private UpdateSet update;

    private DeleteFrom delete;

    private List<Column> columns;

    private List<Table> tables;

    private List<Condition> conditions;

    private List<SetQuery> setQueries;

    private List<OrderBy> orders;

    private GroupBy groupBy;

    private WhereClause having;

    private SqlCodeBuilder codeBuilder;

    private AtomicInteger fetchCount = new AtomicInteger();

    public Sql(Option... options) {
        if (options != null) {
            for (Option each : options) {
                this.options.add(each);
            }
        }
    }

    public void insertInto(String tableName) {
        insert = new InsertInto(tableName);
    }

    public void value(String columnName, Object value) {
        value(Bind.PARAM, columnName, value);
    }

    public void value(Bind bind, String columnName, Object value) {
        // TODO insert가 null 일 때의 처리
        insert.value(bind, columnName, value);
    }

    public void update(String tableName) {
        update = new UpdateSet(tableName);
    }

    public void set(String columnName, Object value) {
        set(Bind.PARAM, columnName, value);
    }

    public void set(Bind bind, String columnName, Object value) {
        // TODO update가 null 일 때의 처리
        update.set(bind, columnName, value);
    }

    public void deleteFrom(String tableName) {
        delete = new DeleteFrom(tableName);
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

    public void from(Enum<?> tableName) {
        from(tableName.toString());
    }

    public void from(Enum<?> tableName, String alias) {
        from(tableName.toString(), alias);
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

    public void join(String tableName, String alias, String column1, String column2) {
        join(tableName, alias, Keyword.JOIN, column1, column2);
    }

    public void join(String tableName, String alias, Consumer<JoinTable> on) {
        join(tableName, alias, Keyword.JOIN, on);
    }

    public void leftJoin(String tableName, String alias, String column1, String column2) {
        join(tableName, alias, Keyword.LEFT_JOIN, column1, column2);
    }

    public void leftJoin(String tableName, String alias, Consumer<JoinTable> on) {
        join(tableName, alias, Keyword.LEFT_JOIN, on);
    }

    public void rightJoin(String tableName, String alias, String column1, String column2) {
        join(tableName, alias, Keyword.RIGHT_JOIN, column1, column2);
    }

    public void rightJoin(String tableName, String alias, Consumer<JoinTable> on) {
        join(tableName, alias, Keyword.RIGHT_JOIN, on);
    }

    public void fullJoin(String tableName, String alias, String column1, String column2) {
        join(tableName, alias, Keyword.FULL_OUTER_JOIN, column1, column2);
    }

    public void fullJoin(String tableName, String alias, Consumer<JoinTable> on) {
        join(tableName, alias, Keyword.FULL_OUTER_JOIN, on);
    }

    private void join(String tableName, String alias, Keyword joinType, String column1, String column2) {
        join(tableName, alias, joinType, on -> {
            on.and(column1, column2);
        });
    }

    private void join(String tableName, String alias, Keyword joinType, Consumer<JoinTable> on) {
        JoinTable table = new JoinTable(tableName, alias, joinType);
        on.accept(table);
        tables.add(table);
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
    public void or(Consumer<CompositeCondition> or) {
        CompositeCondition condition = new CompositeCondition(Op.OR);
        or.accept(condition);
        addCondition(condition);
    }

    @Override
    public void and(Consumer<CompositeCondition> and) {
        CompositeCondition condition = new CompositeCondition(Op.AND);
        and.accept(condition);
        addCondition(condition);
    }

    @Override
    public void not(Consumer<CompositeCondition> not) {
        CompositeCondition condition = new CompositeCondition(Op.NOT);
        not.accept(condition);
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

    public void groupBy(String... columnNames) {
        this.groupBy = new GroupBy(columnNames);
    }

    public void having(Consumer<WhereClause> where) {
        this.having = new Sql();
        where.accept(having);
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
        if (insert != null) {
            insert.writeSql(codeBuilder);
        } else if (update != null) {
            update.writeSql(codeBuilder);
            if (conditions != null) {
                codeBuilder.build("WHERE", options.contains(Option.OR) ? " OR" : " AND", conditions);
            }
        } else if (delete != null) {
            delete.writeSql(codeBuilder);
            if (conditions != null) {
                codeBuilder.build("WHERE", options.contains(Option.OR) ? " OR" : " AND", conditions);
            }
        } else if (setQueries == null || setQueries.isEmpty()) {
            if (columns != null) {
                codeBuilder.build(options.contains(Option.DISTINCT) ? "SELECT DISTINCT" : "SELECT", ",", columns);
            }
            if (tables != null) {
                codeBuilder.build("FROM", ",", tables);
            }
            if (conditions != null) {
                codeBuilder.build("WHERE", options.contains(Option.OR) ? " OR" : " AND", conditions);
            }
            if (groupBy != null) {
                codeBuilder.append(" ");
                groupBy.writeSql(codeBuilder);
            }
            if (having != null) {
                codeBuilder.build("HAVING", " AND", ((Sql) having).conditions);
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

    public int execute() {
        try (Connection con = SqlRuntime.getInstance().getConnectionFactory().getConnection()) {
            return execute(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int execute(Connection connection) {
        AtomicInteger result = new AtomicInteger();
        handle(connection, (pstmt) -> {
            result.set(pstmt.executeUpdate());
        });
        return result.intValue();
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
        handle(connection, (pstmt) -> {
            try (ResultSet rs = pstmt.executeQuery()) {
                int i = 0;
                while (rs.next()) {
                    consumer.accept(i++, rs);
                    fetchCount.incrementAndGet();
                    if (i >= limit) {
                        break;
                    }
                }
            }
        });
    }

    void handle(PreparedStatementHandler handler) {
        try (Connection con = SqlRuntime.getInstance().getConnectionFactory().getConnection()) {
            handle(con, handler);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void handle(Connection connection, PreparedStatementHandler handler) {
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
                handler.accept(pstmt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getFetchCount() {
        return fetchCount.get();
    }

}