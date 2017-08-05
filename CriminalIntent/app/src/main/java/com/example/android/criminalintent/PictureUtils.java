package com.example.android.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import static android.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.media.ExifInterface.ORIENTATION_ROTATE_270;
import static android.media.ExifInterface.ORIENTATION_ROTATE_90;

/**
 * Created by RavenSP on 4/8/2017.
 */

// orientation information is stored into the picture (EXIF meta data)

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        //read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        //figure out how much to scale down by
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            float heightScale = srcHeight / destHeight;
            float widthScale = srcWidth / destWidth;

            inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        Bitmap bm = BitmapFactory.decodeFile(path, options);
        Matrix matrix;

        try {
            ExifInterface exif = new ExifInterface(new File(path).getAbsolutePath());
            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;
            int rotationAngle = 0;
            Log.d("CAMERA", "ORIENTATION + " + orientString + " | " + ORIENTATION_ROTATE_90 + " | " + ORIENTATION_ROTATE_180 + " | " + ORIENTATION_ROTATE_270 );
            if (orientation == ORIENTATION_ROTATE_90) rotationAngle = 90;
            if (orientation == ORIENTATION_ROTATE_180) rotationAngle = 180;
            if (orientation == ORIENTATION_ROTATE_270) rotationAngle = 270;

            matrix = new Matrix();
            matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, options.outWidth, options.outHeight, matrix, true);


            return rotatedBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }



        //read in and create final bitmap
        return null;
    }

    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }
}
