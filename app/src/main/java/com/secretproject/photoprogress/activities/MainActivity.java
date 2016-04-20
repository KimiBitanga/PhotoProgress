package com.secretproject.photoprogress.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.adapters.AlbumAdapter;
import com.secretproject.photoprogress.data.PhotoAlbum;
import com.secretproject.photoprogress.helpers.PhotoAlbumHelper;
import org.lucasr.twowayview.TwoWayView;
import java.util.ArrayList;
import java.util.Collection;


public class MainActivity extends Activity {

    private AlbumAdapter albumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        onCreateNewAlbumButtonListener();

        initializeAlbumListView();
    }

    private void initializeAlbumListView() {

        ArrayList<PhotoAlbum> albums=new ArrayList<PhotoAlbum>();
        albumAdapter = new AlbumAdapter(this, albums);
        TwoWayView  albumTwoWayView = (TwoWayView)findViewById(R.id.albumsTwoWayView);
        albumTwoWayView.setAdapter(albumAdapter);
        albumTwoWayView.setOnItemClickListener(new
                                                       AdapterView.OnItemClickListener() {
                                                           @Override
                                                           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                               PhotoAlbumHelper.CurrentPhotoAlbum = (PhotoAlbum) parent.getItemAtPosition(position);
                                                               startActivity(new Intent(MainActivity.this, PhotoAlbumOverviewActivity.class));
                                                           }
                                                       });
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

    @Override
    public void onResume() {
        super.onResume();

        PhotoAlbumHelper.CurrentPhotoAlbum = null;

        refreshAlbumListView();
    }

    private void refreshAlbumListView() {
        Collection<PhotoAlbum> albums = PhotoAlbumHelper.getAllPhotoAlbums();

        if (albums != null) {
            albumAdapter.clear();
            albumAdapter.addAll(albums);
            albumAdapter.notifyDataSetChanged();
        }
    }

}
