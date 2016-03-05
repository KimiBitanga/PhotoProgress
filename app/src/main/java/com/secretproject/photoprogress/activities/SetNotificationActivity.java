package com.secretproject.photoprogress.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TimePicker;

import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.notifications.NotificationReceiver;

import java.util.Calendar;

public class SetNotificationActivity extends AppCompatActivity {

    public AlarmManager alarmManager;
    Intent alarmIntent;
    PendingIntent pendingIntent;
    int mNotificationCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
    public void triggerAlarm(View v){
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmIntent = new Intent(SetNotificationActivity.this, NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(SetNotificationActivity.this, 0, alarmIntent, 0);

        //TimePicker is not in use. Tomorrow..
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.clearFocus();

        Calendar alarmStartTime = Calendar.getInstance();
        alarmStartTime.add(Calendar.MINUTE, 1);

        //Repeating alarm should be added
//        alarmManager.setRepeating(AlarmManager.RTC, alarmStartTime.getTimeInMillis(), getInterval(), pendingIntent);
        alarmManager.set(AlarmManager.RTC, alarmStartTime.getTimeInMillis(), pendingIntent);
    }
    //Not in use
    private int getInterval(){
        int seconds = 60;
        int milliseconds = 1000;
        int repeatMS = seconds * 2 * milliseconds;
        return repeatMS;
    }
}
