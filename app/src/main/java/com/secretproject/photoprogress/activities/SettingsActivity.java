package com.secretproject.photoprogress.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.data.NotificationInterval;
import com.secretproject.photoprogress.data.PhotoAlbum;
import com.secretproject.photoprogress.helpers.NotificationHelper;
import com.secretproject.photoprogress.helpers.PhotoAlbumHelper;
import com.secretproject.photoprogress.helpers.XmlHelper;
import com.secretproject.photoprogress.notifications.NotificationReceiver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SettingsActivity extends AppCompatActivity {

    public static TextView tvNotification;
    private int id = -1;

    public AlarmManager alarmManager;
    Intent alarmIntent;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tvNotification = (TextView) findViewById(R.id.tvNotification);

        if (PhotoAlbumHelper.CurrentPhotoAlbum != null) {
            id = PhotoAlbumHelper.CurrentPhotoAlbum.getId();

            EditText etName = (EditText) findViewById(R.id.etName);
            EditText etDescription = (EditText) findViewById(R.id.etDescription);

            etName.setText(PhotoAlbumHelper.CurrentPhotoAlbum.getName());
            etDescription.setText(PhotoAlbumHelper.CurrentPhotoAlbum.getDescription());

            if (PhotoAlbumHelper.CurrentPhotoAlbum.getNotificationTime() != 0) {
                Switch notificationSwitch = (Switch) findViewById(R.id.notificationSwitch);

                notificationSwitch.setChecked(true);
                tvNotification.setText(NotificationHelper.getTimeFromMilliseconds(PhotoAlbumHelper.CurrentPhotoAlbum.getNotificationTime()));
            }
        }
        else{
            PhotoAlbumHelper.CurrentPhotoAlbum = new PhotoAlbum();
            PhotoAlbumHelper.CurrentPhotoAlbum.setId(-1);
        }

        onSaveButtonListener();
        onCancelButtonListener();
        onNotificationSwitchListener();
    }

    public void onSaveButtonListener() {
        Button saveBtn = (Button) findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhotoAlbum album = null;
                if (id >= 0) {
                    album = PhotoAlbumHelper.CurrentPhotoAlbum;
                } else {
                    album = new PhotoAlbum();
                }

                EditText name = (EditText) findViewById(R.id.etName);
                EditText description = (EditText) findViewById(R.id.etDescription);
                TextView notificationTime = (TextView) findViewById(R.id.tvNotification);

                if (!name.getText().toString().isEmpty() && !description.getText().toString().isEmpty()) {
                    album.setName(name.getText().toString());
                    album.setDescription(description.getText().toString());
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Enter name and description";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    return;
                }


                Collection<PhotoAlbum> photoAlbums = PhotoAlbumHelper.getAllPhotoAlbums();

                if (!notificationTime.getText().toString().isEmpty()) {
                    album.setNotificationTime(PhotoAlbumHelper.CurrentPhotoAlbum.getNotificationTime());
                    album.setNotificationInterval(PhotoAlbumHelper.CurrentPhotoAlbum.getNotificationInterval());
                    album.setUseNotifications(PhotoAlbumHelper.CurrentPhotoAlbum.getUseNotifications());
                } else {
                    album.setNotificationTime(0);
                    album.setNotificationInterval(null);
                    album.setUseNotifications(false);
                }

                if (id < 0) {
                    int newId = -1;
                    if (photoAlbums != null && photoAlbums.size() > 0) {
                        for (PhotoAlbum photoAlbum : photoAlbums) {
                            if (newId < photoAlbum.getId()) {
                                newId = photoAlbum.getId();
                            }
                        }
                    } else {
                        photoAlbums = new ArrayList<PhotoAlbum>();
                    }

                    album.setId(newId + 1);

                    photoAlbums.add(album);

                    try {
                        //XmlHelper.saveToXmlFile(settings);
                        PhotoAlbumHelper.writeAllPhotoAlbums(photoAlbums);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PhotoAlbumHelper.updatePhotoAlbum(album);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                PhotoAlbumHelper.CurrentPhotoAlbum = album;

                triggerAlarm();

                startActivity(new Intent(SettingsActivity.this, PhotoAlbumOverviewActivity.class));
            }
        });

    }

    public void onCancelButtonListener() {
        Button cancelBtn = (Button) findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
    }

    public void onNotificationSwitchListener() {

        Switch notificationSwitch = (Switch) findViewById(R.id.notificationSwitch);

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startActivity(new Intent(SettingsActivity.this, SetNotificationActivity.class));

                } else {
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    Intent alarmIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);

                    alarmManager.cancel(pendingIntent);

                    if (id >= 0) {
                        PhotoAlbumHelper.CurrentPhotoAlbum.setUseNotifications(false);

                        try {
                            PhotoAlbumHelper.updatePhotoAlbum(PhotoAlbumHelper.CurrentPhotoAlbum);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    tvNotification.setText("");
                }
            }
        });
    }

    public void triggerAlarm(){
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), PhotoAlbumHelper.CurrentPhotoAlbum.getId(), alarmIntent, 0);

        long notificationTime = PhotoAlbumHelper.CurrentPhotoAlbum.getNotificationTime();
        long interval = NotificationHelper.getNotificationIntervalInMilliseconds(PhotoAlbumHelper.CurrentPhotoAlbum.getNotificationInterval());

        if (interval > 0){
            alarmManager.setRepeating(AlarmManager.RTC, notificationTime, interval, pendingIntent);
        }
        else{
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, "Error occurred while setting notification!", duration);
            toast.show();
        }
    }

    private void closeActivity() {
        this.finish();
    }

    @Override
    public void onResume(){
        super.onResume();

        if (PhotoAlbumHelper.CurrentPhotoAlbum != null) {
            Switch notificationSwitch = (Switch) findViewById(R.id.notificationSwitch);

            if (PhotoAlbumHelper.CurrentPhotoAlbum.getUseNotifications()){
                notificationSwitch.setChecked(true);
            }
            else {
                notificationSwitch.setChecked(false);
                tvNotification.setText("");
            }
        }
    }
}
