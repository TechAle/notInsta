package com.example.mobileproject.models.Post;

import android.net.Uri;

import com.example.mobileproject.utils.DateConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

/*@Entity (tableName = "posts")
@TypeConverters(DateConverter.class)*/
public class Post {
/*    @PrimaryKey
    @NonNull*/
    private String id;
    private String autore;
    private String descrizione;
    private Date pubblicazione;
    private List<String> tags;
    private List<String> likes;
    private String image;
    private boolean promozionale;

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public void setPubblicazione(Date pubblicazione) {
        this.pubblicazione = pubblicazione;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public void setPromozionale(boolean promozionale) {
        this.promozionale = promozionale;
    }

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

    public Post(String id, String autore, String descrizione, Date pubblicazione, List<String> tags, boolean promozionale, String imageURI) {
        this.id = id; //TODO: come segnaposto pensavo di mettere un qualcosa del tipo "???1234"
        this.autore = autore;
        this.descrizione = descrizione;
        this.pubblicazione = pubblicazione;
        this.tags = tags;
        this.likes = new ArrayList<>();
        this.promozionale = promozionale;
        this.image = imageURI;
    }

    public Post(String autore, String descrizione, Date pubblicazione, List<String> tags, boolean promo) {
        this.id = "???";
        this.autore = autore;
        this.descrizione = descrizione;
        this.pubblicazione = null;
        this.tags = tags;
        this.likes = new ArrayList<>();
        this.promozionale = promo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public Post(Map<String, Object> m, String id) {
        this.descrizione = (String) m.get("descrizione");
        this.pubblicazione = (Date) m.get("data");
        this.autore = (String) m.get("creatoreId"); //TODO: qui non credo che vada bene l'id dell'autore, sarebbe più consono il suo username...
        this.tags = (ArrayList<String>) m.get("tag");
        this.likes = (ArrayList<String>) m.get("likes");
        this.id = id;
        this.promozionale = (Boolean) m.get("promozionale");
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

    public List<String> getLikes() {
        return likes;
    }

    public boolean isPromozionale() {
        return promozionale;
    }

    public Date getData() {
        return this.pubblicazione;
    }

}
//Calendar.getInstance().getTime()