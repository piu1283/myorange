package com.ood.myorange.util;

import lombok.experimental.UtilityClass;

/**
 * Created by Chen on 5/1/20.
 */
@UtilityClass
public class ShareUtil {
    private static final String URL_FORMAT = "http://34.204.85.79:8082/?key=%s";
    public static String generateShareUrl(String shareKey) {
        return String.format(URL_FORMAT, shareKey);
    }

    public static void main(String[] args) {
        System.out.println(generateShareUrl("123"));
    }
}
