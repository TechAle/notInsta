package com.example.mobileproject.dataLayer.sources;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Users.Users;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
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
                            Post p = new Post(m, i.getId());
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
                            Users p = new Users(m, i.getId());
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
                            Post p = new Post(m, i.getId());
                            for (String tag: tags) {
                                if (p.getTags().contains(tag)) {
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
    public void retrievePostsSponsor(CallbackPosts c) {
        ArrayList<Post> sponsors = new ArrayList<>();
        if (Math.random() * 3 == 1) {
            // Chiamata API
            // Prendi il titolo, l'immagine, e suppongo anche il link
            ArrayList<Post> output = new ArrayList<>();
            Post temp = new Post();

            output.add(temp);
            c.onSuccess(output);
        } else
            db.collection("post")
                    .whereEqualTo("promozionale", true)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot i : task.getResult()){
                                Map<String, Object> m = i.getData();
                                Post p = new Post(m, i.getId());
                                sponsors.add(p);
                            }
                            if (sponsors.size() > 0) {
                                if (Math.random()*3!=1) {
                                    ArrayList<Post> output = new ArrayList<>();
                                    output.add(sponsors.get((int) (Math.random() * sponsors.size())));
                                    c.onSuccess(output);

                                }
                            }
                        }
                        c.onFailure(new Exception("No sponsor"));
                    });


    }


    @Override
    public void retrievePostByDocumentId(String tag, CallbackPosts c){
        db.collection("post").document(tag)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Map<String, Object> m = task.getResult().getData();
                        Post p = new Post(m, tag);
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
                        Users p = new Users(m, tag);
                        ArrayList<Users> results = new ArrayList<>();
                        results.add(p);
                        c.onSuccess(results);
                    }
                    else{
                        c.onFailure(task.getException());
                    }
                });
    }

    public void editUsername(String tag, String newUsername, CallbackUsers c) {
        // Controllo se nessuno ha quell'username
        db.collection("utenti")
                .whereEqualTo("username", newUsername)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // Allora possiamo cambiarlo
                            updateField("utenti", tag, "username", newUsername, c);
                        } else c.onFailure(new Exception("Someone already has this error"));
                    } else c.onFailure(new Exception("Firebase error"));
                });
    }

    private void updateField(String collectionName, String documentId, String fieldToUpdate, Object newValue, CallbackInterface c) {
        // Create a map to represent the field to be updated
        Map<String, Object> updates = new HashMap<>();
        updates.put(fieldToUpdate, newValue);

        // Update the document with the new field value
        db.collection(collectionName)
                .document(documentId)
                .update(updates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        c.onSuccess();
                    } else {
                        c.onFailure(new Exception("Firebase errore field"));
                    }
                });
    }

    private void createDocument(String collectionName, Map<String, Object> documentFields, FirebaseFirestore firestore, CallbackInterface ci) {
        // Add the new document to our shared collection
        firestore.collection(collectionName)
                .add(documentFields)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        ci.onSuccess();
                    else
                        ci.onFailure(new Exception("Error creating document"));
                });
    }

    @Override
    public void postPost() {
        c.onUploadSuccess();
        //TODO: Funzione vuota. Modificarla o eliminarla
    }
}
