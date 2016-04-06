package com.secretproject.photoprogress.helpers;

import com.secretproject.photoprogress.data.NotificationInterval;

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

    public static long getNotificationIntervalInMilliseconds(NotificationInterval interval){
        switch (interval) {
            case HOURLY: return 360000;
            case EVERY_OTHER_HOUR: return 720000;
            case DAILY: return 8640000;
            case EVERY_OTHER_DAY: return 17280000;
            case WEEKLY: return 60480000;
            case EVERY_OTHER_WEEK: return 120960000;
            case EVERY_FOUR_WEEKS: return 241920000;
            default: return 0;
        }
    }
}
