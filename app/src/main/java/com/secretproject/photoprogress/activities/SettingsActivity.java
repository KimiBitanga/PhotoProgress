package com.secretproject.photoprogress.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.data.PhotoAlbum;
import com.secretproject.photoprogress.helpers.XmlHelper;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        PhotoAlbum album = new PhotoAlbum();
        ArrayList<PhotoAlbum> settings = new ArrayList<PhotoAlbum>();

        album.setId(1);
        album.setName("aaa");
        album.setDescription("aaa");
        album.setNotificationTime(100);

        if (album.getName() == ""){

        }

        settings.add(album);
        try {
            XmlHelper.saveToXmlFile(settings);
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

    }

    public void saveSettings() throws Exception {

    }
}
