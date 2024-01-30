package com.example.mobileproject.dataLayer.sources;

import android.net.Uri;

import com.example.mobileproject.models.Post;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
    public void retrievePosts(){
        db.collection("post").orderBy("data").get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for(QueryDocumentSnapshot i : task.getResult()){
                            Post p = new Post();
                            Map<String, Object> m = i.getData();
                            p.descrizione = (String) m.get("descrizione");
                            p.pubblicazione = ((Timestamp) m.get("data")).toDate();
                            p.autore = (DocumentReference) m.get("creatoreId"); //TODO: qui non credo che vada bene l'id dell'autore, sarebbe più consono il suo username...
                            p.id = (Long) m.get("idPost"); //TODO: rivedere gestione degli ID
                            p.tags = (ArrayList<String>) m.get("tag");
                            String nameImage = "POSTS/" + m.get("immagine");
                            StorageReference imageRef = storageRef.child(nameImage);
                            imageRef.getDownloadUrl().addOnCompleteListener(image -> {
                                if (image.isSuccessful()) {
                                    p.photo = image.getResult();
                                } else {
                                    p.photo = null;
                                }
                               p.isReady = true;
                            });
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
    public void retrievePosts(String tag){
        db.collection("post").whereEqualTo("tag", tag).orderBy("data")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for(QueryDocumentSnapshot i : task.getResult()){
                            Post p = new Post();
                            Map<String, Object> m = i.getData();
                            p.id = (Long) m.get("idPost"); //TODO: rivedere gestione degli ID
                        }
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
