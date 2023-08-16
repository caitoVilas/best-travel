package com.bestTravel.util.exception;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

public class ForbidenCustomerException extends RuntimeException{
    public ForbidenCustomerException() {
        super("this customer is blocked");
    }
}
