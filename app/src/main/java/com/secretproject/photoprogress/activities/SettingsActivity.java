package com.secretproject.photoprogress.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.data.PhotoAlbum;
import com.secretproject.photoprogress.helpers.NotificationHelper;
import com.secretproject.photoprogress.helpers.PhotoAlbumHelper;
import com.secretproject.photoprogress.helpers.XmlHelper;

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
        onEditNotificationButtonListener();

        tvNotification = (TextView) findViewById(R.id.tvNotification);

//        PhotoAlbum album = new PhotoAlbum();
//        ArrayList<PhotoAlbum> settings = new ArrayList<PhotoAlbum>();
//
//        album.setId(1);
//        album.setName("aaa");
//        album.setDescription("aaa");
//        album.setNotificationTime(100);
//
//        if (album.getName() == ""){
//
//        }
//
//        settings.add(album);
//        try {
//            XmlHelper.saveToXmlFile(settings);
//        }
//        catch (Exception e){
//            System.out.println(e.toString());
//        }

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
                    String[] hoursAndMinutes = notificationTime.getText().toString().split(":");
                    int hours = Integer.parseInt(hoursAndMinutes[0].trim());
                    int minutes = Integer.parseInt(hoursAndMinutes[1].trim());

                    album.setNotificationTime(NotificationHelper.getTimeInMilliseconds(hours, minutes));
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

    public void onEditNotificationButtonListener(){
        Button editNotificationBtn=(Button)findViewById(R.id.btnEditNotification);
        editNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, SetNotificationActivity.class));

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

    private void closeActivity(){
        this.finish();
    }

}
