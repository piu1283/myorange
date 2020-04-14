package com.ood.myorange.constant.enumeration;

import com.ood.myorange.exception.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Chen on 3/29/20.
 */
@Slf4j
public enum  FileType {
    DOCUMENT,AUDIO,VIDEO,IMG;

    public static FileType getFileType(String str) {
        FileType fileType;
        try {
            fileType = FileType.valueOf(str.toUpperCase());
            return fileType;
        } catch (IllegalArgumentException e) {
            log.warn("Invalid FileType, {}", str);
            throw new InvalidRequestException("Type can only be one of these: [DOCUMENT, AUDIO, VIDEO, IMG]");
        }
    }

}
