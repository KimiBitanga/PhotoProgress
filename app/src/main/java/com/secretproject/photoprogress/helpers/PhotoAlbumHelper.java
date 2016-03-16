package com.secretproject.photoprogress.helpers;

import android.os.Environment;

import com.secretproject.photoprogress.data.PhotoAlbum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Collection;

/**
 * Created by BO on 14-Mar-16.
 */
public class PhotoAlbumHelper {

    private static Collection<PhotoAlbum> AllPhotoAlbums;

    public static PhotoAlbum CurrentPhotoAlbum;

    public static Collection<PhotoAlbum> getAllPhotoAlbums(){

        if (AllPhotoAlbums != null){
            return AllPhotoAlbums;
        }

        ObjectInputStream input;
        Collection<PhotoAlbum> photoAlbums = null;
        String fileName = "Settings.srl";
        String folderName = "PhotoProgress";

        try {
            input = new ObjectInputStream(new FileInputStream(new File(new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + folderName, "")+File.separator + fileName)));
            photoAlbums = (Collection<PhotoAlbum>) input.readObject();

            input.close();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        AllPhotoAlbums = photoAlbums;

        return photoAlbums;
    }

    public static PhotoAlbum getPhotoAlbum(int id){

        Collection<PhotoAlbum> photoAlbums = getAllPhotoAlbums();

        for (PhotoAlbum photoAlbum : photoAlbums){
            if (photoAlbum.getId() == id){
                return photoAlbum;
            }
        }

        return null;
    }

    public static void writeAllPhotoAlbums(Collection<PhotoAlbum> photoAlbums){

        String fileName = "Settings.srl";
        String folderName = "PhotoProgress";
        ObjectOutput out = null;

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folderName);
        if (!folder.exists()) {
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }

            if (success) {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folderName + File.separator + fileName);
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            out = new ObjectOutputStream(new FileOutputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + folderName, "") + File.separator + fileName));
            out.writeObject(photoAlbums);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AllPhotoAlbums = photoAlbums;
    }

    public static void updatePhotoAlbum(PhotoAlbum photoAlbum)throws Exception{

        Collection<PhotoAlbum> photoAlbums = getAllPhotoAlbums();

        for (PhotoAlbum albumInList : photoAlbums){
            if (photoAlbum.getId() == albumInList.getId()){
                boolean success  = photoAlbums.remove(albumInList);

                if (success){
                    photoAlbums.add(photoAlbum);
                }
                else{
                    throw new Exception("Throw better exception!!!");
                }
            }
        }

        writeAllPhotoAlbums(photoAlbums);
    }

    public static void deletePhotoAlbum(PhotoAlbum photoAlbum)throws Exception{

        Collection<PhotoAlbum> photoAlbums = getAllPhotoAlbums();

        for (PhotoAlbum albumInList : photoAlbums){
            if (photoAlbum.getId() == albumInList.getId()){
                boolean success  = photoAlbums.remove(albumInList);

                if (!success){
                    throw new Exception("Throw better exception!!!");
                }
            }
        }

        writeAllPhotoAlbums(photoAlbums);
    }
}
