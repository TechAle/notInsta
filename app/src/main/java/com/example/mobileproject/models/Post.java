package com.example.mobileproject.models;

import android.net.Uri;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//@Entity
public class Post {
    public Uri photo;
    public Long id;
    public DocumentReference autore;
    public String descrizione;
    public Date pubblicazione;
    public List<String> tags;
    public boolean isReady = false;

    public Post() {

    }

    public Post(Map<String, Object> m, StorageReference storageRef) {
        this.descrizione = (String) m.get("descrizione");
        this.pubblicazione = ((Timestamp) m.get("data")).toDate();
        this.autore = (DocumentReference) m.get("creatoreId"); //TODO: qui non credo che vada bene l'id dell'autore, sarebbe più consono il suo username...
        this.id = (Long) m.get("idPost"); //TODO: rivedere gestione degli ID
        this.tags = (ArrayList<String>) m.get("tag");
        String nameImage = "POSTS/" + m.get("immagine");
        StorageReference imageRef = storageRef.child(nameImage);
        imageRef.getDownloadUrl().addOnCompleteListener(image -> {
            if (image.isSuccessful()) {
                this.photo = image.getResult();
            } else {
                this.photo = null;
            }
            this.isReady = true;
        });
    }


}
