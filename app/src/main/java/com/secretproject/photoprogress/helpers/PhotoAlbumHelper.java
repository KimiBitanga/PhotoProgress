package com.secretproject.photoprogress.helpers;

import android.graphics.Bitmap;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by BO on 14-Mar-16.
 */
public class PhotoAlbumHelper {

    private static Collection<PhotoAlbum> AllPhotoAlbums;

    private static String folderName = "PhotoProgress";

    public static PhotoAlbum CurrentPhotoAlbum;

    static {
        if (CurrentPhotoAlbum == null){
            CurrentPhotoAlbum = new PhotoAlbum();
        }
    }

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

        if(photoAlbum==null)
            return;

        Collection<PhotoAlbum> photoAlbums = getAllPhotoAlbums();

        for (PhotoAlbum albumInList : photoAlbums){
            if (photoAlbum.getId() == albumInList.getId()){
                boolean success  = photoAlbums.remove(albumInList);

                if (!success){
                    throw new Exception("Throw better exception!!!");
                }

                deleteAlbumPhotos(photoAlbum.getId());
            }
        }

        writeAllPhotoAlbums(photoAlbums);
    }

    private static void deleteAlbumPhotos(int albumId) {

        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folderName + File.separator );
        File files[] = directory.listFiles();
        for (File f : files) {
            if(f.getName().startsWith(Integer.toString(albumId) + "_")){
                f.delete();
            }
        }
    }

    public static ArrayList<File> getAllAlbumPhotos(int albumId){
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folderName + File.separator );
        File files[] = directory.listFiles();


        ArrayList<File> albumPhotos = new ArrayList<File>();
        for (File f : files) {
            if(f.getName().startsWith(Integer.toString(albumId) + "_")){
                albumPhotos.add(f);
            }
        }

        Collections.sort(albumPhotos, new Comparator<File>() {
            @Override
            public int compare(File file2, File file1)
            {
                return new Date(file2.lastModified()).compareTo(new Date(file1.lastModified()));
            }
        });

        return albumPhotos;
    }

    public static Bitmap getScaledBitmap(Bitmap origianlImage, int outputImgHeigh) {

        if(origianlImage==null)
            return null;

        float aspectRation = (float) origianlImage.getWidth() / origianlImage.getHeight();
        int imgWidth = Math.round(aspectRation * outputImgHeigh);

        return Bitmap.createScaledBitmap(origianlImage, imgWidth, outputImgHeigh, false);
    }
}
