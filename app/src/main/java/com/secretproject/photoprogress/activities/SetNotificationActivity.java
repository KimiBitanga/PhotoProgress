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
import java.util.Calendar;

import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.notifications.NotificationReceiver;


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

        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void setNotification(View v){
        triggerAlarm();

        //Close activity
        this.finish();
    }

    public void triggerAlarm(){
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmIntent = new Intent(SetNotificationActivity.this, NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(SetNotificationActivity.this, 0, alarmIntent, 0);

        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.clearFocus();

        int hours = timePicker.getCurrentHour();
        int minutes  = timePicker.getCurrentMinute();

//        Calendar alarmStartTime = Calendar.getInstance();
//        alarmStartTime.add(Calendar.MINUTE, 1);

        //Repeating alarm should be added
//        alarmManager.setRepeating(AlarmManager.RTC, this.getTimeInMilliseconds(hours, minutes), 86400000, pendingIntent);
        alarmManager.set(AlarmManager.RTC, this.getTimeInMilliseconds(hours, minutes), pendingIntent);

    }

    private long getTimeInMilliseconds(int hours, int minutes){
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
