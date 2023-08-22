package com.bestTravel.api.controller.error_handler;

import com.bestTravel.api.model.response.ErrorResponse;
import com.bestTravel.util.exception.UserNameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@RestControllerAdvice
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNameNotFoundExceptionHandler {

    @ExceptionHandler(UserNameNotFoundException.class)
    public ErrorResponse userNameNotFoundException(UserNameNotFoundException e){
        return ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND.name())
                .code(HttpStatus.NOT_FOUND.value())
                .build();
    }
}
