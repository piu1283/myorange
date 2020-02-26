//package com.ood.myorange.config;
//
//import com.ood.myorange.dto.response.BaseResponse;
//import com.ood.myorange.exception.BaseRunTimeException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
///**
// * Created by Chen on 2/25/20.
// */
//@Slf4j
//@RestControllerAdvice
//@Order(value = 500)
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//    @ExceptionHandler(BaseRunTimeException.class)
//    public final ResponseEntity<BaseResponse<?>> definedExceptionHandler(BaseRunTimeException ex, WebRequest request) {
//        log.error("Exception(Defined) in Controller: ", ex);
//        BaseResponse<Object> br = BaseResponse.failure(ex.getMessage(), ex.getDetail());
//        return new ResponseEntity<>(br,new HttpHeaders(), ex.getStatus());
//    }
//
//    @ExceptionHandler(HttpClientErrorException.class)
//    protected ResponseEntity<BaseResponse<?>> handleExceptionClient(HttpClientErrorException ex) {
//        log.error("Exception(Unexpected) in Controller: ", ex);
//        BaseResponse<?> body = BaseResponse.failure(ex.getMessage());
//        System.out.println(ex.getStatusCode());
//        return new ResponseEntity<>(body, ex.getStatusCode());
//    }
//
//    @ExceptionHandler(Exception.class)
//    protected ResponseEntity<BaseResponse<?>> handleException(Exception ex) {
//        log.error("Exception(Unexpected) in Controller: ", ex);
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//        BaseResponse<?> body = BaseResponse.failure(ex.getMessage());
//        return new ResponseEntity<>(body,status);
//    }
//}


package com.ood.myorange.config;

import com.ood.myorange.dto.response.BaseResponse;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

/**
 *
 * @author magiccrafter
 */
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

    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse unknownException(Exception ex) {
        return BaseResponse.failure(ex.getMessage());
    }
}


