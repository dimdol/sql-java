package com.dimdol.sql.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.dimdol.example.Examples;

public class DataLoader {

    public static void run(Consumer<DataLoader> consumer, Runnable runnable) {
        DataLoader dataLoader = new DataLoader();
        try {
            consumer.accept(dataLoader);
            runnable.run();
        } finally {
            dataLoader.clear();
        }
    }

    private void clear() {
        tables.forEach(table -> {
            try {
                table.drop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private List<Table> tables = new ArrayList<>();

    public void table(String name, Consumer<Table> consumer) {
        Table table = new Table(name);
        tables.add(table);
        consumer.accept(table);
        table.load();
    }

    public static void main(String[] args) {
        Examples.configure();
        DataLoader.run(loader -> {
            loader.table("USERS", table -> {
                table.column("ID");
                table.column("NAME");
                table.record("A", "정대만");
            });
        }, () -> {
            System.out.println("만세");
        });
    }

}
