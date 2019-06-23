package com.dimdol.sql;

class Alias {

    private String alias;

    Alias(String alias) {
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
    }

    String toSql() {
        return " AS " + alias;
    }

}