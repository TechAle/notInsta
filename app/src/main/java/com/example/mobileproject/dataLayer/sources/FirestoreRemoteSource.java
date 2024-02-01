package com.example.mobileproject.dataLayer.sources;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import android.util.Log;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Users.Users;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
        if ((int) (Math.random() * 3) == 1) {
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
                                if ((int) (Math.random() * 3) != 1) {
                                    ArrayList<Post> output = new ArrayList<>();
                                    output.add(sponsors.get((int) (Math.random() * sponsors.size())));
                                    c.onSuccess(output);
                                    return;
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

    @Override
    public void createPosts(Post post, CallbackPosts ci) {
        Map<String, Object> documentFields = new HashMap<>();
        documentFields.put("autore", post.getAutore());
        documentFields.put("likes", post.getLikes());
        documentFields.put("promozionale", post.getPromozionale());
        documentFields.put("data", post.getPubblicazione());
        documentFields.put("tags", post.getTags());
        documentFields.put("descrizione", post.getDescrizione());
        createDocument("post", documentFields, ci);
    }

    @Override
    public void createUser(Users post, CallbackUsers ci) {
        Map<String, Object> documentFields = new HashMap<>();
        documentFields.put("cognome", post.getCognome());
        documentFields.put("nome", post.getNome());
        documentFields.put("dataNascita", post.getDataNascita());
        documentFields.put("descrizione", post.getDescrizione());
        documentFields.put("followers", post.getFollowers());
        documentFields.put("following", post.getFollowing());
        documentFields.put("tags", post.getTags());
        documentFields.put("username", post.getUsername());
        createDocument("utenti", documentFields, ci);
    }


    @Override
    public void createDocument(String collectionName, Map<String, Object> documentFields, CallbackInterface ci) {
        // Add the new document to our shared collection
        db.collection(collectionName)
                .add(documentFields)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        ci.onUploadSuccess(task.getResult().getId());
                    else
                        ci.onFailure(new Exception("Error creating document"));
                });
    }

    @Override
    public void createImage(Uri imageUri, String document, ContentResolver context, CallbackInterface ci, String id) {
        if (imageUri != null) {
            try {
                // Convert the image to PNG format
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context, imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                // Create a unique filename for the uploaded image
                String fileName = id + ".png";

                StorageReference storageReference = FirebaseStorage.getInstance().getReference(document);

                StorageReference fileReference = storageReference.child(fileName);


                fileReference.putBytes(data).addOnSuccessListener(ris -> {
                    ci.onSuccess();
                }).addOnFailureListener(ris -> {
                    ci.onFailure(new Exception("Caricamento fallito"));
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void postPost() {
    }

}
