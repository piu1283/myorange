package com.ood.myorange.util;

/**
 * Created by Chen on 3/30/20.
 */
public class SizeUtil {
    public static String getPrintSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            return size + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            size = size * 100;
            return size / 100 + "." + size % 100 + "MB";
        } else {
            size = size * 100 / 1024;
            return size / 100 + "."
                    + size % 100 + "GB";
        }
    }
}
