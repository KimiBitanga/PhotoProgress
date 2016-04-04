package com.secretproject.photoprogress.helpers;

import java.util.Calendar;

/**
 * Created by milan.curcic on 14.3.2016..
 */
public class NotificationHelper {

    public static long getTimeInMilliseconds(int hours, int minutes){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        long totalMilliseconds = cal.getTimeInMillis();

        if (totalMilliseconds < Calendar.getInstance().getTimeInMillis()) {
            totalMilliseconds += 86400000;
        }

        return totalMilliseconds;
    }

    public static String getTimeFromMilliseconds(long milliseconds){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);

        int hours = cal.getTime().getHours();
        int minutes = cal.getTime().getMinutes();

        return ((hours < 10) ? "0" + hours : hours) + ":" + ((minutes < 10) ? "0" + minutes : minutes);
    }

    public static int getHoursFromMilliseconds(long milliseconds){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);

        return cal.getTime().getHours();
    }

    public static int getMinutesFromMilliseconds(long milliseconds){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);

        return cal.getTime().getMinutes();
    }
}
