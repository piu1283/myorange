package com.ood.myorange.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Chen on 4/18/20.
 */
public class PasswordUtil {
    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    public static String encodePassword(String pass) {
        return bCryptPasswordEncoder.encode(pass);
    }
}
