package com.ood.myorange.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Chen on 2/24/20.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
@Getter
public class ForbiddenException extends RuntimeException {
    private Object data;

    public ForbiddenException(String msg) {
        super(msg);
    }

    public ForbiddenException(String msg, Object data) {
        super(msg);
        this.data = data;
    }
}
