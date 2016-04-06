package com.secretproject.photoprogress.activities;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.ViewModels.CameraPreview;
import com.secretproject.photoprogress.data.PhotoAlbum;
import com.secretproject.photoprogress.helpers.PhotoAlbumHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhotoActivity extends AppCompatActivity {

    private PhotoAlbum photoAlbum;
    private TakePhotoActivity thisActivity;

    private static final String TAG="TakePhotoActivity";
    private static final String STATE_CAMERA_INDEX="STATE_CAMERA_INDEX";

    public static final String EXTRA_CONTACT="EXTRA_CONTACT";

    private Camera camera;
    private Camera.CameraInfo cameraInfo;
    private int currentCameraIndex;
    private CameraPreview cameraPreview;

    private Bitmap takenPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            int id = extras.getInt("id");

            PhotoAlbumHelper.CurrentPhotoAlbum = PhotoAlbumHelper.getPhotoAlbum(id);
        }

        photoAlbum = PhotoAlbumHelper.CurrentPhotoAlbum;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if(savedInstanceState !=null){
            currentCameraIndex = savedInstanceState.getInt(STATE_CAMERA_INDEX);
        }

        cameraPreview = new CameraPreview(this);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.activity_take_photo_frame);
        frameLayout.addView(cameraPreview,0);

        thisActivity = this;

        onTakePhotoButtonListener();
        onChangeCameraButtonListener();
        onDiscardPhotoButtonListener();
        onSaveImageButtonListener();
        onSeekBarListener();
        onMaskSwitchListener();

        ImageView mskImageView = (ImageView) findViewById(R.id.maskImgView);
        Bitmap lastAlbumPhoto= photoAlbum.getLastPhoto();
        if(lastAlbumPhoto!=null){
            mskImageView.setImageBitmap(lastAlbumPhoto);
        }
    }

    @Override
    public void onResume (){
        super.onResume();
        establishCamera();
    }

    @Override
    public void onPause(){
        super.onPause();

        if(camera != null){
            cameraPreview.setCamera(null,null);
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CAMERA_INDEX, currentCameraIndex);
    }

    private void establishCamera(){

        if(camera!=null){
            cameraPreview.setCamera(null,null);
            camera.release();
            camera=null;
        }

        try{
            camera=Camera.open(currentCameraIndex);
        }catch(Exception e){
            Log.e(TAG, "Cannot open camera" + currentCameraIndex, e);
            Toast.makeText(this, "Error in process of using camera", Toast.LENGTH_SHORT).show();
            return;
        }

        cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(currentCameraIndex, cameraInfo);
        cameraPreview.setCamera(camera, cameraInfo);


    }

    public void onTakePhotoButtonListener(){
        ImageButton takePhotoBtn = (ImageButton) findViewById(R.id.takePhotoBtn);
        takePhotoBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Take Photo implementation
                        camera.takePicture(new Camera.ShutterCallback() {
                            @Override
                            public void onShutter() {

                            }
                        }, new Camera.PictureCallback() {
                            @Override
                            public void onPictureTaken(byte[] data, Camera cmr) {
                            }
                        }, new Camera.PictureCallback() {
                            @Override
                            public void onPictureTaken(byte[] data, Camera cmr) {

                                if (camera != null) {
                                    cameraPreview.setCamera(null, null);
                                    camera.release();
                                    camera = null;
                                }

                                takenPhoto = BitmapFactory.decodeByteArray(data, 0, data.length);
                                Matrix matrix = new Matrix();
                                matrix.postRotate(90);
                                Bitmap rotetedBitMap = Bitmap.createBitmap(takenPhoto, 0, 0, takenPhoto.getWidth(), takenPhoto.getHeight(), matrix, true);

                                ImageView takenPhotoImage = (ImageView) findViewById(R.id.takenPhotoImgView);

                                takenPhotoImage.setImageBitmap(rotetedBitMap);

                                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.activity_take_photo_frame);
                                frameLayout.setVisibility(View.GONE);

                                GridLayout takenPhotoGrid = (GridLayout) findViewById(R.id.takenPhotoGridLayout);
                                takenPhotoGrid.setVisibility(View.VISIBLE);
                            }
                        });
                        Toast.makeText(thisActivity, "Take Photo clicked", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void onChangeCameraButtonListener(){
        ImageButton changeCameraBtn = (ImageButton) findViewById(R.id.changeCameraBtn);

        int camNo = Camera.getNumberOfCameras();
        if(camNo < 2)
            changeCameraBtn.setVisibility(View.GONE);

        changeCameraBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int numberOfCameras = Camera.getNumberOfCameras();
                        if (numberOfCameras > 1) {
                            currentCameraIndex = currentCameraIndex == 0 ? 1 : 0;
                            establishCamera();
                        }
                    }
                }
        );
    }

    public void onDiscardPhotoButtonListener(){
        ImageButton discardPhotoBtn =(ImageButton) findViewById(R.id.discardImageBtn);
        discardPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takenPhoto = null;
                establishCamera();
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.activity_take_photo_frame);
                frameLayout.setVisibility(View.VISIBLE);

                GridLayout takenPhotoGrid= (GridLayout) findViewById(R.id.takenPhotoGridLayout);
                takenPhotoGrid.setVisibility(View.GONE);
            }
        });
    }

    public void onSaveImageButtonListener(){
        ImageView saveImageBtn=(ImageView)findViewById(R.id.saveImageBtn);
        saveImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(takenPhoto!=null){
                    saveImageToDisk();
                    PhotoAlbumHelper.CurrentPhotoAlbum = photoAlbum;
                    startActivity(new Intent(TakePhotoActivity.this, PhotoAlbumOverviewActivity.class));
                }
            }
        });
    }

    private void saveImageToDisk() {

        try {
            File file = new File(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "PhotoProgress" + File.separator ),getImageName());
            if (file.exists()) {
                file.delete();
            }

            FileOutputStream out = new FileOutputStream(file);
            takenPhoto.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getImageName() {

        return Integer.toString(photoAlbum.getId())+"_"+(new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()))+".jpg";
    }

    public void onSeekBarListener(){
        SeekBar imageViewOpacitySkBr =(SeekBar) findViewById(R.id.maskOpacitySkBr);
        imageViewOpacitySkBr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            final ImageView imageView = (ImageView) findViewById(R.id.maskImgView);

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setImageOpacity(seekBar.getProgress(), imageView);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setImageOpacity(int progress, ImageView imageView) {
        double alpha = progress * 2.4;
        imageView.setAlpha((int) alpha);
        imageView.setImageAlpha((int) alpha);
    }

    public void onMaskSwitchListener(){

        Switch maskSwitch=(Switch)findViewById(R.id.maskSwitch);

        final ImageView maskImageView = (ImageView) findViewById(R.id.maskImgView);
        final SeekBar seekBar = (SeekBar) findViewById(R.id.maskOpacitySkBr);

        maskSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    maskImageView.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                } else {
                    maskImageView.setVisibility(View.GONE);
                    seekBar.setVisibility(View.GONE);
                }
            }
        });

        setImageOpacity(photoAlbum.getMaskOpacityLevel(), maskImageView);

        if(!photoAlbum.getIsMaskOn()){
            maskSwitch.setChecked(false);
        }
    }
}
