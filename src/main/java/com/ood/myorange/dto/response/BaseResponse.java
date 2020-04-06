package com.ood.myorange.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.sql.DatabaseMetaData;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Chen on 2/24/20.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    private String msg;
    private Object resultData;
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    private BaseResponse(String msg, Object data) {
        this.msg = msg;
        this.resultData = data;
    }

    private BaseResponse(Object data) {
        this.msg = "";
        this.resultData = data;
    }

    public static BaseResponse success() {
        return new BaseResponse(HttpStatus.OK.getReasonPhrase(), null);
    }

    public static BaseResponse success(Object data) {
        return new BaseResponse(HttpStatus.OK.getReasonPhrase(), data);
    }

    public static BaseResponse process(Object data) {
        return new BaseResponse(data);
    }

    public static  BaseResponse success(String msg, Object data) {
        return new BaseResponse(msg, data);
    }

    public static BaseResponse failure() {
        return new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
    }

    public static  BaseResponse failure(String msg) {
        return new BaseResponse(msg, null);
    }

    public static BaseResponse failure(String msg, Object data) {
        return new BaseResponse(msg, data);
    }

}
