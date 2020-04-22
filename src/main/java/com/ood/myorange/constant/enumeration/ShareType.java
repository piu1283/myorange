package com.ood.myorange.constant.enumeration;

import com.ood.myorange.exception.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Linkun on 4/13/20.
 */
@Slf4j
public enum ShareType {
    NONEPWD,PWD;

    public static ShareType getShareType(String str) {
        ShareType shareType;
        try {
            shareType = ShareType.valueOf(str.toUpperCase());
            return shareType;
        } catch (IllegalArgumentException e) {
            log.warn("Invalid ShareType, {}", str);
            throw new InvalidRequestException("Type can only be one of these: [NONEPWD, PWD]");
        }
    }

}