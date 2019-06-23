package com.dimdol.sql;

class SubqueryTable extends Table {

    private Sql<?> subquery;

    SubqueryTable(Sql<?> subquery) {
        super(null);
        this.subquery = subquery;
    }

    SubqueryTable(Sql<?> subquery, String alas) {
        super(null, alas);
        this.subquery = subquery;
    }

    @Override
    public void writeSql(SqlCodeBuilder builder) {
        builder.append("(");
        builder.append(subquery);
        builder.append(")");
        Alias alias = getAlias();
        if (alias != null) {
            builder.append(alias.toSql());
        }
    }

}