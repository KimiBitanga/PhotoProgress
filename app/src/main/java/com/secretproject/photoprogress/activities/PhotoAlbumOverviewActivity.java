package com.secretproject.photoprogress.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.adapters.PhotosHorizontalListAdapter;
import com.secretproject.photoprogress.data.PhotoAlbum;
import com.secretproject.photoprogress.helpers.PhotoAlbumHelper;
import com.secretproject.photoprogress.helpers.TouchImageView;

import org.lucasr.twowayview.TwoWayView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class PhotoAlbumOverviewActivity extends AppCompatActivity {

    private PhotoAlbum photoAlbum;
    private PhotosHorizontalListAdapter photosHorizontalListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        onShowVideoPreviewButtonListener();
        onMakeImagePostersButtonListener();
        onMakeNewPhotoButtonListener();

        photoAlbum = PhotoAlbumHelper.CurrentPhotoAlbum;
        if(photoAlbum == null){
            //Every activity should have id of photoAlbum in case that android delete static variable to get instance from the file.
        }
        setAlbumDataToLayout();

        initializePhotosListView();
    }



    private void initializePhotosListView() {

        ArrayList<File> albums=new ArrayList<File>();
        photosHorizontalListAdapter = new PhotosHorizontalListAdapter(this, albums);
        TwoWayView photosmTwoWayView = (TwoWayView)findViewById(R.id.photosTwoWayView);
        photosmTwoWayView.setAdapter(photosHorizontalListAdapter);
        photosmTwoWayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RelativeLayout albumOverviewRl = (RelativeLayout) findViewById(R.id.albumOverviewRL);
                RelativeLayout photoOverviewRl = (RelativeLayout) findViewById(R.id.photoOverviewRL);
                TouchImageView photoOverviewTIV = (TouchImageView) findViewById(R.id.photoOverviewTIV);
                Bitmap selectedPhoto = BitmapFactory.decodeFile(((File) parent.getItemAtPosition(position)).getAbsolutePath());
                photoOverviewTIV.setImageBitmap(selectedPhoto);
                albumOverviewRl.setVisibility(View.GONE);
                photoOverviewRl.setVisibility(View.VISIBLE);
            }
        });

        photosmTwoWayView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                PopupMenu photoPopupMenu = new PopupMenu(PhotoAlbumOverviewActivity.this, view);
                photoPopupMenu.inflate(R.menu.photo_menu);
                photoPopupMenu.show();

                final File selectedPhoto = (File) parent.getItemAtPosition(position);

                photoPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_item_delete) {
                            selectedPhoto.delete();
                            refreshPhotosListView();
                        }
                        if (item.getItemId() == R.id.copy_photo_to_gallery) {
                            copyFileToGallery(selectedPhoto);
                        }
                        if (item.getItemId() == R.id.share_photo_item_delete) {
                            Uri phototUri = Uri.fromFile(selectedPhoto);
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.setData(phototUri);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, phototUri);
                            shareIntent.setType("image/*");

                            startActivity(Intent.createChooser(shareIntent, "Share Image"));
                        }
                        return false;
                    }
                });

                return false;
            }
        });
    }

    private void copyFileToGallery(File selectedPhoto) {

        try {
            File sdcardFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM+"/Camera").getAbsolutePath(),  selectedPhoto.getName().replace("_"," ")+".jpg");
            sdcardFile.createNewFile();
            InputStream inputStream = null;
            OutputStream outputStream = null;
            inputStream = new FileInputStream(selectedPhoto);
            outputStream = new FileOutputStream(sdcardFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            Toast.makeText(PhotoAlbumOverviewActivity.this, "Photo successfully copied to gallery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(PhotoAlbumOverviewActivity.this, "Error occurred during copy operation.", Toast.LENGTH_SHORT).show();
        }

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

    @Override
    public void onBackPressed() {

        RelativeLayout albumOverviewRl=(RelativeLayout)findViewById(R.id.albumOverviewRL);
        if(albumOverviewRl.getVisibility()==View.GONE){
            RelativeLayout photoOverviewRl = (RelativeLayout) findViewById(R.id.photoOverviewRL);
            albumOverviewRl.setVisibility(View.VISIBLE);
            photoOverviewRl.setVisibility(View.GONE);
            return;
        }

        startActivity(new Intent(PhotoAlbumOverviewActivity.this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        refreshPhotosListView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_album:
                PhotoAlbumHelper.CurrentPhotoAlbum = photoAlbum;
                startActivity(new Intent(PhotoAlbumOverviewActivity.this, SettingsActivity.class));
                return true;

            case R.id.action_delete_album:
                AlertDialog.Builder builder = new AlertDialog.Builder(PhotoAlbumOverviewActivity.this);
                builder.setMessage("This will delete all photos of the album. Are you sure you want to delete album?")
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
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_photo_album_overview, menu);
        return true;
    }

    private void refreshPhotosListView() {

        ArrayList<File> albumPhotos = PhotoAlbumHelper.getAllAlbumPhotos(PhotoAlbumHelper.CurrentPhotoAlbum.getId());

        photosHorizontalListAdapter.clear();
        photosHorizontalListAdapter.addAll(albumPhotos);
        photosHorizontalListAdapter.notifyDataSetChanged();
    }
}
