package com.example.mobileproject.dataLayer.sources;

import android.content.ContentResolver;
import android.net.Uri;

import com.example.mobileproject.models.Post.Post;

import java.util.List;

/**
 * Classe astratta per il datasource relativo ai posts
 */
public abstract class GeneralPostRemoteSource {

    protected CallbackPosts c;

    /**
     * Metodo setter della callback
     * @param call callback da settare
     */
    public void setCallback(CallbackPosts call){
        this.c = call;
    }

    //TODO: javadoc non conclusa
    /**
     * Metodo che prende da remoto un post sponsorizzato.
     * Chiama il metodo di callback
     */
    public abstract void retrievePostsSponsor();

    /**
     * Metodo per prendere da remoto i post di un determinato utente
     * @implNote Utilizza il meccanismo del lazy loading
     */
    public abstract void retrievePostsByAuthor(String idUser, int page);

    /**
     * Metodo per la creazione su sorgente remota di un post
     */
    public abstract void createPost(Post post);
    public abstract void createPosts(List<Post> p);
//    protected abstract void createDocument(String collectionName, Map<String, Object> documentFields, CallbackInterface ci);
    public abstract void createImage(Uri imageUri, String document, ContentResolver contentResolver, CallbackInterface postRepository, String id);

    /**
     * Metodo per prendere tutti i post
     * @implNote Utilizza il meccanismo del lazy loading
     * @param page pagina di caricamento
     */
    public abstract void retrievePostsLL(int page);
    public abstract void retrievePostsWithTagsLL(String[] tags, int page);
/*    public abstract void retrievePostsForSync(Date d);*/
    public abstract void retrieveUserPostsForSync(int page);

    /**
     * Metodo per prendere i post dell'utente pubblicati dopo una certa data
     * @param page pagina di caricamento
     * @param lastUpdate data di ultima sincronizzazione in formato timestamp
     * @implNote Utilizza il meccanismo del lazy loading
     */
    public abstract void retrieveUserPostsForSync(int page, long lastUpdate);
}