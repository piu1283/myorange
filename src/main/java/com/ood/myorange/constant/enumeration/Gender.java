package com.ood.myorange.constant.enumeration;

import com.ood.myorange.exception.InvalidRequestException;
import com.sun.tools.javah.Gen;

/**
 * Created by Chen on 2/25/20.
 */
public enum Gender {
    M("Male"),F("Female");

    private String name;

    Gender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
