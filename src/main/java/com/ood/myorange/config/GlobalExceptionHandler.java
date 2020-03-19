package com.ood.myorange.config;

import com.ood.myorange.dto.response.BaseResponse;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { InvalidRequestException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse constraintViolationException(ConstraintViolationException ex) {
        return BaseResponse.failure(ex.getMessage());
    }

    @ExceptionHandler(value = { ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse ResourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        return BaseResponse.failure(ex.getMessage());
    }

    @ExceptionHandler(value = { NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse noHandlerFoundExceptionHandler(Exception ex) {
        return BaseResponse.failure(ex.getMessage());
    }

    @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public BaseResponse HttpRequestMethodNotSupportedExceptionHandler(Exception ex) {
        return BaseResponse.failure(ex.getMessage());
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse AccessDeniedExceptionExceptionHandler(Exception ex) {
        return BaseResponse.failure(ex.getMessage());
    }

    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse unknownException(Exception ex) {
        return BaseResponse.failure(ex.getMessage());
    }
}


