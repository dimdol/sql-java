package com.dimdol.sql;

class Alias {

    private String alias;

    private boolean forTable;

    Alias(String alias) {
        this(alias, false);
    }

    Alias(String alias, boolean forTable) {
        if (alias == null) {
            throw new NullPointerException();
        }
        // It requires double quotation marks or square brackets if the column
        // name contains spaces
        int i = alias.indexOf(" ");
        if (i == -1) {
            this.alias = alias;
        } else {
            this.alias = "\"" + alias + "\"";
        }
        this.forTable = forTable;
    }

    String toSql() {
        return forTable ? " " + alias : " AS " + alias;
    }

}