package com.ood.myorange.util;

import com.ood.myorange.constant.PermissionConstant;
import com.ood.myorange.dto.UserInfo;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Created by Chen on 4/21/20.
 */

@UtilityClass
public class AuthUtil {
    public static boolean canDownload(UserInfo userInfo) {
        if (userInfo == null) {
            return false;
        }
        for (SimpleGrantedAuthority authority : userInfo.getAuthorities()) {
            if (authority.getAuthority().equals(PermissionConstant.DOWNLOAD.toString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean canUpload(UserInfo userInfo) {
        if (userInfo == null) {
            return false;
        }
        for (SimpleGrantedAuthority authority : userInfo.getAuthorities()) {
            if (authority.getAuthority().equals(PermissionConstant.UPLOAD.toString())) {
                return true;
            }
        }
        return false;
    }
}