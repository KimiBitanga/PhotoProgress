package com.secretproject.photoprogress.helpers;

import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Xml;

import com.secretproject.photoprogress.data.PhotoAlbum;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by milan.curcic on 9.3.2016..
 */
public class XmlHelper {

    public static String saveToXmlFile(Collection<PhotoAlbum> settings) throws Exception {

        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        xmlSerializer.setOutput(writer);

        xmlSerializer.startDocument("UTF-8", true);

        xmlSerializer.startTag("", PhotoAlbum.PHOTO_ALBUM + "s");

        for(PhotoAlbum photoAlbum : settings) {
            xmlSerializer.startTag("", PhotoAlbum.PHOTO_ALBUM);

            xmlSerializer.startTag("", PhotoAlbum.ID);
            xmlSerializer.text(String.valueOf(photoAlbum.getId()));
            xmlSerializer.endTag("", PhotoAlbum.ID);

            xmlSerializer.startTag("", PhotoAlbum.NAME);
            xmlSerializer.text(photoAlbum.getName());
            xmlSerializer.endTag("", PhotoAlbum.NAME);

            xmlSerializer.startTag("", PhotoAlbum.DESCRIPTION);
            xmlSerializer.text(photoAlbum.getDescription());
            xmlSerializer.endTag("", PhotoAlbum.DESCRIPTION);

            xmlSerializer.startTag("", PhotoAlbum.NOTIFICATION_TIME);
            xmlSerializer.text(String.valueOf(photoAlbum.getNotificationTime()));
            xmlSerializer.endTag("", PhotoAlbum.NOTIFICATION_TIME);

            xmlSerializer.endTag("", PhotoAlbum.PHOTO_ALBUM);
        }

        xmlSerializer.endTag("", PhotoAlbum.PHOTO_ALBUM + "s");

        xmlSerializer.endDocument();

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "PhotoProgress");
        if (!folder.exists()) {
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }

            if (success) {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PhotoProgress/Settings.xml");
                success = file.createNewFile();
            }
        }

        FileOutputStream fos = new FileOutputStream (Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "PhotoProgress/Settings.xml");

        fos.write(writer.toString().getBytes());
        fos.close();

        return writer.toString();
    }

    public static Collection<PhotoAlbum> loadSettingsFromXml() throws Exception {

        Collection<PhotoAlbum> settings = new ArrayList<PhotoAlbum>();
        PhotoAlbum photoAlbum = null;

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "PhotoProgress" + "/", "Settings.xml");
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        char[] inputBuffer = new char[fis.available()];
        isr.read(inputBuffer);
        String data = new String(inputBuffer);
        isr.close();
        fis.close();

        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(data));
            int eventType = parser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType) {

                    case XmlPullParser.START_DOCUMENT:
                        photoAlbum = new PhotoAlbum();
                        break;

                    case XmlPullParser.START_TAG:

                        String tagName = parser.getName();

                        if(tagName.equalsIgnoreCase(PhotoAlbum.ID)) {
                            photoAlbum.setId(Integer.parseInt(parser.nextText()));
                        }

                        else if(tagName.equalsIgnoreCase(PhotoAlbum.NAME)) {
                            photoAlbum.setName(parser.nextText());
                        }

                        else if(tagName.equalsIgnoreCase(PhotoAlbum.DESCRIPTION)) {
                            photoAlbum.setDescription(parser.nextText());
                        }

                        else if(tagName.equalsIgnoreCase(PhotoAlbum.NOTIFICATION_TIME)) {
                            photoAlbum.setNotificationTime(Long.parseLong(parser.nextText()));
                        }
                        break;

                    case XmlPullParser.END_TAG:

                        if(parser.getName().equalsIgnoreCase(PhotoAlbum.PHOTO_ALBUM)) {
                            settings.add(photoAlbum);
                            photoAlbum = new PhotoAlbum();
                        }
                }
                // jump to next event
                eventType = parser.next();
            }
            // exception stuffs
        } catch (XmlPullParserException e) {
            settings = null;
        } catch (IOException e) {
            settings = null;
        }

        return settings;
    }
}
