package com.ood.myorange.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Chen on 2/24/20.
 */
@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {
    private Object data;

    public InvalidRequestException(String msg) {
        super(msg);
    }

    public InvalidRequestException(String msg, Object data) {
        super(msg);
        this.data = data;
    }
}
