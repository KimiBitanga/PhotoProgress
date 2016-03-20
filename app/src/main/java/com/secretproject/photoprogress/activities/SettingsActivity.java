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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        onSaveButtonListener();
        onCancelButtonListener();
        onNotificationSwitchListener();

        tvNotification = (TextView) findViewById(R.id.tvNotification);

    }

    public void onSaveButtonListener(){
        Button saveBtn=(Button)findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhotoAlbum album = new PhotoAlbum();

                EditText name = (EditText) findViewById(R.id.etName);
                EditText description = (EditText) findViewById(R.id.etDescription);
                TextView notificationTime = (TextView) findViewById(R.id.tvNotification);

                if (!name.getText().toString().isEmpty() && !description.getText().toString().isEmpty()){
                    album.setName(name.getText().toString());
                    album.setDescription(description.getText().toString());
                }
                else{
                    Context context = getApplicationContext();
                    CharSequence text = "Enter name and description";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    return;
                }


                Collection<PhotoAlbum> photoAlbums = PhotoAlbumHelper.getAllPhotoAlbums();
                int id = -1;

                if (photoAlbums != null && photoAlbums.size() > 0) {
                    for (PhotoAlbum photoAlbum : photoAlbums) {
                        if (id < photoAlbum.getId()) {
                            id = photoAlbum.getId();
                        }
                    }
                }
                else {
                    photoAlbums = new ArrayList<PhotoAlbum>();
                }

                album.setId(id + 1);

                if (!notificationTime.getText().toString().isEmpty()) {
                    album.setNotificationTime(PhotoAlbumHelper.CurrentPhotoAlbum.getNotificationTime());
                    album.setNotificationInterval(PhotoAlbumHelper.CurrentPhotoAlbum.getNotificationInterval());
                }

                photoAlbums.add(album);

                PhotoAlbumHelper.CurrentPhotoAlbum = album;

                try {
                    //XmlHelper.saveToXmlFile(settings);
                    PhotoAlbumHelper.writeAllPhotoAlbums(photoAlbums);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                startActivity(new Intent(SettingsActivity.this, PhotoAlbumOverviewActivity.class));
            }
        });

    }

    public void onCancelButtonListener(){
        Button cancelBtn=(Button)findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
    }

    public void onNotificationSwitchListener(){

        Switch notificationSwitch=(Switch)findViewById(R.id.notificationSwitch);

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

                    tvNotification.setText("");
                }
            }
        });
    }

    private void closeActivity(){
        this.finish();
    }

}
