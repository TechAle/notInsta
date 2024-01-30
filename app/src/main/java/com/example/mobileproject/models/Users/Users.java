package com.example.mobileproject.models.Users;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//@Entity
public class Users {

    String cognome, nome, username, descrizione;
    Date dataNascita;
    ArrayList<String> followers, following;

    public Users() {}

    public Users(Map<String, Object> m) {
        this.descrizione = (String) m.get("descrizione");
        this.cognome = (String) m.get("cognome");
        this.nome = (String) m.get("nome");
        this.username = (String) m.get("username");
        this.following = (ArrayList<String>) m.get("following");
        this.followers = (ArrayList<String>) m.get("followers");
        this.dataNascita = ((Timestamp) m.get("dataNascita")).toDate();
    }


}
