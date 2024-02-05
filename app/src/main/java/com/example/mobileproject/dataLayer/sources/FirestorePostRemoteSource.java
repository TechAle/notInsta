package com.example.mobileproject.dataLayer.sources;

import android.app.Application;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.lifecycle.LifecycleOwner;

//import com.example.mobileproject.dataLayer.repositories.ProductsRepository; //TODO: da quando si utilizza una reference ad una classe di uno strato superiore?
import com.example.mobileproject.dataLayer.repositories.ProductsRepository;
import com.example.mobileproject.models.Post.Post;
/*
import com.example.mobileproject.models.Users.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
*/
import com.google.firebase.firestore.DocumentReference;
import com.example.mobileproject.models.Product;
import com.example.mobileproject.utils.Result;

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

public class FirestorePostRemoteSource extends GeneralPostRemoteSource{
    FirebaseFirestore db;
    FirebaseStorage storage;
//    StorageReference storageRef;
//    FirebaseAuth firebaseAuth;
    Application app;
    public FirestorePostRemoteSource(){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReference();
//        firebaseAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void retrievePosts(){
        db.collection("post")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Post> results = new ArrayList<>();
                        for (QueryDocumentSnapshot i : task.getResult()) {
                            Map<String, Object> m = i.getData();
                            Post p = new Post(m, i.getId());
                            results.add(p);
                        }
                        c.onSuccess(results);
                    } else {
                        c.onFailure(task.getException());
                    }
                });
    }
    @Override
    public void retrievePostsWithTags(String[] tags) {
        db.collection("post").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Post> results = new ArrayList<>();
                        for (QueryDocumentSnapshot i : task.getResult()) {
                            Map<String, Object> m = i.getData();
                            Post p = new Post(m, i.getId());
                            if(p.getTags() == null) continue;
                            for (String tag : tags) {
                                if (p.getTags().contains(tag)) {
                                    results.add(p);
                                    break;
                                }
                            }
                        }
                        c.onSuccess(results);
                    } else {
                        c.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void retrievePostsSponsor(LifecycleOwner ow) {
        ArrayList<Post> sponsors = new ArrayList<>();
        if ((int) (Math.random() * 3) == 1) {
            createSponsorFromApi(ow);
        } else
            db.collection("post")
                    .whereEqualTo("promozionale", true)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot i : task.getResult()) {
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
    public void retrievePostByDocumentId(String tag){
        db.collection("post").document(tag)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, Object> m = task.getResult().getData();
                        Post p = new Post(m, tag);
                        ArrayList<Post> results = new ArrayList<>();
                        results.add(p);
                        c.onSuccess(results);
                    } else {
                        c.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void retrievePostsByAuthor(String idUser, int page){
        DocumentReference refUser = db.collection("utenti").document(idUser);
        db.collection("post")
                .whereEqualTo("autore", refUser)
                .orderBy("data")
                .startAfter(page * 20)
                .limit(20)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for (QueryDocumentSnapshot i : task.getResult()) {
                            Map<String, Object> m = i.getData();
                            Post p = new Post(m, i.getId());
                            results.add(p);
                        }
                        c.onSuccess(results);
                    } else {
                        c.onFailure(task.getException());
                    }
                });
    }
    @Override
    public void createPosts(Post post) {
        Map<String, Object> documentFields = new HashMap<>();
        documentFields.put("autore", post.getAutore());
        documentFields.put("likes", post.getLikes());
        documentFields.put("promozionale", post.getPromozionale());
        documentFields.put("data", post.getPubblicazione());
        documentFields.put("tags", post.getTags());
        documentFields.put("descrizione", post.getDescrizione());
        createDocument("post", documentFields, c);
    }
    @Override
    protected void createDocument(String collectionName, Map<String, Object> documentFields, CallbackInterface ci) {
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
    public void createImage(Uri imageUri, String document, ContentResolver context, CallbackInterface ci, String id) { //alla fine l'immagine è comunque parte del post
        if (imageUri != null) {
            try {
                // Convert the image to PNG format
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context, imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                // Create a unique filename for the uploaded image
                String fileName = id + ".png";

                StorageReference storageReference = storage.getReference(document);

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
    public void retrievePostsLL(int page){ //Lazy loading
        db.collection("post")
                .orderBy("data")
                .startAfter(page * 20)
                .limit(20)
                .get()
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
    public void retrievePostsWithTagsLL(String tags[], int page) {//mero segnaposto, è come quello normale
        db.collection("post").get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for(QueryDocumentSnapshot i : task.getResult()){
                            Map<String, Object> m = i.getData();
                            Post p = new Post(m, i.getId());
                            if(p.getTags() == null) continue;
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
  
    private void createSponsorFromApi(LifecycleOwner life) {
        ProductsRepository t = new ProductsRepository(app);
        t.fetchProducts(1).observe(life, task -> {
            if (task.successful()) {
                List<Product> resp = ((Result.ProductSuccess) task).getData();
                Product pr = resp.get(0);
                ArrayList<Post> output = new ArrayList<>();
                Post temp = new Post();
                temp.setDescrizione(pr.getTitle());
                temp.setImage(pr.getImage());
                output.add(temp);
                c.onSuccess(output);
            } else {
                c.onFailure(new Exception("No post"));
            }
        });
    }
}