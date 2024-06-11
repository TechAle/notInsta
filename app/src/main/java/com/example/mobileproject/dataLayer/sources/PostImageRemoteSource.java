package com.example.mobileproject.dataLayer.sources;

import android.graphics.Bitmap;

import androidx.concurrent.futures.CallbackToFutureAdapter;

import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.Future;

public final class PostImageRemoteSource extends GeneralPostImageRemoteSource{
    FirebaseStorage storage = FirebaseStorage.getInstance();
    @Override
    public Future<Boolean> createImage(String id, Bitmap bmp) {
        return CallbackToFutureAdapter.getFuture(completer -> {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();
            String fileName = id + ".png";
            storage.getReference("POSTS")
                    .child(fileName)
                    .putBytes(data)
                    .addOnSuccessListener(r -> completer.set(true))
                    .addOnFailureListener(r -> completer.set(false));
            return "Image creation";
        });
    }
    @Override
    public Future<Boolean> retrieveImage(String id, File dst){
        return CallbackToFutureAdapter.getFuture(completer -> {
            storage.getReference().child("POSTS/" + id + ".png").getFile(dst).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    completer.set(true);
                } else {
                    completer.set(false);
                }
            });
            return "Remote image";
        });
    }
}
