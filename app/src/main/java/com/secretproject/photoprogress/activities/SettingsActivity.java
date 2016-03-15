package com.secretproject.photoprogress.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.data.PhotoAlbum;
import com.secretproject.photoprogress.helpers.XmlHelper;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        onSaveButtonListener();
        onCancelButtonListener();

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

    public void onSaveButtonListener(){
        Button saveBtn=(Button)findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    public void onCancelButtonListener(){
        Button cancelBtn=(Button)findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Should we return to the previous activity or simply to clean filds ?
            }
        });
    }

}
