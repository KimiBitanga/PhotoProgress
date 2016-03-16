package com.secretproject.photoprogress.data;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;


public class PhotoAlbum implements Serializable {

    private int id;
    private String name;
    private String description;
    private long notificationTime;
    private NotificationInterval notificationInterval;
    private boolean isMaskOn;
    private int maskOpacityLevel;
    private transient Collection<Bitmap> albumPhotos;

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

    public void setIsMaskOn(boolean isMaskOn){
        this.isMaskOn = isMaskOn;
    }

    public boolean getIsMaskOn (){
        return isMaskOn;
    }

    public void setMaskOpacityLevel(int maskOpacityLevel){
        this.maskOpacityLevel=maskOpacityLevel;
    }

    public int getMaskOpacityLevel(){
        return maskOpacityLevel;
    }

    public Bitmap getLastPhoto(){

        Bitmap lastPhoto = null;
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "PhotoProgress" + File.separator );
        File files[] = directory.listFiles();
        File lastPhotoFile=null;
        for (File f : files) {
            if(f.getName().startsWith(Integer.toString(getId()) + "_")){
                if(lastPhotoFile==null) {
                    lastPhotoFile = f;
                }
                else{
                    if(new Date(f.lastModified()).after(new Date(lastPhotoFile.lastModified()))){
                        lastPhotoFile = f;
                    }
                }
            }
        }
        if(lastPhotoFile!=null) {
            lastPhoto = BitmapFactory.decodeFile(lastPhotoFile.getAbsolutePath());
        }

        return lastPhoto;
    }

    public Collection<Bitmap> getAlbumPhotos(){
        if (albumPhotos==null){
            albumPhotos = null;//TODO: GetLastPhoto!!!
        }
        return albumPhotos;
    }



//    public static final Creator<PhotoAlbum> CREATOR = new Creator<PhotoAlbum>(){
//        @Override
//        public PhotoAlbum createFromParcel(Parcel source) {
//            return new PhotoAlbum(source);
//        }
//
//        @Override
//        public PhotoAlbum[] newArray(int size) {
//            return new PhotoAlbum[size];
//        }
//    };

//    public PhotoAlbum(){}
//
//    private PhotoAlbum(Parcel parcel){
//        id=parcel.readInt();
//        name=parcel.readString();
//        description = parcel.readString();
//        notificationTime = parcel.readLong();
//        notificationInterval = NotificationInterval.fromInt(parcel.readInt());
//        isMaskOn = parcel.readByte() == 1;
//        maskOpacityLevel = parcel.readInt();
//    }
//
//    @Override
//    public void writeToParcel(Parcel destination, int flags) {
//        destination.writeInt(id);
//        destination.writeString(name);
//        destination.writeString(description);
//        destination.writeLong(notificationTime);
//        destination.writeInt(notificationInterval.getValue());
//        destination.writeByte((byte)(isMaskOn ? 1:0));
//        destination.writeInt(maskOpacityLevel);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
}
