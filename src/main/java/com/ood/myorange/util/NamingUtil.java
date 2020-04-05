package com.ood.myorange.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Chen on 4/1/20.
 */
@UtilityClass
public class NamingUtil {
    public static boolean validDirName(String dirName) {
        if (StringUtils.isBlank(dirName)) {
            return false;
        }
        return dirName.matches("^[^\\\\\\/<>'\",#·.￥…%!@$^*?&]+$");
    }

    public static boolean validFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return false;
        }
        return fileName.matches("^[^\\\\\\/<>'\",#·￥…%!@$^*?&]+$");
    }

    public static void main(String[] args) {
        System.out.println(validFileName("/"));
        System.out.println(validFileName("12erqw"));
        System.out.println(validFileName("_sd&"));
        System.out.println(validDirName("sdf/ll"));
        System.out.println(validFileName("^&!d?sdf"));
    }
}
