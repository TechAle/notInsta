package com.example.mobileproject.dataLayer.sources;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public final class PostImageLocalSource extends GeneralPostImageLocalSource{
    private final Context context;
    public PostImageLocalSource(Context c){
        context = c;
    }

    @Override
    public Bitmap getImage(Uri img){
        return BitmapFactory.decodeFile(img.getPath());
    }
    @Override
    public Uri createImage(Bitmap bmp, String id){
        File imageFile = createEmptyImageFile(id);
        if (imageFile == null){
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            return null;
        }
        //return imageFile.toURI();
        return Uri.fromFile(imageFile);
    }
    @Override
    public void deleteImages(){
        File[] lf = context.getFilesDir().listFiles();
        if(lf == null) throw new RuntimeException();
        for(File tmp : lf){
            try {
                Files.delete(tmp.toPath());
            } catch (IOException e) {

            }
        }
    }

    @Override
    public File createEmptyImageFile(String id){
        if(id.isEmpty()){
            throw new IllegalArgumentException();
        }
        File f = new File (context.getFilesDir(), id + ".png");
        try{
            if(!f.createNewFile()) {
                //TODO: clear file
            }
        } catch (IOException e){
            return null;
        }
        return f;
    }

    @Override
    public Uri renameImage(Uri img, String id){
        File f = new File(img.getPath());
        File newFile = createEmptyImageFile(id);
        if(f.renameTo(newFile)) return Uri.fromFile(f);
        else return null;
    }
    //TODO: in tutto questo, bisogna notificare il media scanner?
    /*private void notifyMediaScanner(Context context, File file) throws FileNotFoundException {
        // Notify the media scanner to add the file to the gallery
        MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }*/
    //File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
}
