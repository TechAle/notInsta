package com.example.mobileproject.models;

import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;

import java.net.URL;
import java.util.Date;
import java.util.List;

//@Entity
public class Post {
    public Uri photo;
    public Long id;
    public DocumentReference autore;
    public String descrizione;
    public Date pubblicazione;
    public List<String> tags;
    public boolean isReady = false;


}
