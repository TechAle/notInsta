package com.example.mobileproject.models;

import com.example.mobileproject.models.Post.Post;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

import android.net.Uri;

public class PostUnitTest {

    private boolean equal_post(Post p1, Post p2){
        return  p1.getAutore().equals(p2.getAutore())
                //&& p1.getImage().equals(p2.getImage())
                && p1.getId().equals(p2.getId())
                && p1.getData().equals(p2.getData())
                && p1.getDescrizione().equals(p2.getDescrizione())
                && p1.getPubblicazione().equals(p2.getPubblicazione())
                && p1.getLikes().equals(p2.getLikes())
                && p1.getTags().equals(p2.getTags());
    }
    @Test
    public void constructor_equivalence_test(){
        String id = "prova";
        String descrizione = "dimostrazione di correttezza";
        String autore = "Il Programmatore";
        Date d = new Date();
        //Uri link = Uri.parse("qwer://ty.uiop.dummy");
        List<String> likes = new ArrayList<>();
        likes.add("Mi piace");
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");
        boolean promo = false;
        Post p1 = new Post();
        p1.setId(id);
        p1.setLikes(likes);
        p1.setImage(null);
        p1.setPubblicazione(d);
        p1.setDescrizione(descrizione);
        p1.setAutore(autore);
        p1.setPromozionale(promo);
        p1.setTags(tags);
        Post p2 = new Post(p1);
        Post p3 = new Post(id, autore, descrizione, d, tags, promo, null);
        p3.setLikes(likes);
        assertTrue(equal_post(p1, p2));
        assertTrue(equal_post(p2, p3));
        promo = true;
        assertFalse(p1.isPromozionale());
        assertFalse(p2.isPromozionale());
        assertFalse(p3.isPromozionale());
        tags.add("Unexpected");
        assertEquals(3, p1.getTags().size());
        assertEquals(3, p2.getTags().size());
        assertEquals(3, p3.getTags().size());
        likes.clear();
        assertEquals(1, p1.getLikes().size());
        assertEquals(1, p2.getLikes().size());
        assertEquals(1, p3.getLikes().size());
        //Stringhe immutabili
        //List<String> likes2 = new ArrayList<>();
        //likes2.add("Non mi piace");

    }
}
