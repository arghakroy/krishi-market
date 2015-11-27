package me.argha.sustproject.helpers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.argha.sustproject.utils.AppConst;

/**
 * Created by ARGHA K ROY on 6/2/2015.
 */
public class MediaHelper {

    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStorageDirectory(),
                AppConst.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(AppConst.DEBUG_KEY,AppConst.IMAGE_DIRECTORY_NAME+ "Oops! Failed create "
                    + AppConst.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == AppConst.SELECT_PHOTO_CAMERA) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }else {
            return null;
        }

        return mediaFile;
    }

    public static File getOutputMediaFile(Context context,String idName) {

        if(!isExternalStorageWritable()){
            Toast.makeText(context,"External storage is not available",Toast.LENGTH_SHORT).show();
        }

        // External sdcard location
        File mediaStorageDir = new File(
            Environment
                .getExternalStorageDirectory(),
            AppConst.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("JFK",AppConst.IMAGE_DIRECTORY_NAME+ "Oops! Failed create "
                    + AppConst.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        return new File(mediaStorageDir.getPath() + File.separator
               + idName + ".jpg");
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static String getRealPathFromURI(Context context,Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
