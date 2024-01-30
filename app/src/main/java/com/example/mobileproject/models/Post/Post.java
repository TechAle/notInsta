package com.example.mobileproject.models.Post;

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
    public String photo;
    public String id;
    public DocumentReference autore;
    public String descrizione;
    public Date pubblicazione;
    public List<String> tags;

    public Post() {

    }

    public Post(Map<String, Object> m, String id) {
        this.descrizione = (String) m.get("descrizione");
        this.pubblicazione = ((Timestamp) m.get("data")).toDate();
        this.autore = (DocumentReference) m.get("creatoreId"); //TODO: qui non credo che vada bene l'id dell'autore, sarebbe più consono il suo username...
        this.tags = (ArrayList<String>) m.get("tag");
        this.photo = "POSTS/" + m.get("immagine");
        this.id = id;
        /* Per luchino che dovrà gestire le immagini
        StorageReference imageRef = storageRef.child(nameImage);
        imageRef.getDownloadUrl().addOnCompleteListener(image -> {
            if (image.isSuccessful()) {
                URL = = image.getResult();
            }
        });*/
    }


}
