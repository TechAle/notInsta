package com.example.mobileproject.models.Post;

import android.net.Uri;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

//@Entity
public class Post {
    private String id;
    private DocumentReference autore;
    private String descrizione;
    private Date pubblicazione;
    private List<String> tags;
    private List<DocumentReference> likes;
    private String image;
    private boolean promozionale;

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // TODO make for null better management
    public String getImage() {
        return image;
    }

    public Post() {

    }

    public Post(String autore, String descrizione, List<String> tags, boolean promozionale, FirebaseFirestore db) {
        this.autore = db.collection("utenti").document(autore);
        this.descrizione = descrizione;
        this.pubblicazione = Calendar.getInstance().getTime();
        this.tags = tags;
        this.likes = new ArrayList<>();
        this.promozionale = promozionale;
    }

    public void setId(String id) {
        this.id = id;
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
        this.id = id;
        this.promozionale = (Boolean) m.get("promozionale");
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

    public boolean getPromozionale() {
        return this.promozionale;
    }

    public Date getData() {
        return this.pubblicazione;
    }
}
