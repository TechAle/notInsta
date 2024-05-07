package com.example.mobileproject.dataLayer.repositories;

import android.content.ContentResolver;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class PostRepository implements CallbackPosts {

    private final MutableLiveData<Result> postsG;
    private final MutableLiveData<Result> postsO;
    private final MutableLiveData<Result> postsF;
    private final MutableLiveData<Result> ready;
    private final MutableLiveData<Result> ad;
    private final GeneralPostRemoteSource rem;
    private final GeneralPostLocalSource loc;
    private final GeneralAdvSource ads;

    private List<Post> res;

    /**
     * Latch per permettere la chiamata sincrona da remoto
     */
    private final CyclicBarrier barrier;//TODO: sostituzione latch con barriere
    private CountDownLatch latch;
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
        postsG = new MutableLiveData<>();
        postsO = new MutableLiveData<>();
        postsF = new MutableLiveData<>();
        ready = new MutableLiveData<>();
        ad = new MutableLiveData<>();
        barrier = new CyclicBarrier(1);
    }

    //assegnamento in callback
    /*public MutableLiveData<Result> retrievePosts(){
        rem.retrievePosts();
        return posts;
    }

    //WTF
    public MutableLiveData<Result> retrievePosts(String tag){
        rem.retrievePostByDocumentId(tag);
        return posts;
    }*/

    //TODO: Attenzione a questi due
    public MutableLiveData<Result> retrievePostsbyAuthor(String idUser, int page){
        rem.retrievePostsByAuthor(idUser, page);
        return postsO;
    }
    public MutableLiveData<Result> retrieveUserPosts(String idUser, int page){
        rem.retrievePostsByAuthor(idUser, page);
        return postsO;
    }

    /**
     * Prende alcuni post, preferibilmente non dell'utente stesso. Usa il Lazy loading
     *
     * @param page Numero di pagina
     *
     * @return Un MutableLiveData contenente il risultato. Settato tramite callback
     */
    public MutableLiveData<Result> retrievePostsLL(int page){ //Lazy Loading
        rem.retrievePostsLL(page);
        return postsG;
    }

    /**
     * Prende alcuni post aventi i tags specificati. Utilizza il meccanismo di lazy loading
     *
     * @param tags Lista di tag
     * @param page Numero di pagina
     *
     * @return Un MutableLiveData contenente il risultato. Settato tramite callback
     */
    public MutableLiveData<Result> retrievePostsWithTagsLL(String tags[], int page){ //Lazy Loading
        rem.retrievePostsWithTagsLL(tags, page);
        return postsG;
    }
    /**
     * Prende un solo post sponsorizzato
     *
     * @return Un MutableLiveData contenente il post
     */
    public MutableLiveData<Result> retrieveSponsoredPosts(){
        if ((int) (Math.random() * 3) == 1) {
            ads.getAdvPost();
        } else
            rem.retrievePostsSponsor();
        return ad;
    }

    public MutableLiveData<Result> createPost(Post post) {
        rem.createPost(post);
        return ready;
    }

    /**
     * Metodo che prende i post dell'utente loggato.
     * @param page pagina di caricamento
     *
     * @implNote ATTENZIONE: questa è una chiamata sincrona, non deve essere utilizzata dal thread UI
     * (infatti è chiamata da un worker)
     */
    public List<Post> retrieveUserPostSynchronously(int page){
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
    }

    /**
     * Metodo che prende i post dell'utente loggato postati dopo una certa data.
     *
     * @implNote ATTENZIONE: questa è una chiamata sincrona, non deve essere utilizzata dal thread UI
     * (infatti è chiamata da un worker)
     */
    public List<Post> retrievePostsForSync(int page, long lastUpdate){
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
    }
    public List<Post> syncPostsFromLocal(){
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
    }
    public void substitutePost(Post p1, Post p2){
        //TODO: implementare questa parte
    }
    //Callbacks

    public void onSuccessG(List<Post> res) {/*
        if (posts.getValue() != null && posts.getValue().successful()) { //Lazy Loading
            List<Post> l = ((Result.PostResponseSuccess)posts.getValue()).getData().getPostList();
            l.addAll(res);
            Result.PostResponseSuccess result = new Result.PostResponseSuccess(new PostResp(l));
            posts.postValue(result);
        } else {*/
        Result.PostResponseSuccess result = new Result.PostResponseSuccess(new PostResp(res));
        postsG.postValue(result);
        //}
    }
    public void onSuccessO(List<Post> res) {/*
        if (posts.getValue() != null && posts.getValue().successful()) { //Lazy Loading
            List<Post> l = ((Result.PostResponseSuccess)posts.getValue()).getData().getPostList();
            l.addAll(res);
            Result.PostResponseSuccess result = new Result.PostResponseSuccess(new PostResp(l));
            posts.postValue(result);
        } else {*/
        Result.PostResponseSuccess result = new Result.PostResponseSuccess(new PostResp(res));
        postsO.postValue(result);
        //}
    }
    public void onSuccessF(List<Post> res) {/*
        if (posts.getValue() != null && posts.getValue().successful()) { //Lazy Loading
            List<Post> l = ((Result.PostResponseSuccess)posts.getValue()).getData().getPostList();
            l.addAll(res);
            Result.PostResponseSuccess result = new Result.PostResponseSuccess(new PostResp(l));
            posts.postValue(result);
        } else {*/
        Result.PostResponseSuccess result = new Result.PostResponseSuccess(new PostResp(res));
        postsF.postValue(result);
        //}
    }

    @Override
    public void onFailureG(Exception e) {
        Result.Error resultError = new Result.Error(e.getMessage());
        postsG.postValue(resultError);
    }

    @Override
    public void onFailureO(Exception e) {
        Result.Error resultError = new Result.Error(e.getMessage());
        postsO.postValue(resultError);
    }

    @Override
    public void onFailureF(Exception e) {
        Result.Error resultError = new Result.Error(e.getMessage());
        postsF.postValue(resultError);
    }

    @Override
    public void onSuccess() { //Perchè???
    }

    @Override
    public void onUploadFailure(Exception e) {
        Result.Error resultError = new Result.Error(e.getMessage());
        ready.postValue(resultError);
    }

    @Override
    public void onUploadSuccess(String id) {
        Result.PostCreationSuccess result = new Result.PostCreationSuccess(id);
        ready.postValue(result);
    }

    public MutableLiveData<Result> createImage(Uri imageUri, String document, ContentResolver contentResolver, String id) {
        rem.createImage(imageUri, document, contentResolver, this, id);
        return ready;
    }

    /**
     * Callback per sincronizzazione
     */
    @Override
    public void onSuccessSyncRemote(List<Post> pl){
        res = pl;
        latch.countDown();
    }

    //TODO: duplicato?
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

    @Override
    public void onSuccessAdv(Post p) {
        List<Post> pl = new ArrayList<>();
        pl.add(p);
        Result.PostResponseSuccess res = new Result.PostResponseSuccess(new PostResp(pl));
        ad.postValue(res);
    }
    @Override
    public void onFailureAdv(Exception e){
        Result.Error resultError = new Result.Error(e.getMessage());
        ad.postValue(resultError);
    }
}
