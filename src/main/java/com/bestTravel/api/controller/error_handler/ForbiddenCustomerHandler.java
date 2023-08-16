package com.bestTravel.api.controller.error_handler;


import com.bestTravel.api.model.response.ErrorResponse;
import com.bestTravel.util.exception.ForbidenCustomerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@RestControllerAdvice()
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenCustomerHandler {

    @ExceptionHandler(ForbidenCustomerException.class)
        public ErrorResponse forbidenCustomerHandler(ForbidenCustomerException exception){
        return ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.name())
                .code(HttpStatus.FORBIDDEN.value())
                .message(exception.getMessage())
                .build();
    }
}
