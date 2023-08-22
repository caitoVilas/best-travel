package com.bestTravel.util.exception;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

public class UserNameNotFoundException extends RuntimeException{
    private static final String ERROR_MESSAGE = "user no exits in %s";

    public UserNameNotFoundException(String message) {
        super(String.format(ERROR_MESSAGE, message));
    }
}
