package com.secretproject.photoprogress.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.activities.TakePhotoActivity;
import com.secretproject.photoprogress.data.PhotoAlbum;
import com.secretproject.photoprogress.helpers.PhotoAlbumHelper;

import org.w3c.dom.Text;

public class PhotoAlbumOverviewActivity extends AppCompatActivity {

    private PhotoAlbum photoAlbum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if(photoAlbum!=null) {
//            Toast.makeText(this, photoAlbum.getName(), Toast.LENGTH_SHORT).show();
//        }

        onShowVideoPreviewButtonListener();
        onMakeImagePostersButtonListener();
        onMakeNewPhotoButtonListener();
        onEditAlbumButtonListener();
        onDeleteAlbumButtonListener();

        photoAlbum = PhotoAlbumHelper.CurrentPhotoAlbum;
        if(photoAlbum == null){
            //Every activity should have id of photoAlbum in case that android delete static variable to get instance from the file.
        }
        setAlbumDataToLayout();
    }

    private void setAlbumDataToLayout() {
        if(photoAlbum!=null) {
            TextView albumNameTV = (TextView) findViewById(R.id.albumNameTextView);
            albumNameTV.setText(photoAlbum.getName());

            TextView albumDescriptionTV = (TextView) findViewById(R.id.albumDescriptionTextView);
            albumDescriptionTV.setText(photoAlbum.getDescription());
        }
    }

    public void onShowVideoPreviewButtonListener (){
        Button showVideoPreviewBtn=(Button)findViewById(R.id.showVideoPreviewBtn);
        showVideoPreviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoAlbumHelper.CurrentPhotoAlbum = photoAlbum;
                startActivity(new Intent(PhotoAlbumOverviewActivity.this, AlbumVideoActivity.class));
            }
        });
    }

    public void onMakeImagePostersButtonListener (){
        Button makeImagePostersBtn=(Button)findViewById(R.id.makeImagePostersBtn);
        makeImagePostersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoAlbumHelper.CurrentPhotoAlbum = photoAlbum;

                startActivity(new Intent(PhotoAlbumOverviewActivity.this, PosterActivity.class));
            }
        });
    }

    public void onMakeNewPhotoButtonListener(){

        Button makenewPhotoBtn=(Button)findViewById(R.id.makeNewPhotoBtn);
        makenewPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoAlbumHelper.CurrentPhotoAlbum = photoAlbum;

                startActivity(new Intent(PhotoAlbumOverviewActivity.this, TakePhotoActivity.class));
            }
        });
    }

    public void onEditAlbumButtonListener(){

        Button editAlbumBtn=(Button)findViewById(R.id.editAlbumBtn);
        editAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoAlbumHelper.CurrentPhotoAlbum = photoAlbum;

                startActivity(new Intent(PhotoAlbumOverviewActivity.this, SettingsActivity.class));
            }
        });
    }

    public void onDeleteAlbumButtonListener(){
        Button deleteAlbumBtn=(Button)findViewById(R.id.deleteAlbumBtn);
        deleteAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //This code needs to bre extracted to helper method because popup will be used in several situations !
                AlertDialog.Builder builder = new AlertDialog.Builder(PhotoAlbumOverviewActivity.this);
                builder.setMessage("Delete of album will delete all it's photos. Are you sure you want to delete album?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                try {
                                    PhotoAlbumHelper.deletePhotoAlbum(photoAlbum);

                                    PhotoAlbumHelper.CurrentPhotoAlbum = null;
                                    startActivity(new Intent(PhotoAlbumOverviewActivity.this, MainActivity.class));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(PhotoAlbumOverviewActivity.this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }
}
