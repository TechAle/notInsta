package com.example.mobileproject.models.Users;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//@Entity
public class Users {

    String cognome, nome, username, descrizione, id;
    Date dataNascita;
    ArrayList<DocumentReference> followers, following;
    ArrayList<String> tags;

    public Users() {}

    public Users(String cognome, String nome, String username, String descrizione, Date dataNascita, ArrayList<String> tags) {
        this.cognome = cognome;
        this.nome = nome;
        this.username = username;
        this.descrizione = descrizione;
        this.dataNascita = dataNascita;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.tags = tags;
    }

    public Users(Map<String, Object> m, String id) {
        this.descrizione = (String) m.get("descrizione");
        this.cognome = (String) m.get("cognome");
        this.nome = (String) m.get("nome");
        this.username = (String) m.get("username");
        this.following = (ArrayList<DocumentReference>) m.get("following");
        this.followers = (ArrayList<DocumentReference>) m.get("followers");
        this.tags = (ArrayList<String>) m.get("tags");
        this.dataNascita = ((Timestamp) m.get("dataNascita")).toDate();
        this.id = id;
    }

    public String getCognome() {
        return cognome;
    }

    public String getNome() {
        return nome;
    }

    public String getUsername() {
        return username;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getId() {
        return id;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public ArrayList<DocumentReference> getFollowers() {
        return followers;
    }

    public ArrayList<DocumentReference> getFollowing() {
        return following;
    }

    public ArrayList<String> getTags() {
        return tags;
    }
}
