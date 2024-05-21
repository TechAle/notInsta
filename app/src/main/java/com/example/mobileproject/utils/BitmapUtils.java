package com.example.mobileproject.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//TODO: credo che questo debba stare in una source [CCL]
@Deprecated
public class BitmapUtils {

    public static Uri getUriFromBitmap(Context context, Bitmap bitmap) throws FileNotFoundException {
        // Save the Bitmap to a file
        File imageFile = saveBitmapToFile(context, bitmap);

        // Convert the File to a Uri
        return Uri.fromFile(imageFile);
    }

    private static File saveBitmapToFile(Context context, Bitmap bitmap) throws FileNotFoundException {
        // Get the external storage directory
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Generate a unique file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Create the file
        File imageFile = new File(storageDir, imageFileName + ".jpg");

        try {
            // Save the Bitmap to the file
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Notify the system that a new file has been created
        notifyMediaScanner(context, imageFile);

        return imageFile;
    }

    private static void notifyMediaScanner(Context context, File file) throws FileNotFoundException {
        // Notify the media scanner to add the file to the gallery
        MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }
}
