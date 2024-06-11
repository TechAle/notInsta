package com.example.mobileproject.dataLayer.sources;

import com.example.mobileproject.models.Post.Post;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Classe astratta per il datasource relativo ai posts
 */
public abstract class GeneralPostDataRemoteSource {

    protected CallbackPosts c;

    /**
     * Metodo setter della callback
     * @param call callback da settare
     */
    public void setCallback(CallbackPosts call){
        this.c = call;
    }

    /**
     * Metodo che prende da remoto un post sponsorizzato.
     */
    public abstract void retrievePostsSponsor();

    /**
     * Metodo per prendere da remoto i post di un determinato utente
     * @implNote Utilizza il meccanismo del lazy loading
     */
    public abstract void retrievePostsByAuthor(String idUser, int page);

    /**
     * Metodo per la creazione su sorgente remota di un post
     *
     * @return
     */
    public abstract Future<String> createPost(Post post);

    /**
     * Metodo per prendere tutti i post
     * @implNote Utilizza il meccanismo del lazy loading
     * @param page pagina di caricamento
     */
    public abstract void retrievePosts(int page);
    public abstract void retrievePostsWithTagsLL(String[] tags, int page);

    /**
     * Metodo per prendere i post dell'utente pubblicati dopo una certa data
     *
     * @param page       pagina di caricamento
     * @param lastUpdate data di ultima sincronizzazione in formato timestamp
     *
     * @return future dei post
     *
     * @implNote Utilizza il meccanismo del lazy loading
     */
    public abstract Future<List<Post>> retrieveUserPostsForSync(int page, long lastUpdate);
}