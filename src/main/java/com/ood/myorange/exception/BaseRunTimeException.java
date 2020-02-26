//package com.ood.myorange.exception;
//
//import lombok.AccessLevel;
//import lombok.Data;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.http.HttpStatus;
//
///**
// * Created by Chen on 2/24/20.
// */
//@Setter(AccessLevel.PROTECTED)
//@Getter
//public class BaseRunTimeException extends RuntimeException {
//    private HttpStatus status;
//    private Object detail;
//    protected BaseRunTimeException() {
//    }
//    protected BaseRunTimeException(String msg) {
//        super(msg);
//    }
//    protected BaseRunTimeException(String msg, Object obj) {
//        super(msg);
//        this.detail = obj;
//    }
//}
