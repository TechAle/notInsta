package com.example.mobileproject.dataLayer.sources;

import com.example.mobileproject.models.Post;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
    public FirestoreRemoteSource(){
        db = FirebaseFirestore.getInstance();
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
                            p.pubblicazione = (Date) m.get("data");
                            p.autore = (String) m.get("autoreId"); //TODO: qui non credo che vada bene l'id dell'autore, sarebbe più consono il suo username...
                            p.photo = (URL) m.get("immagine");
                            p.tags = new ArrayList<>(); //TODO: definire i tags (ed inserirli)
                            p.id = (int) m.get("idPost"); //TODO: rivedere gestione degli ID
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
                            p.descrizione = (String) m.get("descrizione");
                            p.pubblicazione = (Date) m.get("data");
                            p.autore = (String) m.get("autoreId"); //TODO: qui non credo che vada bene l'id dell'autore, sarebbe più consono il suo username...
                            p.photo = (URL) m.get("immagine");
                            p.tags = new ArrayList<>(); //TODO: definire i tags (ed inserirli)
                            p.id = (int) m.get("idPost"); //TODO: rivedere gestione degli ID
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
