package com.dimdol.sql;

public enum Keyword {

    ADD("ADD"),

    ADD_CONSTRAINT("ADD CONSTRAINT"),

    ALTER("ALTER"),

    ALTER_COLUMN("ALTER COLUMN"),

    ALTER_TABLE("ALTER TABLE"),

    ALL("ALL"),

    AND("AND"),

    AS("AS"),

    ASC("ASC"),

    BACKUP_DATABASE("BACKUP DATABASE"),

    BETWEEN("BETWEEN"),

    CASE("CASE"),

    CHECK("CHECK"),

    COLUMN("COLUMN"),

    CONSTRAINT("CONSTRAINT"),

    CREATE("CREATE"),

    CREATE_DATABASE("CREATE DATABASE"),

    CREATE_INDEX("CREATE INDEX"),

    CREATE_OR_REPLACE_VIEW("CREATE OR REPLACE VIEW"),

    CREATE_TABLE("CREATE TABLE"),

    CREATE_PROCEDURE("CREATE PROCEDURE"),

    CREATE_UNIQUE_INDEX("CREATE UNIQUE INDEX"),

    CREATE_VIEW("CREATE VIEW"),

    DATABASE("DATABASE"),

    DEFAULT("DEFAULT"),

    DELETE("DELETE"),

    DESC("DESC"),

    DISTINCT("DISTINCT"),

    DROP("DROP"),

    DROP_COLUMN("DROP COLUMN"),

    DROP_CONSTRAINT("DROP CONSTRAINT"),

    DROP_DATABASE("DROP DATABASE"),

    DROP_DEFAULT("DROP DEFAULT"),

    DROP_INDEX("DROP INDEX"),

    DROP_TABLE("DROP TABLE"),

    DROP_VIEW("DROP VIEW"),

    EXEC("EXEC"),

    EXISTS("EXISTS"),

    FOREIGN_KEY("FOREIGN KEY"),

    FROM("FROM"),

    FULL_OUTER_JOIN("FULL OUTER JOIN"),

    GROUP_BY("GROUP BY"),

    HAVING("HAVING"),

    IN("IN"),

    INDEX("INDEX"),

    INNER_JOIN("INNER JOIN"),

    INSERT_INTO("INSERT INTO"),

    INSERT_INTO_SELECT("INSERT INTO SELECT"),

    IS_NULL("IS NULL"),

    IS_NOT_NULL("IS NOT NULL"),

    JOIN("JOIN"),

    LEFT_JOIN("LEFT JOIN"),

    LIKE("LIKE"),

    LIMIT("LIMIT"),

    NOT("NOT"),

    NOT_NULL("NOT NULL"),

    ON("ON"),

    OR("OR"),

    ORDER_BY("ORDER BY"),

    OUTER_JOIN("OUTER JOIN"),

    PRIMARY_KEY("PRIMARY KEY"),

    PROCEDURE("PROCEDURE"),

    RIGHT_JOIN("RIGHT JOIN"),

    ROWNUM("ROWNUM"),

    SELECT("SELECT"),

    SELECT_DISTINCT("SELECT DISTINCT"),

    SELECT_INTO("SELECT INTO"),

    SELECT_TOP("SELECT TOP"),

    SET("SET"),

    TABLE("TABLE"),

    TOP("TOP"),

    TRUNCATE_TABLE("TRUNCATE TABLE"),

    UNION("UNION"),

    UNION_ALL("UNION ALL"),

    UNIQUE("UNIQUE"),

    UPDATE("UPDATE"),

    VALUES("VALUES"),

    VIEW("VIEW"),

    WHERE("WHERE");

    private final String keyword;

    private Keyword(String keyword) {
        this.keyword = keyword;
    }

    boolean isJoin() {
        return this == JOIN || this == LEFT_JOIN || this == RIGHT_JOIN || this == Keyword.FULL_OUTER_JOIN || this == Keyword.INNER_JOIN;
    }

    @Override
    public String toString() {
        return keyword;
    }

}
