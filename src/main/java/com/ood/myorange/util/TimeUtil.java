package com.ood.myorange.util;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Chen on 4/22/20.
 */
@UtilityClass
public class TimeUtil {
    public static Timestamp getCurrentTimeStamp(){
        TimeZone zone = TimeZone.getTimeZone(ZoneId.of("GMT-4"));
        Calendar cal = Calendar.getInstance(zone);
        return new Timestamp(cal.getTime().getTime());

    }

    public static void main(String[] args) {
        System.out.println(getCurrentTimeStamp());
    }
}
