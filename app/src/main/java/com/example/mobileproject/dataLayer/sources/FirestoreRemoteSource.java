package com.example.mobileproject.dataLayer.sources;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Users.Users;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Classe per il recupero remoto
 */

public class FirestoreRemoteSource extends GeneralPostRemoteSource{

    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;
    public FirestoreRemoteSource(){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    @Override
    public void retrievePosts(CallbackPosts c){
        db.collection("post").get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for(QueryDocumentSnapshot i : task.getResult()){
                            Map<String, Object> m = i.getData();
                            Post p = new Post(m);
                            results.add(p);
                        }
                        c.onSuccess(results);
                    }
                    else{
                        c.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void retrieveUsers(CallbackUsers c){
        db.collection("utenti").get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Users> results = new ArrayList<>();
                        for(QueryDocumentSnapshot i : task.getResult()){
                            Map<String, Object> m = i.getData();
                            Users p = new Users(m);
                            results.add(p);
                        }
                        c.onSuccess(results);
                    }
                    else{
                        c.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void retrievePostsWithTags(String[] tags, CallbackPosts c) {
        db.collection("post").get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for(QueryDocumentSnapshot i : task.getResult()){
                            Map<String, Object> m = i.getData();
                            Post p = new Post(m);
                            for (String tag: tags) {
                                if (p.tags.contains(tag)) {
                                    results.add(p);
                                    break;
                                }
                            }
                        }
                        c.onSuccess(results);
                    }
                    else{
                        c.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void retrievePostByDocumentId(String tag, CallbackPosts c){
        db.collection("post").document(tag)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Map<String, Object> m = task.getResult().getData();
                        Post p = new Post(m);
                        ArrayList<Post> results = new ArrayList<>();
                        results.add(p);
                        c.onSuccess(results);
                    }
                    else{
                        c.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void retrieveUserByDocumentId(String tag, CallbackUsers c){
        db.collection("utenti").document(tag)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Map<String, Object> m = task.getResult().getData();
                        Users p = new Users(m);
                        ArrayList<Users> results = new ArrayList<>();
                        results.add(p);
                        c.onSuccess(results);
                    }
                    else{
                        c.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void postPost() {
        c.onUploadSuccess();
        //TODO: Funzione vuota. Modificarla o eliminarla
    }
}
