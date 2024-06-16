package com.example.mobileproject.dataLayer.repositories;

import android.net.Uri;

import com.example.mobileproject.dataLayer.sources.CallbackPosts;
import com.example.mobileproject.dataLayer.sources.GeneralPostDataLocalSource;
import com.example.mobileproject.dataLayer.sources.GeneralPostDataRemoteSource;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.DBConverter;

import java.util.List;
import java.util.concurrent.ExecutionException;

public final class PostDataRepository implements CallbackPosts {
    private final GeneralPostDataRemoteSource rem;
    private final GeneralPostDataLocalSource loc;
    private CallbackPosts c;
    public void setCallback(CallbackPosts c){
        this.c = c;
    }

    public PostDataRepository(GeneralPostDataRemoteSource rem, GeneralPostDataLocalSource loc){
        this.rem = rem;
        this.loc = loc;
        this.rem.setCallback(this);
        this.loc.setCallback(this);
    }
    /**
     * Sincronizza i dati partendo dal db locale
     *
     * @return Tutti i post che hanno subito le modifiche, con i nuovi valori
     * */
    public List<Post> syncDataFromLocal(){
        List<Post> pl;
        try{
            pl = loc.retrieveNoSyncPosts().get();
        } catch (ExecutionException | InterruptedException e){
            return null;
        }
        for(Post p : pl){
            long now = System.currentTimeMillis();
            p.setPubblicazione(DBConverter.dateFromTimestamp(now));
            String newId;
            try{
                newId = rem.createPost(p).get();
            } catch(InterruptedException | ExecutionException e){
                continue;
            }
            if (newId == null){
                continue;
            }
            loc.modifyId(p.getId(),newId);
            p.setId(newId);
        }
        return pl;
    }

    /**
     * Sincronizza i dati in locale
     *
     * @return il momento della pubblicazione dell'ultimo post ricevuto, oppure quello attuale
     * */
    public long syncDataFromRemote(long lastUpdate){
        int page = 0;
        long time = lastUpdate;
        long now = System.currentTimeMillis();
        try{
            List<Post> pl = rem.retrieveUserPostsForSync(page, lastUpdate).get();
            while(pl.size() != 0){
                for(Post p : pl){
                    p.setImage(null);
                }
                loc.insertPosts(pl);
                time = pl.get(pl.size()-1).getData().getTime();
                page++;
                pl = rem.retrieveUserPostsForSync(page, lastUpdate).get();
            }
            return Math.max(now, time);
        } catch (ExecutionException | InterruptedException e) {
            return time;
        }

    }

    /**
     * Cerca tutti gli ID dei post con immagine non sincronizzata
     *
     * @return Una lista di stringhe rappresentanti gli ID
     * */
    public List<String> getPartialSyncId(){
        List<String> result;
        try {
            result = loc.retrieveIDsWithNoImage().get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
        return result;
    }

    public void deleteLocal(){
        loc.deletePosts();
    }

    public void updateImage(String id, Uri newImg){
        loc.modifyImage(id, newImg);
    }

    public void retrievePostsByAuthor(String idUser, int page) {
        rem.retrievePostsByAuthor(idUser, page);
    }

    public void retrieveLocalPosts(int page) {
        loc.retrievePosts(page);
    }

    public void retrievePosts(int page) {
        rem.retrievePosts(page);
    }

    public void retrievePostsWithTags(String[] tags, int page) {
        rem.retrievePostsWithTagsLL(tags, page);
    }

    public void retrievePostsSponsor() {
        rem.retrievePostsSponsor();
    }

    public void insertPost(Post post) {
        loc.insertPost(post);
    }
    public void updateLocal(Post p) {
        loc.updatePost(p);
    }
    @Override
    public void onSuccessG(List<Post> res) {
        c.onSuccessG(res);
    }
    @Override
    public void onSuccessO(List<Post> res) {
        c.onSuccessO(res);
    }
    @Override
    public void onSuccessF(List<Post> res) {
        c.onSuccessF(res);
    }
    @Override
    public void onFailureG(Exception e) {
        c.onFailureG(e);
    }
    @Override
    public void onFailureO(Exception e) {
        c.onFailureO(e);
    }
    @Override
    public void onFailureF(Exception e) {
        c.onFailureF(e);
    }
    @Override
    public void onLocalSaveSuccess(){
        c.onLocalSaveSuccess();
    }
    @Override
    public void onSuccessAdv(Post p) {
        c.onSuccessAdv(p);
    }
    @Override
    public void onFailureAdv(Exception e){
        c.onFailureAdv(e);
    }
}
