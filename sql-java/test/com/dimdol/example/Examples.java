package com.dimdol.example;

import com.dimdol.sql.Option;
import com.dimdol.sql.Sqls;

public class Examples {

    public static void configure() {
        Sqls.configure(runtime -> {
            runtime.setConnectionFactory(new OracleConnectionFactory());
        });
        Option.DEBUG_SQL.enable();
    }

}