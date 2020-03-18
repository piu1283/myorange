package com.ood.myorange.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Chen on 2/24/20.
 */
@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerError extends RuntimeException {
    private Object data;

    public InternalServerError(String msg) {
        super(msg);
    }

    public InternalServerError(String msg, Object data) {
        super(msg);
        this.data = data;
    }
}
