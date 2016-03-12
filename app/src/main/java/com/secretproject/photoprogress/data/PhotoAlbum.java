package com.secretproject.photoprogress.data;


/**
 * Created by milan.curcic on 9.3.2016..
 */
public class PhotoAlbum {

    private int id;
    private String name;
    private String description;
    private long notificationTime;
    private NotificationInterval notificationInterval;

    // TODO: 9.3.2016. Add photo collection field

    public static final String PHOTO_ALBUM = "PhotoAlbum";
    public static final String ID = "Id";
    public static final String NAME = "Name";
    public static final String DESCRIPTION = "Description";
    public static final String NOTIFICATION_TIME = "NotificationTime";
    public static final String NOTIFICATION_INTERVAL = "NotificationInterval";

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(long notificationTime) {
        this.notificationTime = notificationTime;
    }

    public NotificationInterval getNotificationInterval() {
        return notificationInterval;
    }

    public void setNotificationInterval(NotificationInterval notificationInterval) {
        this.notificationInterval = notificationInterval;
    }
}
