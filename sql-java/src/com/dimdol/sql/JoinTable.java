package com.dimdol.sql;

import java.util.ArrayList;
import java.util.List;

public class JoinTable extends Table {

    private final Keyword joinType;

    private final List<JoinCondition> joinConditions = new ArrayList<>();

    JoinTable(String name, String alias, Keyword joinType) {
        super(name, alias);
        if (joinType == null) {
            throw new NullPointerException();
        }
        if (!joinType.isJoin()) {
            throw new IllegalArgumentException();
        }
        this.joinType = joinType;
    }

    public void and(String column1, String column2) {
        add(Keyword.AND, column1, column2);
    }

    public void or(String column1, String column2) {
        add(Keyword.OR, column1, column2);
    }

    private void add(Keyword keyword, String column1, String column2) {
        joinConditions.add(new JoinCondition(joinConditions.size(), keyword, column1, column2));
    }

    @Override
    public void writeSql(SqlCodeBuilder builder) {
        builder.append(joinType.toString());
        builder.append(" ");
        super.writeSql(builder);
        for (JoinCondition each : joinConditions) {
            builder.append(" ");
            each.writeSql(builder);
        }
    }

    private class JoinCondition extends Unit {

        int order;

        Keyword keyword;

        String column1;

        String column2;

        JoinCondition(int order, Keyword keyword, String column1, String column2) {
            if (keyword != Keyword.AND && keyword != Keyword.OR) {
                throw new IllegalArgumentException();
            }
            this.order = order;
            this.keyword = keyword;
            this.column1 = column1;
            this.column2 = column2;
        }

        @Override
        void writeSql(SqlCodeBuilder builder) {
            if (order == 0) {
                builder.append(Keyword.ON.toString());
            } else {
                builder.append(keyword.toString());
            }
            builder.append(String.format(" %s = %s", column1, column2));
        }

    }

}