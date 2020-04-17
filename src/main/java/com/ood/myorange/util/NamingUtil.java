package com.ood.myorange.util;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    public static String generateOriginFileId(String md5, String size){
        return md5 + "_" + size;
    }

    /**
     * split file name by '.'
     * @param name like aa.txt
     * @return res[0]: name, res[1]: suffixes
     */
    public static String[] splitFileName(String name) {
        int idx = name.lastIndexOf('.');
        String [] res = new String[2];
        res[0] = name.substring(0, idx);
        res[1] = name.substring(idx + 1);
        return res;
    }

    public static boolean validEmailAddress(String email) {
        return email.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
    }

    public static void main(String[] args) throws ClassNotFoundException, JsonProcessingException {
        System.out.println(validFileName("/"));
        System.out.println(validFileName("12erqw"));
        System.out.println(validFileName("_sd&"));
        System.out.println(validDirName("sdf/ll"));
        System.out.println(validFileName("^&!d?sdf"));
        System.out.println(validEmailAddress("123123@dsfa"));
        System.out.println(validEmailAddress("123123@dsfa.com"));
        System.out.println(validEmailAddress("123123@dsfa.com.cn"));
    }
}
