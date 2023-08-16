package com.bestTravel.util.exception;

/**
 * @author claudio.vilas
 * date: 08/2023
 */
public class IdNotFoundException extends RuntimeException{
    private static final String ERROR_MSG = "record no exist in %s";
    public IdNotFoundException(String tableName) {
        super(String.format(ERROR_MSG, tableName));
    }
}
