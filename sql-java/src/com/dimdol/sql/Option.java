package com.dimdol.sql;

public enum Option {

    DISTINCT,

    DEBUG_SQL(false),

    MAX_FETCH_LIMT(1000);

    private boolean booleanValue;

    private int intValue;

    private Option() {
    }

    private Option(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    private Option(int intValue) {
        this.intValue = intValue;
    }

    public void enable() {
        booleanValue = true;
    }

    public void disable() {
        booleanValue = false;
    }

    public boolean on() {
        return booleanValue;
    }

    public boolean off() {
        return !on();
    }

    public int getInt() {
        return intValue;
    }

    public void set(int intValue) {
        this.intValue = intValue;
    }

}
