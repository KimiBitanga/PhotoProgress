package com.secretproject.photoprogress.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.data.PhotoAlbum;
import com.secretproject.photoprogress.helpers.PhotoAlbumHelper;

import java.util.ArrayList;


/**
 * Created by BO on 06-Apr-16.
 */
public class AlbumAdapter extends ArrayAdapter<PhotoAlbum> {

    public AlbumAdapter (Context context, ArrayList<PhotoAlbum> values){
        super(context, R.layout.album_list_view, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(getContext());
        View view=inflater.inflate(R.layout.album_list_view, parent, false);

        PhotoAlbum album = getItem(position);

        ImageView albumImageIV=(ImageView)view.findViewById(R.id.albumImageIV);
        albumImageIV.setImageBitmap(PhotoAlbumHelper.getScaledBitmap(album.getLastPhoto(), 400));

        TextView albumNameTv=(TextView) view.findViewById(R.id.albumNameTV);
        albumNameTv.setText(album.getName());

        TextView albumDescriptionTv=(TextView) view.findViewById(R.id.albumDescriptionTV);
        albumDescriptionTv.setText(album.getDescription());

        return view;
    }
}
