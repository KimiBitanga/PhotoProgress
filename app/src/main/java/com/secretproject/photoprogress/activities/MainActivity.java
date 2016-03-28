package com.secretproject.photoprogress.activities;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.DialerFilter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.data.PhotoAlbum;
import com.secretproject.photoprogress.helpers.PhotoAlbumHelper;
import com.secretproject.photoprogress.helpers.XmlHelper;

import java.io.File;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Collection;


public class MainActivity extends Activity {

    private PhotoAlbum selectedPhotoAlbum;
    private Collection<PhotoAlbum> existingPhotoAlbums;

    public static PhotoAlbumHelper photoAlbumHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TODO Questions to discuss :
        /*
        *   If user is using one camera first time, should we allow him to switch camera for next photos ?
        *   We have to save CameraIndex and OpacityLevel if user set it.
        *   Sending PhotoAlbum variable from activity to activity
        *   Creating folder and saving PhotoAlbum
        *   Saving Images
        *   Creating Video
        *   Creating posters activity
        *   Fixing TakePhoto mask and other things
        * */

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        onCreateNewAlbumButtonListener();
        onGoToAlbumOverviewButtonListener();

        fillExistingAlbums();

    }

    public void onCreateNewAlbumButtonListener(){
        Button createNewAlbumButton = (Button) findViewById(R.id.createNewAlbumBtn);
        createNewAlbumButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhotoAlbumHelper.CurrentPhotoAlbum = null;

                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    }
                }
        );
    }

    public void onGoToAlbumOverviewButtonListener(){
        Button goToAlbumOverviewBtn = (Button) findViewById(R.id.goToAlbumOverviewBtn);
        final  Activity _currentActivity=this;
        goToAlbumOverviewBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedPhotoAlbum==null) {
                            Toast.makeText(_currentActivity, R.string.select_album_information_message, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            PhotoAlbumHelper.CurrentPhotoAlbum = selectedPhotoAlbum;
                            startActivity(new Intent(MainActivity.this, PhotoAlbumOverviewActivity.class));
                        }
                    }
                }
        );
    }

    private View currentlySelectedView = null;

    public void fillExistingAlbums(){

        try {
            existingPhotoAlbums = XmlHelper.loadSettingsFromXml();
        }
        catch(Exception e){
            Log.e("BMTesting", "loadSettingsFromXml", e);
        }

        existingPhotoAlbums = PhotoAlbumHelper.getAllPhotoAlbums();

        if ( existingPhotoAlbums == null || existingPhotoAlbums.size() == 0){
            RelativeLayout existingAlbumsContainer = (RelativeLayout)findViewById(R.id.existingAlbumsContainerRelativeLayout);
            existingAlbumsContainer.setVisibility(View.GONE);
        }

        Bitmap image=BitmapFactory.decodeResource(getResources(), R.drawable.new_x48px);

        LinearLayout existingAlbumsLL = (LinearLayout) findViewById(R.id.existingAlbumsLinearLayout);

        for (int i=0; i< existingPhotoAlbums.size(); i++){

            RelativeLayout rl = new RelativeLayout(this);
            rl.setId(i);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(3, 3, 3, 3);
            rl.setLayoutParams(lp);

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentlySelectedView != null){
                        currentlySelectedView.setBackground(getResources().getDrawable(R.drawable.border));
                    }

                    currentlySelectedView = v;
                    selectedPhotoAlbum = ((ArrayList<PhotoAlbum>)existingPhotoAlbums).get(v.getId());


                    GradientDrawable gd = (GradientDrawable) v.getBackground().mutate();
                    int width_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
                    gd.setStroke(width_px, 0xff00ff00);
                    v.setBackground(gd);

                    if(selectedPhotoAlbum != null){
                        Button goToAlbumOverviewBtn = (Button) findViewById(R.id.goToAlbumOverviewBtn);
                        goToAlbumOverviewBtn.setEnabled(true);
                    }
                }
            });

            Drawable backgroundBorder = getResources().getDrawable(R.drawable.border);
            rl.setBackground(backgroundBorder);

            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(2, 2, 2, 2);
            imageView.setMaxHeight(80);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //TODO GetLastImageForEveryAlbum
            imageView.setImageBitmap(image);
            rl.addView(imageView);

            TextView tv = new TextView(this);
            tv.setText(((ArrayList<PhotoAlbum>) existingPhotoAlbums).get(i).getName());
            rl.addView(tv);

            existingAlbumsLL.addView(rl);
        }

    }


    public Collection<PhotoAlbum> getMockPhotoAlbums(){
        Collection<PhotoAlbum> photoAlbums = new ArrayList<PhotoAlbum>();

        for (int i = 0; i < 8; i++){
            PhotoAlbum pa = new PhotoAlbum();
            pa.setName("albumName_"+Integer.toString(i));
            pa.setDescription("albumDescription_" + Integer.toString(i));
            photoAlbums.add(pa);
        }

        return photoAlbums;
    }

}
