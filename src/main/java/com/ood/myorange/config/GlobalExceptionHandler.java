package com.ood.myorange.config;

import com.ood.myorange.dto.response.BaseResponse;
import com.ood.myorange.exception.ForbiddenException;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {InvalidRequestException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse InvalidRequestExceptionExceptionHandler(InvalidRequestException ex) {
        log.warn("Invalid request.", ex);
        return BaseResponse.failure(ex.getMessage(), ex.getData());
    }

    @ExceptionHandler(value = { ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse constraintViolationException(Exception ex) {
        log.warn("Invalid request.", ex);
        return BaseResponse.failure(ex.getMessage());
    }

    @ExceptionHandler(value = { ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse ResourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        log.warn("Resource Not found or unable to accessed.", ex);
        return BaseResponse.failure(ex.getMessage(), ex.getData());
    }

    @ExceptionHandler(value = {ForbiddenException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse ForbiddenExceptionExceptionHandler(ForbiddenException ex) {
        log.error("Exception:: ", ex);
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
        log.error("Exception:: ", ex);
        return BaseResponse.failure(ex.getMessage());
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse AccessDeniedExceptionExceptionHandler(Exception ex) {
        log.error("Exception:: ", ex);
        return BaseResponse.failure(ex.getMessage());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse IllegalArgumentExceptionExceptionHandler(Exception ex) {
        log.error("Exception:: ", ex);
        return BaseResponse.failure("Bad request parameters.");
    }

    @ExceptionHandler(value = {NullPointerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse NullPointerExceptionExceptionHandler(Exception ex) {
        log.error("Exception:: ", ex);
        return BaseResponse.failure("Internal error.");
    }

    @ExceptionHandler(value = {DataAccessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse DataAccessExceptionHandler(DataAccessException ex) {
        log.error("Exception:: ", ex);
        if (ex.getCause() instanceof SQLIntegrityConstraintViolationException) {
            return BaseResponse.failure("Duplicate data exist");
        }
        return unknownException(ex);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse httpMessageNotReadableOrMissingExceptionHandler(Exception ex) {
        log.error("Exception: ", ex);
        return BaseResponse.failure(ex.getLocalizedMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse unknownException(Exception ex) {
        log.error("Unknown Exception:", ex);
        return BaseResponse.failure(ex.getMessage());
    }

}


