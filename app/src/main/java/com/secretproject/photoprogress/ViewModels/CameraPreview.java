package com.secretproject.photoprogress.ViewModels;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by BO on 07-Mar-16.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG ="CameraPreview";
    private final SurfaceHolder surfaceHolder ;
    private Camera camera;
    private Camera.CameraInfo cameraInfo;
    private boolean isSurfaceCreated;
    Activity _activity;

    public CameraPreview (Activity activity){
        super(activity);
        _activity=activity;
        isSurfaceCreated=false;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

    }

    public void setCamera(Camera camera, Camera.CameraInfo cameraInfo){
        if(this.camera!=null){
            try{
                this.camera.stopPreview();
            }catch(Exception e){
                Log.e(TAG, "Couldn't stop camera preview", e);
            }
        }

        this.camera=camera;
        this.cameraInfo=cameraInfo;
        if(!isSurfaceCreated){
            return;
        }

        try{
            camera.setPreviewDisplay(surfaceHolder);
            configureCamera();
            camera.startPreview();
        } catch(Exception e){
            Log.e(TAG,"Couldn't start camera preview",e);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isSurfaceCreated=true;
        if(camera!=null){
            setCamera(camera, cameraInfo);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        // stop preview before making changes
//        try {
//            camera.stopPreview();
//        } catch (Exception e) {
//            // ignore: tried to stop a non-existent preview
//        }

//        int rotation =  _activity.getWindowManager().getDefaultDisplay().getRotation();
//        int degrees = 0;
//
//        switch (rotation) {
//            case Surface.ROTATION_0: degrees = 0; break;
//            case Surface.ROTATION_90: degrees = 90; break;
//            case Surface.ROTATION_180: degrees = 180; break;
//            case Surface.ROTATION_270: degrees = 270; break;
//        }
//
//        int result;
//        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            result = (cameraInfo.orientation + degrees) % 360;
//            result = (360 - result) % 360;  // compensate the mirror
//        } else {  // back-facing
//            result = (cameraInfo.orientation - degrees + 360) % 360;
//        }
//        camera.setDisplayOrientation(result);
//        camera.startPreview();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(camera==null || surfaceHolder.getSurface()==null){
            return;
        }

        try{
            camera.stopPreview();
        }catch (Exception e){
            Log.e(TAG,"Can't stop preview",e);
        }
    }

    @Override
    protected void onMeasure(int width, int heigh) {
        width = resolveSize(getSuggestedMinimumHeight(), width);
        heigh = resolveSize(getSuggestedMinimumHeight(), heigh);

        setMeasuredDimension(width, heigh);

    }

    private int _previewWidth;
    public int getPreviewWidth(){
        return _previewWidth;
    }

    private int _previewHeight;
    public int getPreviewHeight(){
        return _previewHeight;
    }


    private void configureCamera(){
        Camera.Parameters parameters = camera.getParameters();

        Camera.Size targetPreviewSize = getClosestSize(getWidth(), getHeight(), parameters.getSupportedPreviewSizes());
        parameters.setPreviewSize(targetPreviewSize.width, targetPreviewSize.height);

        _previewWidth = targetPreviewSize.width;
        _previewHeight =  targetPreviewSize.height;

        Toast.makeText(_activity, Integer.toString(targetPreviewSize.width) +  " " + Integer.toString(targetPreviewSize.height), Toast.LENGTH_SHORT).show();

        Camera.Size targetImageSize = getClosestSize(1024, 1280, parameters.getSupportedPictureSizes());

        Toast.makeText(_activity, "picture " + Integer.toString(targetImageSize.width) +  " " + Integer.toString(targetImageSize.height), Toast.LENGTH_SHORT).show();
        parameters.setPictureSize(targetImageSize.width, targetImageSize.height);
        //parameters.setPictureSize(targetPreviewSize.width, targetPreviewSize.height);

        camera.setDisplayOrientation(90);

        _activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        camera.setParameters(parameters);
    }

    private Camera.Size getClosestSize(int width, int height, List<Camera.Size> supportedSizes) {

        final double ASPECT_TLERANCE =.1;
        double targetRation = (double)height/width;

        Camera.Size targetSize = null;
        double minDifference = Double.MAX_VALUE;

        for ( Camera.Size size: supportedSizes){
            double ration=(double) size.width/size.height;
            if(Math.abs(ration - targetRation)>ASPECT_TLERANCE){
                continue;
            }
            int heightDifference =Math.abs(size.height   -height);
            if(heightDifference < minDifference){
                targetSize = size;
                minDifference = heightDifference;
            }
        }

        if (targetSize == null) {
            minDifference = Double.MAX_VALUE;
            for (Camera.Size size: supportedSizes){
                int heightDifference = Math.abs(size.height - height);
                if(heightDifference <minDifference){
                    targetSize = size;
                    minDifference = heightDifference;
                }
            }
        }
        return targetSize;
    }


}
