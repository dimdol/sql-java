package com.dimdol.sql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// SELECT
// TODO TOP (PERCENT)
// TODO LIMIT

// WHERE
// TODO AND
// TODO OR
// TODO LIKE

// UPDATE

// DELETE

// INSERT INTO

// CREATE DATABASE

// ALTER DATABASE

// CREATE TABLE

// DROP TABLE

// CREATE INDEX

// DROP INDEX

// ��Ÿ
// TODO OFFSET FETCH

// JOIN
// TODO INNER, FULL, LEFT, RIGHT

// GROUP BY
// TODO COUNT, MIN, MAX, SUM, AVG �޼ҵ� ����. �� �޼ҵ带 ����ϸ� �ڵ����� GROUP BY ����

// ����Ŭ
// HINT, LEFT JOIN(+)
public class Sql<T> implements WhereClause {

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
        Sql<?> subquery = new Sql<>();
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
        Sql<?> subquery = new Sql<>();
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
         * sql.join(j -> { j.from("USER"); j.from("GROUP"); j.on("GROUP_ID",
         * "ID"); });
         * 
         * sql.join(Op.INNER_JOIN, j -> { j.from("USER"); j.from("GROUP");
         * j.on("GROUP_ID", "ID"); });
         */
    }

    @Override
    public void where(Op operator, String operand1, String operand2) {
        where(operator, Bind.PARAM, operand1, operand2);
    }

    @Override
    public void where(Op operator, Bind bind, String operand1, String operand2) {
        addCondition(new StringCondition(operator, bind, operand1, operand2));
    }

    @Override
    public void where(Op operator, SubqueryBuilder builder) {
        if (operator != Op.EXISTS && operator != Op.NOT_EXISTS) {
            throw new IllegalArgumentException();
        }

        Sql<?> subquery = new Sql<>();
        builder.build(subquery);
        addCondition(new SubqueryCondition(operator, subquery));
    }

    @Override
    public void where(Op operator, SubqueryBuilder builder, String operand1) {
        if (operator == Op.EXISTS || operator == Op.NOT_EXISTS) {
            throw new IllegalArgumentException();
        }

        Sql<?> subquery = new Sql<>();
        builder.build(subquery);
        addCondition(new SubqueryCondition(operator, subquery, operand1));
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

    public void intersect(SubqueryBuilder builder) {
        addSetQuery(Op.INTERSECT, builder);
    }

    private void addSetQuery(Op operator, SubqueryBuilder builder) {
        Sql<?> subquery = new Sql<>();
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
        }
    }

}