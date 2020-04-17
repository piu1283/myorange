package com.ood.myorange.util;

import com.ood.myorange.constant.enumeration.FileType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Chen on 4/16/20.
 */
public class FileTypeUtil {
    private static final Set<String> DOCUMENT_TYPE_SET = new HashSet<>(Arrays.asList("html","rtf","xml","zip","rar","eml","dbx","pst","xls","doc","mdb","wpd","eps","ps","pdf","qdf","pwl"));
    private static final Set<String> VIDEO_TYPE_SET = new HashSet<>(Arrays.asList("avi", "mp3", "mpg", "mpeg", "rm", "rmvb", "mov", "wmv", "asf", "dat"));
    private static final Set<String> AUDIO_TYPE_SET = new HashSet<>(Arrays.asList("wma", "rm", "wav", "mid", "ram", "asf", "mid", "ape", "flac"));
    private static final Set<String> IMG_TYPE_SET = new HashSet<>(Arrays.asList("jpg","jpge","png","gif","tif","bmp","dwg","psd","svg",""));


    public static FileType getFileTypeBySuffixes(String suffixes){
        if (IMG_TYPE_SET.contains(suffixes)) {
            return FileType.IMG;
        }
        if (AUDIO_TYPE_SET.contains(suffixes)) {
            return FileType.AUDIO;
        }
        if (VIDEO_TYPE_SET.contains(suffixes)) {
            return FileType.VIDEO;
        }
        return FileType.DOCUMENT;
    }
}
