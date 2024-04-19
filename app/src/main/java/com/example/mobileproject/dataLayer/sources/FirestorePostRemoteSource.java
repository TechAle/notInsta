package com.example.mobileproject.dataLayer.sources;

import android.app.Application;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import static com.example.mobileproject.utils.Constants.ELEMENTS_LAZY_LOADING;
import com.example.mobileproject.dataLayer.repositories.ProductsRepository; //TODO: da quando si utilizza una reference ad una classe di uno strato superiore?
import com.example.mobileproject.models.Post.Post;
import com.google.firebase.firestore.DocumentReference;
import com.example.mobileproject.models.Product;
import com.example.mobileproject.utils.Result;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe per il recupero remoto dei post da Firebase
 */

public class FirestorePostRemoteSource extends GeneralPostRemoteSource{
    FirebaseFirestore db;
    FirebaseStorage storage;
    Application app;
    public FirestorePostRemoteSource(){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
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
                                    c.onSuccessG(output);//TODO: Controllare
                                    return;
                                }
                            }
                        }
                        c.onFailureG(new Exception("No sponsor"));
                    });


    }
    @Override
    public void retrievePostsByAuthor(@NonNull String idUser, int page){
        DocumentReference refUser = db.collection("utenti").document(idUser);
        db.collection("post")
                .whereEqualTo("autore", refUser)
                .orderBy("data")
                .startAfter(page * ELEMENTS_LAZY_LOADING)
                .limit(ELEMENTS_LAZY_LOADING)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for (QueryDocumentSnapshot i : task.getResult()) {
                            Map<String, Object> m = i.getData();
                            Post p = new Post(m, i.getId());
                            results.add(p);
                        }
                        c.onSuccessO(results);
                    } else {
                        c.onFailureO(task.getException());
                    }
                });
    }
    @Override
    public void createPosts(Post post) {
        Map<String, Object> documentFields = new HashMap<>();
        documentFields.put("autore", post.getAutore());
        documentFields.put("likes", post.getLikes());
        documentFields.put("promozionale", post.isPromozionale());
        documentFields.put("data", post.getPubblicazione());
        documentFields.put("tags", post.getTags());
        documentFields.put("descrizione", post.getDescrizione());
        createDocument("post", documentFields, c);
    }
    @Override
    protected void createDocument(String collectionName, Map<String, Object> documentFields, @Deprecated CallbackInterface ci) {
        // Add the new document to our shared collection
        db.collection(collectionName)
                .add(documentFields)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        c.onUploadSuccess(task.getResult().getId());
                    else
                        c.onFailure(new Exception("Error creating document"));
                });
    }

    @Override
    public void createImage(Uri imageUri, String document, ContentResolver context, @Deprecated CallbackInterface ci, String id) { //alla fine l'immagine è comunque parte del post
        if (imageUri != null) {
            try {
                // Convert the image to PNG format
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context, imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                // Create a unique filename for the uploaded image
                String fileName = id + ".png";
                storage.getReference(document)
                        .child(fileName)
                        .putBytes(data)
                        .addOnSuccessListener(r -> {
                            c.onSuccess();
                        })
                        .addOnFailureListener(r -> {
                            c.onFailure(new Exception("Caricamento fallito"));
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void retrievePostsLL(int page){
        db.collection("post")
                //.whereNotEqualTo("autore", <segnaposto per l'autore>)  //l'utente quando cerca altri post non cerca i propri
                .orderBy("data")
                .startAfter(page * ELEMENTS_LAZY_LOADING)
                .limit(ELEMENTS_LAZY_LOADING)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for(QueryDocumentSnapshot i : task.getResult()){
                            Map<String, Object> m = i.getData();
                            Post p = new Post(m, i.getId());
                            results.add(p);
                        }
                        c.onSuccessG(results);
                    }
                    else{
                        c.onFailure(task.getException());
                    }
                });
    }
    @Override
    public void retrievePostsWithTagsLL(String[] tags, int page) {//mero segnaposto, è come quello normale
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
                        c.onSuccessG(results);
                    }
                    else{
                        c.onFailure(task.getException());
                    }
                });
    }

/*    @Override
    public void retrievePostsForSync(Date lastUpdate){
        db.collection("posts")
            //.whereEqualTo("autore", <segnaposto per l'autore>)
            .whereLessThan("data", lastUpdate)
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
    }*/

/*    @Override
    public void retrievePostsForSync(){
        db.collection("posts")
                //.whereEqualTo("autore", <segnaposto per l'autore>)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for(QueryDocumentSnapshot i : task.getResult()){
                            Map<String, Object> m = i.getData();
                            Post p = new Post(m, i.getId());
                            results.add(p);
                        }
                        c.onSuccessInSync(results);
                    }
                    else{
                        c.onFailure(task.getException());
                    }
                });
    }*/
  
    private void createSponsorFromApi(LifecycleOwner life) {
        ProductsRepository t = new ProductsRepository(app); //TODO: controllare questo riferimento alla repository (da livello sottostante...)
        t.fetchProducts(1).observe(life, task -> {
            if (task.successful()) {
                List<Product> resp = ((Result.ProductSuccess) task).getData();
                Product pr = resp.get(0);
                ArrayList<Post> output = new ArrayList<>();
                Post temp = new Post();
                temp.setDescrizione(pr.getTitle());
                temp.setImage(pr.getImage());
                output.add(temp);
                c.onSuccessG(output);
            } else {
                c.onFailure(new Exception("No post"));
            }
        });
    }
}