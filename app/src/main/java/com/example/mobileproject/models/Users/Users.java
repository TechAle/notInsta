package com.example.mobileproject.models.Users;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public final class Users {

    String email, cognome, nome, username, descrizione, id;
    Date dataNascita;
    ArrayList<String> followers, following;
    ArrayList<String> tags;
    URI image;

    public Users() {
    }

    public Users(String id, String email) {
        this.id = id;
        this.email = email;
        this.cognome = null;
        this.nome = null;
        this.username = id;
        this.descrizione = null;
        this.dataNascita = new Date();
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.tags = new ArrayList<String>();
    }

    public Users(String email, String cognome, String nome, String username, String descrizione, Date dataNascita, ArrayList<String> tags) {
        this.email = email;
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
        this.email = (String) m.get("email");
        this.descrizione = (String) m.get("descrizione");
        this.cognome = (String) m.get("cognome");
        this.nome = (String) m.get("nome");
        this.username = (String) m.get("username");
        this.following = (ArrayList<String>) m.get("following");
        this.followers = (ArrayList<String>) m.get("followers");
        this.tags = (ArrayList<String>) m.get("tags");
        this.id = id;
        try{
            this.dataNascita = ((Timestamp) m.get("dataNascita")).toDate();
        } catch (NullPointerException e) {
            this.dataNascita = null;
        }
    }

    public Users getUser() {
        return this;
    }

    public String getEmail() {
        return email;
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

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public URI getImageUri(){
        return image;
    }
}
