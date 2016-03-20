package com.secretproject.photoprogress.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import java.util.Calendar;

import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.data.NotificationInterval;
import com.secretproject.photoprogress.helpers.NotificationHelper;
import com.secretproject.photoprogress.helpers.PhotoAlbumHelper;
import com.secretproject.photoprogress.notifications.NotificationReceiver;


public class SetNotificationActivity extends AppCompatActivity {

    public AlarmManager alarmManager;
    Intent alarmIntent;
    PendingIntent pendingIntent;
    int mNotificationCount;
    long notificationTime;
    Spinner intervalSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        getWindow().setLayout((int)(width *.8), (int)(height * .7));

        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

        intervalSpinner = (Spinner) findViewById(R.id.intervalSpinner);
        intervalSpinner.setAdapter(new ArrayAdapter<NotificationInterval>(this, android.R.layout.simple_spinner_item, NotificationInterval.values()));
        intervalSpinner.setSelection(2);

        if (PhotoAlbumHelper.CurrentPhotoAlbum.getNotificationTime() > 0){
            //TODO: set time and interval
        }
    }

    public void setNotification(View v){
        triggerAlarm();

        PhotoAlbumHelper.CurrentPhotoAlbum.setNotificationTime(notificationTime);
        PhotoAlbumHelper.CurrentPhotoAlbum.setNotificationInterval((NotificationInterval)intervalSpinner.getSelectedItem());

        //Close activity
        this.finish();
    }

    public void triggerAlarm(){
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);

        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.clearFocus();

        int hours = timePicker.getCurrentHour();
        int minutes  = timePicker.getCurrentMinute();

        SettingsActivity.tvNotification.setText(hours + ":" + ((minutes > 9) ? minutes : "0" + minutes));

        notificationTime = NotificationHelper.getTimeInMilliseconds(hours, minutes);

//        Calendar alarmStartTime = Calendar.getInstance();
//        alarmStartTime.add(Calendar.MINUTE, 1);

        //Repeating alarm should be added
//        alarmManager.setRepeating(AlarmManager.RTC, this.getTimeInMilliseconds(hours, minutes), 86400000, pendingIntent);
        alarmManager.set(AlarmManager.RTC, notificationTime, pendingIntent);

    }
}
