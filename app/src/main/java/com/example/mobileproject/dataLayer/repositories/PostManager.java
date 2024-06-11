package com.example.mobileproject.dataLayer.repositories;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.mobileproject.dataLayer.sources.CallbackPosts;
import com.example.mobileproject.dataLayer.sources.GeneralAdvSource;
import com.example.mobileproject.dataLayer.sources.GeneralWorkerSource;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.Result;

import org.jetbrains.annotations.Blocking;

import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.List;

//import java.net.URL;

public final class PostManager implements CallbackPosts {

    //LiveData non presenti("https://developer.android.com/topic/libraries/architecture/livedata#livedata-in-architecture")
    //TODO: maybe a byte[] data type is better than a android.graphics.Bitmap?
    //TODO: maybe a java.net.URL/java.net.URI data type is better than a android.net.Uri?
    private PostResponseCallback c;
    private final PostDataRepository dataRep;
    private final PostImageRepository imageRep;
    private final GeneralAdvSource ads;
    private final GeneralWorkerSource w;

    public PostManager(PostDataRepository data, PostImageRepository images, GeneralWorkerSource w, GeneralAdvSource ads){
        this.dataRep = data;
        this.imageRep = images;
        this.ads = ads;
        this.w = w;
        this.dataRep.setCallback(this);
        this.ads.setCallback(this);
    }

    public void setCallback(PostResponseCallback c){
        this.c = c;
    }

    /**
     * Prende tutti i post di un determinato utente
     *
     * @param idUser ID dell'utente di cui si vogliono recuperare i post
     * @param page   numero di pagina
     * */
    public void retrievePostsbyAuthor(String idUser, int page){
        dataRep.retrievePostsByAuthor(idUser, page);
    }

    /**
     * Prende tutti i post in locale
     *
     * @param page pagina
     * */
    public void retrieveUserPosts(int page){
        dataRep.retrieveLocalPosts(page);
    }

    /**
     * Prende alcuni post, preferibilmente non dell'utente stesso. Usa il Lazy loading
     *
     * @param page Numero di pagina
     */
    public void retrievePosts(int page){ //Lazy Loading
        dataRep.retrievePosts(page);
    }

    /**
     * Prende alcuni post aventi i tags specificati. Utilizza il meccanismo di lazy loading
     *
     * @param tags Lista di tag
     * @param page Numero di pagina
     */
    public void retrievePostsWithTagsLL(String[] tags, int page){
        dataRep.retrievePostsWithTags(tags, page);
    }

    /**
     * Prende un solo post sponsorizzato
     */
    //TODO: controllare, mi sa che ho sbagliato qualcosa...
    public void retrieveSponsoredPosts(){
        if ((int) (Math.random() * 3) == 1) {
            ads.getAdvPost();
        } else dataRep.retrievePostsSponsor();
    }

    /**
     * Crea un post in locale e accoda la scrittura in remoto. Utilizza callbacks
     *
     * @param post dati del post
     * @param bmp  immagine
     * */
    public void createPost(Post post, Bitmap bmp) {
        post.setId("???" + System.currentTimeMillis());
        post.setPubblicazione(null);
        Uri img = imageRep.createImage(bmp, post.getId());
        if(img == null){
            c.onResponseCreation(new Result.Error(""));
        }
        post.setImage(img);
        dataRep.insertPost(post);
    }

    /**
     * Inizia a mandare in esecuzione la sincronizzazione da remoto
     * */
    public void scheduleSync(){
        w.enqueueRemoteRead();
    }
    /**
     * Sincronizza i post da locale. Operazione bloccante
     *
     * @return successo nel sincronizzare tutti i post
     * */
    @Blocking
    public boolean syncPostsFromLocal(){
        List<Post> pl = dataRep.syncDataFromLocal();
        if(pl != null) {
            List<Post> img = imageRep.syncImagesFromLocal(pl);
            for(Post p : img){
                dataRep.updateLocal(p);
            }
            return true;
        }
        else return false;
    }
    /**
     * Sincronizza tutti i post da remoto a partire da un dato momento. Operazione bloccante
     *
     * @param lastUpdate Marca temporale da cui partire per prendere i post
     *
     * @return L'ultima marca temporale salvata nel DB
     * */
    //TODO: le immagini non scaricate per qualunque motivo chi le scarica?
    @Blocking
    public long syncPostsFromRemote(long lastUpdate) throws ExecutionException, InterruptedException {
        long result = dataRep.syncDataFromRemote(lastUpdate);
        List<String> ids = dataRep.getPartialSyncId();
        List<Uri> newImgs = imageRep.syncImagesFromRemote(ids);
        assert(ids.size() == newImgs.size());
        for(int i = 0; i < ids.size(); i++){
            dataRep.updateImage(ids.get(i), newImgs.get(i));
        }
        return result;
    }

    /**
     * Cancella tutti i dati in locale
     * */
    public void deleteData() {
        dataRep.deleteLocal();
        imageRep.deleteLocal();
    }
    /**
     * Sincronizzazione immagini
     *
     * @return se tutte le immagini sono state sincronizzate
     * */
    @Blocking
    public boolean syncImages(){
        List<Uri> img = imageRep.syncImagesFromRemote(dataRep.getPartialSyncId());
        for(Uri u : img){
            if(u == null)
                return false;
        }
        return true;
    }

    //Callbacks
    @Override
    public void onSuccessG(List<Post> res) {
        Result.PostResponseSuccess result = new Result.PostResponseSuccess(new PostResp(res));
        c.onResponseGlobalPost(result);
    }
    @Override
    public void onSuccessO(List<Post> res) {
        Result.PostResponseSuccess result = new Result.PostResponseSuccess(new PostResp(res));
        c.onResponseOwnedPosts(result);
    }
    @Override
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
    public void onLocalSaveSuccess(){//prima callback di creazione
        w.enqueueRemoteWrite();
        c.onResponseCreation(new Result.UserEditSuccess());
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