package com.secretproject.photoprogress.TakePhotoActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
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

public class TakePhotoActivity extends AppCompatActivity {

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
                    Toast.makeText(thisActivity, "Save Image", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void onSeekBarListener(){
        SeekBar imageViewOpacitySkBr =(SeekBar) findViewById(R.id.maskOpacitySkBr);
        imageViewOpacitySkBr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            final ImageView imageView = (ImageView) findViewById(R.id.maskImgView);

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double alpha = seekBar.getProgress() * 2.4;
                imageView.setAlpha((int) alpha);
                imageView.setImageAlpha((int) alpha);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void onMaskSwitchListener(){

        Switch maskSwitch=(Switch)findViewById(R.id.maskSwitch);
        maskSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ImageView maskImageView = (ImageView) findViewById(R.id.maskImgView);
                SeekBar seekBar =(SeekBar)findViewById(R.id.maskOpacitySkBr);
                if(isChecked){
                    maskImageView.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                }
                else {
                    maskImageView.setVisibility(View.GONE);
                    seekBar.setVisibility(View.GONE);
                }
            }
        });

        maskSwitch.setChecked(false);
    }
}
