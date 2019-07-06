package com.dimdol.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class SqlCodeBuilder {

    private StringBuilder code = new StringBuilder();

    private List<Parameter> parameters;

    void build(String prefix, String separator, List<? extends Unit> units) {
        if (units == null || units.isEmpty()) {
            return;
        }
        addSpace();
        append(prefix);
        int size = units.size();
        for (int i = 0; i < size; i++) {
            Unit each = units.get(i);
            if (i > 0 && !(each instanceof JoinTable)) {
                append(separator);
            }
            addSpace();
            each.writeSql(this);
            if (each instanceof Parameter) {
                Parameter parameter = (Parameter) each;
                if (parameter.prepared()) {
                    if (parameters == null) {
                        parameters = new ArrayList<>();
                    }
                    parameters.add(parameter);
                }
            }
        }
    }

    private void addSpace() {
        int length = code.length();
        if (length > 0) {
            char c = code.charAt(length - 1);
            if (c != ' ' && c != '(') {
                append(" ");
            }
        }
    }

    String getCode() {
        return code.toString();
    }

    void append(String str) {
        code.append(str);
    }

    void append(Sql sql) {
        sql.buildQuery(this);
    }

    void append(Op operator, Sql sql) {
        addSpace();
        append(operator.toSql());
        sql.buildQuery(this);
    }

    List<Parameter> getParameters() {
        return parameters == null ? Collections.emptyList() : parameters;
    }

}