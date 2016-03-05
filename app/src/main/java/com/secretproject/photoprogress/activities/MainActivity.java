package com.secretproject.photoprogress.activities;

import java.util.Calendar;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.notifications.NotificationReceiver;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button setNotificationButton = (Button) findViewById(R.id.buttonSetNotification);
        setNotificationButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, SetNotificationActivity.class));
                    }
                }
        );
    }

//    @Override
//    protected void onNewIntent( Intent intent ) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//    }

}
