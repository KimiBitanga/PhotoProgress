package com.secretproject.photoprogress.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.helpers.PhotoAlbumHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PosterActivity extends AppCompatActivity {

    Bitmap resultImage;
    ArrayList<Bitmap> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        if (PhotoAlbumHelper.CurrentPhotoAlbum == null){
            this.finish();
        }

        images = (ArrayList)PhotoAlbumHelper.CurrentPhotoAlbum.getAlbumPhotos();

        double root = Math.sqrt(images.size());

        int imagesInRow = (int)Math.floor(root);
        int imagesInColumn = imagesInRow;

        if (Math.floor(root) < root){
            imagesInRow++;
        }

        //setPoster(images, imagesInRow, imagesInColumn);

        Spinner columnsSpinner = (Spinner) findViewById(R.id.columnsSpinner);

        Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        columnsSpinner.setAdapter(adapter);

        columnsSpinner.setSelection(imagesInRow - 1);

        onSaveButtonListener();
        onCancelButtonListener();
        onSpinnerSelectedListener();
    }

    public void onCancelButtonListener() {
        Button cancelBtn = (Button) findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
    }

    public void onSpinnerSelectedListener(){
        Spinner columnsSpinner = (Spinner) findViewById(R.id.columnsSpinner);
        columnsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int imagesInRow = position + 1;
                int imagesInColumn = images.size() / imagesInRow;

                if (images.size() % imagesInRow != 0) {
                    imagesInColumn++;
                }

                setPoster(images, imagesInRow, imagesInColumn);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    public void onSaveButtonListener() {
        Button cancelBtn = (Button) findViewById(R.id.btnSave);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    File file = new File(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "PhotoProgress" + File.separator), getImageName());
                    if (file.exists()) {
                        file.delete();
                    }

                    FileOutputStream out = new FileOutputStream(file);
                    resultImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                closeActivity();
            }
        });
    }

    private void setPoster(ArrayList<Bitmap> images, int imagesInRow, int imagesInColumn){
        if (resultImage != null) {
            resultImage.recycle();
            resultImage = null;
        }
        Bitmap result = Bitmap.createBitmap((images.get(0).getWidth() + 40) * imagesInRow, (images.get(0).getHeight() + 40) * imagesInColumn, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        for (int i = 0; i < images.size(); i++) {
            Bitmap currentImage = addWhiteBorder(images.get(i), 20);
            canvas.drawBitmap(currentImage, currentImage.getWidth() * (i % imagesInRow), currentImage.getHeight() * (i / imagesInRow), paint);
        }

        ImageView posterView = (ImageView) findViewById(R.id.imgViewPoster);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        Bitmap scaledImage = PhotoAlbumHelper.getScaledBitmap(result, width / 2);

        posterView.setImageBitmap(scaledImage);

        resultImage = result;
    }

    private String getImageName() {

        return "Poster_" + Integer.toString(PhotoAlbumHelper.CurrentPhotoAlbum.getId()) + "_" + (new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date())) + ".jpg";
    }

    private Bitmap addWhiteBorder(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }

    private void closeActivity() {
        this.finish();
    }

}
