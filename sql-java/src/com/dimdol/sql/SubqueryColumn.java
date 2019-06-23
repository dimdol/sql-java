package com.dimdol.sql;

public class SubqueryColumn extends Column {

    private Sql<?> subquery;

    public SubqueryColumn(Sql<?> subquery, String alias) {
        super(null, alias);
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