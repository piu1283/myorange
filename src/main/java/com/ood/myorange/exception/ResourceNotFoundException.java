package com.ood.myorange.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

/**
 * Created by Chen on 2/24/20.
 */
@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private Map<String, List<String>> data;

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    public ResourceNotFoundException(String msg, List<String> resourceName) {
        super(msg);
        if (!CollectionUtils.isEmpty(resourceName)) {
            data.put("resource", resourceName);
        }
    }
}
