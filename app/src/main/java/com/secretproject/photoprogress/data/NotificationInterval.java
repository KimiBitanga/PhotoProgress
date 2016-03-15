package com.secretproject.photoprogress.data;

/**
 * Created by milan.curcic on 12.3.2016..
 */
public enum NotificationInterval {
    HOURLY(0),
    EVERY_OTHER_HOUR(1),
    DAILY(2),
    EVERY_OTHER_DAY(3),
    WEEKLY (4),
    EVERY_OTHER_WEEK (5),
    EVERY_FOUR_WEEKS (6);

    private final int value;
    private NotificationInterval(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static NotificationInterval fromInt(int i) {
        for (NotificationInterval b : NotificationInterval .values()) {
            if (b.getValue() == i) { return b; }
        }
        return null;
    }
}
