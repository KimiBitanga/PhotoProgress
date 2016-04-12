package com.secretproject.photoprogress.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.secretproject.photoprogress.R;
import com.secretproject.photoprogress.helpers.PhotoAlbumHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by BO on 11-Apr-16.
 */
public class PhotosHorizontalListAdapter extends ArrayAdapter<File> {

public PhotosHorizontalListAdapter (Context context, ArrayList<File> values){
        super(context, R.layout.photo_list_view, values);
        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(getContext());
        View view=inflater.inflate(R.layout.photo_list_view, parent, false);

        File photoFile = getItem(position);

        ImageView photoImageIV=(ImageView)view.findViewById(R.id.photoIV);
        photoImageIV.setImageBitmap(PhotoAlbumHelper.getScaledBitmap(BitmapFactory.decodeFile(photoFile.getAbsolutePath()), 300));

        TextView photoCreationDate=(TextView) view.findViewById(R.id.photo_creation_date_TV);
        photoCreationDate.setText(new Date(photoFile.lastModified()).toString());

        return view;
        }
}
