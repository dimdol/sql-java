package com.dimdol.sql;

public enum Option {

    DEBUG_SQL(false);

    boolean booleanValue;

    private Option(boolean booleanValue) {
        this.booleanValue = booleanValue;
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

}
