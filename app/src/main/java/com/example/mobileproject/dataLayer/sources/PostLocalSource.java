package com.example.mobileproject.dataLayer.sources;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.Constants;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostLocalSource extends GeneralPostLocalSource{

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
            c.onLocalSaveSuccess();
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
    public void retrieveNoSyncPosts() {
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Post> res = d.getNoSyncPosts();
            //c.onSuccessSyncLocal(res);
        });

    }
    @Override
    public void modifyId(String oldId, String newId){
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
           //d.deletePost();
        });
    }
    @Override
    public void deletePosts() {
        PostRoomDatabase.databaseWriteExecutor.execute(d::deleteAll);
    }

    @Override
    public Bitmap getImage(String id){
        //TODO: controllare
        File f = new File(context.getFilesDir(), id + ".png");
        return BitmapFactory.decodeFile(f.getPath());
    }
    public Uri createImage(Bitmap bmp){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File imageFile = new File(context.getFilesDir(), imageFileName + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            return null;
        }
        return Uri.fromFile(imageFile);
    }
}