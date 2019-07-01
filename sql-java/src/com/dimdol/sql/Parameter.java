package com.dimdol.sql;

public interface Parameter {

    boolean prepared();

    Object[] getValues();

}