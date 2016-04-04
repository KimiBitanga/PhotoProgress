package com.secretproject.photoprogress.data;

/**
 * Created by milan.curcic on 12.3.2016..
 */
public enum NotificationInterval {
    HOURLY("Hourly"),
    EVERY_OTHER_HOUR("Every two hours"),
    DAILY("Daily"),
    EVERY_OTHER_DAY("Every other day"),
    WEEKLY ("Weekly"),
    EVERY_OTHER_WEEK ("Every other week"),
    EVERY_FOUR_WEEKS ("Every four weeks");

    private String friendlyName;

    private NotificationInterval(String friendlyName){
        this.friendlyName = friendlyName;
    }

    @Override public String toString(){
        return friendlyName;
    }

//    private final int value;
//    private NotificationInterval(int value) {
//        this.value = value;
//    }
//
//    public int getValue() {
//        return value;
//    }
//
//    public static NotificationInterval fromInt(int i) {
//        for (NotificationInterval b : NotificationInterval.values()) {
//            if (b.getValue() == i) { return b; }
//        }
//        return null;
//    }
}
