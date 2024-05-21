package com.example.mobileproject.dataLayer.repositories;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.example.mobileproject.dataLayer.sources.CallbackPosts;
import com.example.mobileproject.dataLayer.sources.GeneralAdvSource;
import com.example.mobileproject.dataLayer.sources.GeneralPostLocalSource;
import com.example.mobileproject.dataLayer.sources.GeneralPostRemoteSource;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.Result;

import java.util.ArrayList;
import java.util.List;

public class PostRepository implements CallbackPosts {
    private static class data_structure{
        Post post;
        Bitmap image;
        int[] loadProgress = new int[]{0,0,0,0};
    }
    private PostResponseCallback c;
    private final MutableLiveData<Result> ready;
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
        ready = new MutableLiveData<>();
        //barrier = new CyclicBarrier(1);
    }

    private void controlComplete(){
        for(int i = 0; i < 4; i++){
            if(d.loadProgress[i] == 0){
                return;
            }
        }
        d.post = null;
        Result res;
        if(d.loadProgress[0] == 1){
            if(d.loadProgress[1] == 1){
                if(d.loadProgress[2] == 1){
                    res = new Result.PostCreationSuccess(Result.PostCreationSuccess.ResponseType.SUCCESS);
                } else {
                    res = new Result.PostCreationSuccess(Result.PostCreationSuccess.ResponseType.REMOTE);
                    //TODO: gestione della non sincronizzazione
                }
            } else {
                if(d.loadProgress[2] == 1){
                    res = new Result.PostCreationSuccess(Result.PostCreationSuccess.ResponseType.NO_REMOTE_IMAGE);
                    //TODO: gestione della non sincronizzazione
                } else {
                    res = new Result.Error("Not created");
                    //TODO: gestione della non sincronizzazione
                }
            }
        }
        else{
            if(d.loadProgress[2] == 1){
                res = new Result.PostCreationSuccess(Result.PostCreationSuccess.ResponseType.LOCAL);
                //TODO: gestione della non sincronizzazione
            }else{
                res = new Result.Error("Not created");
            }
        }
        c.onResponseCreation(res);
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

    /*public MutableLiveData<Result> createPost(Post post) {
        rem.createPost(post);
        return ready;
    }*/

    public void createPost(Post post, Bitmap bmp) {
        if(d.post == null){
            d.post = post;
            d.image = bmp;
            rem.createPost(post);
        }
    }

    //TODO: valutare se serve questa funzione (verrà chiamata da un worker?)
    /*
    /**
     * Metodo che prende i post dell'utente loggato.
     * @param page pagina di caricamento
     *
     * @implNote ATTENZIONE: questa è una chiamata sincrona, non deve essere utilizzata dal thread UI
     * (infatti è chiamata da un worker)
     */
    /*public List<Post> retrieveUserPostSynchronously(int page){
        latch = new CountDownLatch(1);
        rem.retrieveUserPostsForSync(page);
        try{
            latch.await();
        } catch (InterruptedException e){
            return null;
        }
        if(res == null){
            return new ArrayList<>();
        }
        return res;
    }
    public void loadPostsInLocal(List<Post> p){
        loc.insertPosts(p);
    }*/

    /*
    /**
     * Metodo che prende i post dell'utente loggato postati dopo una certa data.
     *
     * @implNote ATTENZIONE: questa è una chiamata sincrona, non deve essere utilizzata dal thread UI
     * (infatti è chiamata da un worker)
     */
    /*public List<Post> retrievePostsForSync(int page, long lastUpdate){
        latch = new CountDownLatch(1);
        rem.retrieveUserPostsForSync(page, lastUpdate);
        try{
            latch.await();
        } catch (InterruptedException e){
            return null;
        }
        if(res == null){
            return new ArrayList<>();
        }
        return res;
    }*/
    /*public List<Post> syncPostsFromLocal(){
        latch = new CountDownLatch(1);
        loc.retrieveNoSyncPosts();
        try{
            latch.await();
        } catch (InterruptedException e){
            return null;
        }
        if(res == null){
            return new ArrayList<>();
        }
        return res;
    }*/
    /*public void substitutePost(Post p1, Post p2){
        //TODO: implementare questa parte
    }*/
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
    public void onSuccess() { //Perchè???
    }

    @Deprecated
    @Override
    public void onUploadFailure(Exception e) {
        Result.Error resultError = new Result.Error(e.getMessage());
        /*
        DataStoreSingleton ds = DataStoreSingleton.getInstance();
        long temp = ds.readLongData("postsNotLoaded");
        p.setId("???" + temp);
        ds.writeLongData("postsNotLoaded", temp+1);
        p.setImage(loc.createImage());
        loc.insertPost(p);
        */
        ready.postValue(resultError);
    }
    @Deprecated
    @Override
    public void onUploadSuccess(String id) {
        Result.PostCreationSuccess result = new Result.PostCreationSuccess(Result.PostCreationSuccess.ResponseType.SUCCESS);
        ready.postValue(result);
    }
    @Override
    public void onUploadImageSuccess(){
        d.loadProgress[1] = 1;
        controlComplete();
    }
    @Override
    public void onUploadImageFailure(){
        d.loadProgress[1] = 2;
        controlComplete();
    }
    @Override
    public void onUploadPostSuccess(String id){
        d.post.setId(id);
        d.loadProgress[0] = 1;
        rem.createImage(id, d.image);
        Uri img = loc.createImage(d.image);
        if (img == null){
            d.loadProgress[2] = 2;
            d.loadProgress[3] = 2;
            controlComplete();
        }
        d.loadProgress[2] = 1;
        d.post.setImage(img);
        loc.insertPost(d.post);
    }
    @Override
    public void onUploadPostFailure(){
        d.loadProgress[0] = 2;
        d.loadProgress[1] = 2;
        d.post.setId("???" + System.currentTimeMillis());//La prima cosa venuta in mente
        Uri img = loc.createImage(d.image);
        if (img == null){
            d.loadProgress[2] = 2;
            d.loadProgress[3] = 2;
            controlComplete();
        }
        d.loadProgress[2] = 1;
        d.post.setImage(img);
        loc.insertPost(d.post);
    }
    @Override
    public void onLocalSaveSuccess(){
        d.loadProgress[3] = 1;
        controlComplete();
    }
    public void onLocalSaveFailure(){
        d.loadProgress[3] = 2;
        controlComplete();
    }

    /*public MutableLiveData<Result> createImage(Uri imageUri, String document, ContentResolver contentResolver, String id) {
        rem.createImage(imageUri, document, contentResolver, this, id);
        return ready;
    }


    @Override
    public void onSuccessSyncRemote(List<Post> pl){
        res = pl;
        latch.countDown();
    }
    @Override
    public void onSuccessSyncLocal(List<Post> pl){
        res = pl;
        latch.countDown();
    }

    @Override
    public void onFailureSync(){
        res = null;
        latch.countDown();
    }
    */
    @Override
    public void onSuccessAdv(Post p) {
        List<Post> pl = new ArrayList<>();
        pl.add(p);
        Result.PostResponseSuccess res = new Result.PostResponseSuccess(new PostResp(pl));
        c.onResponseAdvPost(res);
        //ad.postValue(res);
    }
    @Override
    public void onFailureAdv(Exception e){
        Result.Error resultError = new Result.Error(e.getMessage());
        c.onResponseAdvPost(resultError);
    }
}