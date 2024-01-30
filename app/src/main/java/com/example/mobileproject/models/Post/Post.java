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
    private String photo;
    private String id;
    private DocumentReference autore;
    private String descrizione;
    private Date pubblicazione;
    private List<String> tags;
    private List<DocumentReference> likes;
    private boolean promozionale;

    public Post() {

    }

    public List<String> getTags() {
        return tags;
    }

    public Post(Map<String, Object> m, String id) {
        this.descrizione = (String) m.get("descrizione");
        this.pubblicazione = ((Timestamp) m.get("data")).toDate();
        this.autore = (DocumentReference) m.get("creatoreId"); //TODO: qui non credo che vada bene l'id dell'autore, sarebbe più consono il suo username...
        this.tags = (ArrayList<String>) m.get("tag");
        this.likes = (ArrayList<DocumentReference>) m.get("likes");
        this.photo = "POSTS/" + m.get("immagine");
        this.id = id;
        this.promozionale = (Boolean) m.get("promozionale");
        /* Per luchino che dovrà gestire le immagini
        StorageReference imageRef = storageRef.child(nameImage);
        imageRef.getDownloadUrl().addOnCompleteListener(image -> {
            if (image.isSuccessful()) {
                URL = = image.getResult();
            }
        });*/
    }

    public String getPhoto() {
        return photo;
    }

    public String getId() {
        return id;
    }

    public DocumentReference getAutore() {
        return autore;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Date getPubblicazione() {
        return pubblicazione;
    }

    public List<DocumentReference> getLikes() {
        return likes;
    }

    public boolean isPromozionale() {
        return promozionale;
    }
}
