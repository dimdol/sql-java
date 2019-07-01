package com.dimdol.sql;

import java.sql.SQLException;

@FunctionalInterface
public interface ResultEach<T> {

    void accept(int index, T t) throws SQLException;

}