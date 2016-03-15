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
}
