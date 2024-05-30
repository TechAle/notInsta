package com.example.mobileproject.models.Post;

import android.net.Uri;

import com.example.mobileproject.utils.DBConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.Contract;

@Entity (tableName = "posts")
@TypeConverters(DBConverter.class)

/**
 * Classe che rappresenta i posts
 */
public final class Post {
    @PrimaryKey
    @NonNull
    private String id;
    private String autore;
    private String descrizione;
    private Date pubblicazione;
    private List<String> tags;
    private List<String> likes;
    private Uri image;
    private boolean promozionale;

    //Costruttori
    public Post(){}
    /**
     * Copy constructor
     * */
    public Post(Post p){
        this.id = p.id;
        this.autore = p.autore;
        this.descrizione = p.descrizione;
        this.pubblicazione = p.pubblicazione;
        this.tags = p.tags;
        this.likes = p.likes;
        this.promozionale = p.promozionale;
        this.image = p.image;
    }
    public Post(String id, String autore, String descrizione, Date pubblicazione, List<String> tags, boolean promozionale, Uri imageURI) {
        this.id = id;
        this.autore = autore;
        this.descrizione = descrizione;
        this.pubblicazione = pubblicazione;
        this.tags = tags;
        this.likes = new ArrayList<>();
        this.promozionale = promozionale;
        this.image = imageURI;
    }
    public Post(String autore, String descrizione, @Deprecated Date pubblicazione, List<String> tags, boolean promo) {
        this.id = "???";
        this.autore = autore;
        this.descrizione = descrizione;
        this.pubblicazione = null;
        this.tags = tags;
        this.likes = new ArrayList<>();
        this.promozionale = promo;
    }
    public Post(Map<String, Object> m, String id) {
        this.descrizione = (String) m.get("descrizione");
        this.pubblicazione = (Date) m.get("data");
        this.autore = (String) m.get("creatoreId"); //TODO: qui non credo che vada bene l'id dell'autore, sarebbe più consono il suo username...
        this.tags = (ArrayList<String>) m.get("tag");
        this.likes = (ArrayList<String>) m.get("likes");
        this.id = id;
        this.promozionale = (Boolean) m.get("promozionale");
        this.image = (Uri) m.get("immagine");
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }
    public void setPubblicazione(Date pubblicazione) {
        this.pubblicazione = pubblicazione; //assente il copy costructor
    }
    public void setTags(List<String> tags) {
        this.tags = new ArrayList<>(tags);
    }
    public void setLikes(List<String> likes) {
        this.likes = new ArrayList<>(likes);
    }
    public void setPromozionale(boolean promozionale) {
        this.promozionale = promozionale;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public void setImage(Uri image) {
        this.image = image;
    }
    // TODO make for null better management
    public Uri getImage() {
        return image;
    }
    public void setId(String id) {
        this.id = id;
    }
    @NonNull
    @Contract(" -> new")
    public List<String> getTags() {
        return new ArrayList<>(tags);
    }
    public String getId() {
        return id;
    }
    public String getAutore() {
        return autore;
    }
    public String getDescrizione() {
        return descrizione;
    }
    public Date getPubblicazione() {
        return pubblicazione;
    }
    @NonNull
    @Contract(" -> new")
    public List<String> getLikes() {
        return new ArrayList<>(likes);
    }
    public boolean isPromozionale() {
        return promozionale;
    }
    public Date getData() {
        return this.pubblicazione;
    }

}