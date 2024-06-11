package com.example.mobileproject.models.Post;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.mobileproject.utils.DBConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity (tableName = "posts")
@TypeConverters(DBConverter.class)

/**
 * Classe che rappresenta i posts
 */
public final class Post implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id;
    private String autore; //null: immagine non caricata
    private String descrizione;
    private Date pubblicazione; // null: mai caricato
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
    public Post(@NonNull String id, String autore, String descrizione, Date pubblicazione, List<String> tags, boolean promozionale, Uri imageURI) {
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
    public Post(Map<String, Object> m, @NonNull String id) {
        this.descrizione = (String) m.get("descrizione");
        this.pubblicazione = (Date) m.get("data");
        this.autore = (String) m.get("autore"); //TODO: qui non credo che vada bene l'id dell'autore, sarebbe più consono il suo username...
        this.tags = (ArrayList<String>) m.get("tags");
        this.likes = (ArrayList<String>) m.get("likes");
        this.id = id;
        this.promozionale = (Boolean) m.get("promozionale");
        this.image = (Uri) m.get("immagine");
    }

    private Post(Parcel in) {
        id = in.readString();
        autore = in.readString();
        descrizione = in.readString();
        tags = in.createStringArrayList();
        likes = in.createStringArrayList();
        image = in.readParcelable(Uri.class.getClassLoader());
        pubblicazione = new Date(in.readLong());
        promozionale = in.readByte() != 0;
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
    public List<String> getTags() {
        return tags == null ? null : new ArrayList<>(tags);
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
        return likes == null ? null : new ArrayList<>(likes);
    }
    public boolean isPromozionale() {
        return promozionale;
    }
    public Date getData() {
        return this.pubblicazione;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(autore);
        dest.writeString(descrizione);
        dest.writeStringList(tags);
        dest.writeStringList(likes);
        dest.writeParcelable(image, flags);
        dest.writeLong(pubblicazione.getTime());
        dest.writeByte((byte) (promozionale ? 1 : 0));
    }
}