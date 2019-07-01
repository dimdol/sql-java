package com.dimdol.sql;

import java.util.function.Consumer;

public final class Sqls {

    public static final void configure(Consumer<SqlRuntime> consumer) {
        consumer.accept(SqlRuntime.getInstance());
    }

}
