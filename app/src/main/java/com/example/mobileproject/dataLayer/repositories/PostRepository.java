package com.example.mobileproject.dataLayer.repositories;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.mobileproject.dataLayer.sources.CallbackPosts;
import com.example.mobileproject.dataLayer.sources.GeneralAdvSource;
import com.example.mobileproject.dataLayer.sources.GeneralPostLocalSource;
import com.example.mobileproject.dataLayer.sources.GeneralPostRemoteSource;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.Result;

import java.util.ArrayList;
import java.util.List;
//import java.net.URL;

public final class PostRepository implements CallbackPosts {

    //LiveData non presenti("https://developer.android.com/topic/libraries/architecture/livedata#livedata-in-architecture")
    //TODO: maybe a byte[] data type is better than a android.graphics.Bitmap?
    //TODO: maybe a java.net.URL/java.net.URI data type is better than a android.net.Uri?
    private static class data_structure{
        enum ResultType{
            NOT_AVAILABLE,
            SUCCESS,
            FAILURE
        }
        private Post post;
        private Bitmap image;
        private ResultType remote;
        private ResultType local;
        boolean isBusy(){
            return post==null;
        }
        void setPost(Post post) {
            this.post = post;
        }
        public void setRemote(ResultType remote) {
            this.remote = remote;
        }
        public void setLocal(ResultType local) {
            this.local = local;
        }
        boolean loadComplete(){
            if(post == null) throw new RuntimeException();
            if(local == ResultType.NOT_AVAILABLE || remote == ResultType.NOT_AVAILABLE) return false;
            return true;
        }
        Result getResponse(){
            if(post == null) throw new RuntimeException();
            Result res;
            switch(remote){
                case SUCCESS:
                    switch(local){
                        case SUCCESS:
                            res = new Result.PostCreationSuccess(Result.PostCreationSuccess.ResponseType.SUCCESS);
                            break;
                        case FAILURE:
                            res = new Result.PostCreationSuccess(Result.PostCreationSuccess.ResponseType.REMOTE);
                            break;
                        default:
                            throw new RuntimeException();
                    }
                    break;
                case FAILURE:
                    switch(local){
                        case SUCCESS:
                            res = new Result.PostCreationSuccess(Result.PostCreationSuccess.ResponseType.LOCAL);
                            break;
                        case FAILURE:
                            res = new Result.Error("Post not loaded");
                            break;
                        default:
                            throw new RuntimeException();
                    }
                    break;
                default:
                    throw new RuntimeException();
            }
            return res;
        }
    }
    private PostResponseCallback c;
    private final GeneralPostRemoteSource rem;
    private final GeneralPostLocalSource loc;
    private final GeneralAdvSource ads;
    private final data_structure d;

    /**
     * Costruttore
     */
    public PostRepository(GeneralPostRemoteSource rem, GeneralPostLocalSource loc, GeneralAdvSource ads){
        this.rem = rem;
        this.loc = loc;
        this.ads = ads;
        this.rem.setCallback(this);
        this.loc.setCallback(this);
        this.ads.setCallback(this);
        this.d = new data_structure();
    }

    public void setCallback(PostResponseCallback c){
        this.c = c;
    }
    /*
    //WTF
    public MutableLiveData<Result> retrievePosts(String tag){
        rem.retrievePostByDocumentId(tag);
        return posts;
    }*/

    public void retrievePostsbyAuthor(String idUser, int page){
        rem.retrievePostsByAuthor(idUser, page);
    }
    public void retrieveUserPosts(int page){
        loc.retrievePosts(page);
    }

    /**
     * Prende alcuni post, preferibilmente non dell'utente stesso. Usa il Lazy loading
     *
     * @param page Numero di pagina
     */
    public void retrievePosts(int page){ //Lazy Loading
        rem.retrievePosts(page);
    }

    /**
     * Prende alcuni post aventi i tags specificati. Utilizza il meccanismo di lazy loading
     *
     * @param tags Lista di tag
     * @param page Numero di pagina
     */
    public void retrievePostsWithTagsLL(String tags[], int page){
        rem.retrievePostsWithTagsLL(tags, page);
    }
    /**
     * Prende un solo post sponsorizzato
     */
    public void retrieveSponsoredPosts(){
        if ((int) (Math.random() * 3) == 1) {
            ads.getAdvPost();
        } else rem.retrievePostsSponsor();
    }
    public void createPost(Post post, Bitmap bmp) {
        if(d.isBusy()){
            c.onResponseCreation(new Result.Error("Busy"));
        } else {
            d.post = post;
            d.image = bmp;
            rem.createPost(post);
        }
    }
    public void getNoSyncPostsFromLocal(){
        loc.retrieveNoSyncPosts();
    }
    public void getNoSyncPostsFromRemote(int page, long lastUpdate){
        rem.retrieveUserPostsForSync(page, lastUpdate);
    }
    public void putPosts(List<Post> pl){
        loc.insertPosts(pl);
    }
    public void substitutePost(Post oldPost, Post newPost){
        //TODO: implementare questa parte
        c.onResponseAdvPost(new Result.Error(oldPost.getId()));//Anche questo nome non ha senso...
    }
    public void loadPost(Post p) {
        //rem.createPost(p, <recupero l'immagine da locale>); //TODO: recuperare immagine da locale
    }
    public void deleteData() {
        loc.deletePosts();
        //loc.deleteImages(); //TODO: implementare
    }
    //Callbacks

    public void onSuccessG(List<Post> res) {
        Result.PostResponseSuccess result = new Result.PostResponseSuccess(new PostResp(res));
        c.onResponseGlobalPost(result);
    }
    public void onSuccessO(List<Post> res) {
        Result.PostResponseSuccess result = new Result.PostResponseSuccess(new PostResp(res));
        c.onResponseOwnedPosts(result);
    }
    public void onSuccessF(List<Post> res) {
        Result.PostResponseSuccess result = new Result.PostResponseSuccess(new PostResp(res));
        c.onResponseFoundPosts(result);
    }
    @Override
    public void onFailureG(Exception e) {
        Result.Error resultError = new Result.Error(e.getMessage());
        c.onResponseGlobalPost(resultError);
    }
    @Override
    public void onFailureO(Exception e) {
        Result.Error resultError = new Result.Error(e.getMessage());
        c.onResponseOwnedPosts(resultError);
    }
    @Override
    public void onFailureF(Exception e) {
        Result.Error resultError = new Result.Error(e.getMessage());
        c.onResponseFoundPosts(resultError);
    }
    @Override
    public void onUploadImageSuccess(){
        d.setRemote(data_structure.ResultType.SUCCESS);
        if(d.loadComplete()) c.onResponseCreation(d.getResponse());
    }
    @Override
    public void onUploadImageFailure(){
        d.setRemote(data_structure.ResultType.FAILURE);
        //TODO: gestione non sincronizzazione
        if(d.loadComplete()) c.onResponseCreation(d.getResponse());
    }
    @Override
    public void onUploadPostSuccess(String id){
        if (d.post == null){
            c.onResponseCreation(new Result.UserCreationSuccess(id));
        } else {
            d.post.setId(id);
            rem.createImage(id, d.image);
            Uri img = loc.createImage(d.image);
            if (img == null) {
                d.setLocal(data_structure.ResultType.FAILURE);
                if (d.loadComplete()) c.onResponseCreation(d.getResponse());
                return;
            }
            d.post.setImage(img);
            loc.insertPost(d.post);
        }
    }
    @Override
    public void onUploadPostFailure(){
        d.setRemote(data_structure.ResultType.FAILURE);
        d.post.setId("???" + System.currentTimeMillis());//La prima cosa venuta in mente
        Uri img = loc.createImage(d.image);
        if (img == null){
            c.onResponseCreation(new Result.Error("Not created"));
            d.setPost(null);
            return;
        }
        d.post.setImage(img);
        loc.insertPost(d.post);
    }
    @Override
    public void onLocalSaveSuccess(){
        if(d.post == null){//Ovvero: è chiamato da un worker
            c.onResponseCreation(new Result.UserEditSuccess());//Il nome non ci azzecca niente, però è quella più leggera...
            return;
        }
        d.setLocal(data_structure.ResultType.SUCCESS);
        if(d.loadComplete()) c.onResponseCreation(d.getResponse());
    }
    @Override
    public void onLocalSaveFailure(){
        d.setLocal(data_structure.ResultType.SUCCESS);
        //TODO (forse): gestione non sincronizzazione
        if(d.loadComplete()) c.onResponseCreation(d.getResponse());
    }
    @Override
    public void onSuccessAdv(Post p) {
        List<Post> pl = new ArrayList<>();
        pl.add(p);
        Result.PostResponseSuccess res = new Result.PostResponseSuccess(new PostResp(pl));
        c.onResponseAdvPost(res);
    }
    @Override
    public void onFailureAdv(Exception e){
        Result.Error resultError = new Result.Error(e.getMessage());
        c.onResponseAdvPost(resultError);
    }
}