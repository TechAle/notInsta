package com.example.mobileproject.models;

import java.net.URL;
import java.util.Date;
import java.util.List;

//@Entity
public class Post {
    public URL photo;
    public int id;
    public String autore;
    public String descrizione;
    public Date pubblicazione;
    public List<String> tags;

    public URL getPhoto() {
        return photo;
    }

}
