package com.example.mobileproject.dataLayer.sources;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.Constants;
import com.example.mobileproject.utils.DBConverter;
import com.google.common.util.concurrent.SettableFuture;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.Future;

public final class PostLocalSource extends GeneralPostLocalSource{

    private final PostDao d;
    private final Context context;

    public PostLocalSource(@NonNull PostRoomDatabase db, Context c){
        this.d = db.postDao();
        this.context = c;
    }

    @Override
    public void insertPosts(List<Post> l){
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            d.insertAll(l);
            //c.onLocalSaveSuccess();
        });
    }

    @Override
    public void insertPost(Post p){
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            d.insertPost(p);
            c.onLocalSaveSuccess();
        });
    }
    @Override
    public void retrievePosts(int page) {
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Post> res = d.getUserPosts(page* Constants.ELEMENTS_LAZY_LOADING);
            c.onSuccessO(res);
        });
    }

    @Override
    public Future<List<Post>> retrieveNoSyncPosts() {
        SettableFuture<List<Post>> f = SettableFuture.create();
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Post> res = d.getNoSyncPosts();
            //c.onSuccessSyncLocal(res);
            f.set(res);
        });
        return f;
    }
    @Override
    public Future<List<String>> retrieveIDsWithNoImage(){
        SettableFuture<List<String>> f = SettableFuture.create();
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<String> res = d.getIDsWithNoImage();
            f.set(res);
        });
        return f;
    }
    @Override
    public void modifyId(String oldId, String newId){
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
           Post p = d.getPost(oldId);
           d.deletePost(p);
           p.setId(newId);
           d.insertPost(p);
        });
    }
    @Override
    public void modifyImage(String id, Uri img){
        PostRoomDatabase.databaseWriteExecutor.execute(() -> d.updateImage(id, DBConverter.fromUri(img)));
    }
    @Override
    public void updatePost(Post p){
        PostRoomDatabase.databaseWriteExecutor.execute(() -> d.updatePost(p));
    }
    @Override
    public void deletePosts() {
        PostRoomDatabase.databaseWriteExecutor.execute(d::deleteAll);
    }

    /*@Override
    public Bitmap getAndRenameImage(String oldId, String newId){/*
        File f = new File(context.getFilesDir(), newId + ".png");
        return BitmapFactory.decodeFile(f.getPath());
        return null;
    }*/

    @Override
    public Bitmap getImage(Uri img){
        //File f = new File(context.getFilesDir(), id + ".png");
        return BitmapFactory.decodeFile(img.getPath());

        //return null;
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
    public File createEmptyImageFile(){
        return createEmptyImageFile("tmp");
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