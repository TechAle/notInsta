package com.example.mobileproject.dataLayer.sources;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import static com.example.mobileproject.utils.Constants.ELEMENTS_LAZY_LOADING;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.DBConverter;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
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
//TODO: mettere una Uri nel campo "immagine" per ogni post preso
public class FirestorePostRemoteSource extends GeneralPostRemoteSource{
    FirebaseFirestore db;
    FirebaseStorage storage;
    public FirestorePostRemoteSource(){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }
    @Override
    public void retrievePostsSponsor() {
        db.collection("post")
                .whereEqualTo("promozionale", true)
                .get() //TODO: gestione immagini
                .addOnCompleteListener(task -> {
                    ArrayList<Post> sponsors = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot i : task.getResult()) {
                            Map<String, Object> m = i.getData();
                            m.replace("data", ((Timestamp) m.get("data")).toDate());
                            Post p = new Post(m, i.getId());
                            sponsors.add(p);
                        }
                        if (sponsors.size() > 0) {
                            c.onSuccessAdv(sponsors.get((int) (Math.random() * sponsors.size())));//TODO: Controllare
                        }
                    }
                    else c.onFailureAdv(new Exception("No sponsor"));
                });
    }
    @Override
    public void retrievePostsByAuthor(@NonNull String idUser, int page){//TODO: ma invece di mettere l'id, non si potrebbe mettere lo username???
        DocumentReference refUser = db.collection("utenti").document(idUser);
        db.collection("post")
                .whereEqualTo("autore", refUser)
                .orderBy("data")
                .startAfter(page * ELEMENTS_LAZY_LOADING)
                .limit(ELEMENTS_LAZY_LOADING)
                .get()//TODO: gestione immagini
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for (QueryDocumentSnapshot i : task.getResult()) {
                            Map<String, Object> m = i.getData();
                            m.replace("data", ((Timestamp) m.get("data")).toDate());
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
    public void createPost(Post post) {
        Map<String, Object> documentFields = new HashMap<>();
        documentFields.put("autore", post.getAutore());
        documentFields.put("likes", post.getLikes());
        documentFields.put("promozionale", post.isPromozionale());
        documentFields.put("tags", post.getTags());
        documentFields.put("descrizione", post.getDescrizione());
        documentFields.put("data", new Timestamp(DBConverter.dateFromTimestamp(System.currentTimeMillis())));
        db.collection("post")
                .add(documentFields)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        c.onUploadSuccess(task.getResult().getId());
                    else
                        c.onUploadFailure(new Exception("Error creating document"));
                });
    }

    @Override
    public void createPosts(List<Post> pl){//TODO: controllare qua
        for (Post p : pl){
            createPost(p);
        }
    }
    @Override
    public void createImage(Uri imageUri, String document, ContentResolver context, @Deprecated CallbackInterface ci, String id) {
        if (imageUri != null) {
            try {
                // Convert the image to PNG format
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context, imageUri);//maybe deprecated?
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
                            c.onUploadFailure(new Exception("Caricamento fallito"));
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
                            //TODO: gestione immagini
                            /*storage.getReference().child((String) m.get("immagine")).getDownloadUrl().addOnCompleteListener(uri -> {

                            });
                            m.replace("autore", ((DocumentReference) m.get("autore")).getId());

                            m.replace("immagine", )*/
                            m.replace("data", ((Timestamp) m.get("data")).toDate());
                            Post p = new Post(m, i.getId());
                            results.add(p);
                        }
                        c.onSuccessG(results);
                    }
                    else{

                        c.onUploadFailure(task.getException());
                    }
                });
    }
    @Override
    public void retrievePostsWithTagsLL(String[] tags, int page) {//mero segnaposto, è come quello normale
        db.collection("post").get() //TODO: gestione immagini
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
                        c.onUploadFailure(task.getException());
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

    @Override
    public void retrievePostsForSync(int page){
        db.collection("posts")
                .whereEqualTo("autore", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .startAfter(page*ELEMENTS_LAZY_LOADING*5)
                .limit(ELEMENTS_LAZY_LOADING*5)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for(QueryDocumentSnapshot i : task.getResult()){
                            Map<String, Object> m = i.getData(); //TODO: gestione immagini (e fallo!)
                            Post p = new Post(m, i.getId());
                            results.add(p);
                        }
                        c.onSuccessSyncRemote(results);
                    }
                    else{
                        c.onUploadFailure(task.getException());
                    }
                });
    }
}