package com.example.mobileproject.dataLayer.repositories;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.mobileproject.dataLayer.sources.CallbackPosts;
import com.example.mobileproject.dataLayer.sources.GeneralAdvSource;
import com.example.mobileproject.dataLayer.sources.GeneralPostLocalSource;
import com.example.mobileproject.dataLayer.sources.GeneralPostRemoteSource;
import com.example.mobileproject.dataLayer.sources.PostWorkerSource;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.DBConverter;
import com.example.mobileproject.utils.Result;

import org.jetbrains.annotations.Blocking;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;

//import java.net.URL;

public final class PostRepository implements CallbackPosts {

    //LiveData non presenti("https://developer.android.com/topic/libraries/architecture/livedata#livedata-in-architecture")
    //TODO: maybe a byte[] data type is better than a android.graphics.Bitmap?
    //TODO: maybe a java.net.URL/java.net.URI data type is better than a android.net.Uri?
    private PostResponseCallback c;
    private final GeneralPostRemoteSource rem;
    private final GeneralPostLocalSource loc;
    private final GeneralAdvSource ads;
    private final PostWorkerSource w;

    public PostRepository(GeneralPostRemoteSource rem, GeneralPostLocalSource loc, GeneralAdvSource ads, PostWorkerSource postWorkerSource){
        this.rem = rem;
        this.loc = loc;
        this.ads = ads;
        this.w = postWorkerSource;
        this.rem.setCallback(this);
        this.loc.setCallback(this);
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
        rem.retrievePostsByAuthor(idUser, page);
    }

    /**
     * Prende tutti i post in locale
     *
     * @param page pagina
     * */
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
    public void retrievePostsWithTagsLL(String[] tags, int page){
        rem.retrievePostsWithTagsLL(tags, page);
    }

    /**
     * Prende un solo post sponsorizzato
     */
    //TODO: controllare, mi sa che ho sbagliato qualcosa...
    public void retrieveSponsoredPosts(){
        if ((int) (Math.random() * 3) == 1) {
            ads.getAdvPost();
        } else rem.retrievePostsSponsor();
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
        Uri img = loc.createImage(bmp, post.getId());
        if(img == null){
            c.onResponseCreation(new Result.Error(""));
        }
        post.setImage(img);
        loc.insertPost(post);
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
        List<Post> pl;
        try{
            pl = loc.retrieveNoSyncPosts().get();
        } catch (ExecutionException | InterruptedException e){
            return false;
        }
        for(Post p : pl){
            long now = System.currentTimeMillis();
            p.setPubblicazione(DBConverter.dateFromTimestamp(now));
            Future<String> fs = rem.createPost(p);//tanto in remoto non ha l'immagine...
            Bitmap bmp = loc.getImage(p.getImage());
            try {
                String newId = fs.get();
                if(newId != null){
                    Future<Boolean> f = rem.createImage(newId, bmp);
                    Uri newImg = loc.renameImage(p.getImage(), newId);
                    if(f.get() && newImg != null){
                        loc.modifyId(p.getId(), newId);
                        p.setImage(newImg);
                        //loc.modifyImage(p.getId(), newImg);
                        loc.updatePost(p);
                    }
                    //TODO: else
                }
                //se fallisce: non faccio nulla, ritento la prossima volta
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
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
        int page = 0;
        long time = lastUpdate;
        long now = System.currentTimeMillis();
        try {
            List<Post> pl = rem.retrieveUserPostsForSync(page, lastUpdate).get();
            while (pl.size() != 0) {
                for (Post p : pl) {
                    p.setImage(null);
                }
                loc.insertPosts(pl);
                time = pl.get(pl.size() - 1).getData().getTime();
                for (Post p : pl) {
                    File f = loc.createEmptyImageFile(p.getId());
                    if(rem.retrieveImage(p.getId(), f).get()){
                        p.setImage(Uri.fromFile(f));
                        loc.updatePost(p);
                        //loc.modifyImage(p.getId(), Uri.fromFile(f));
                    }
                    else {
                        w.startFinishingDownload();
                    }
                }
                page++;
                pl = rem.retrieveUserPostsForSync(page, lastUpdate).get();
            }
        } catch (InterruptedException e){
            return time;
            //TODO: come faccio a capire se ha fallito?
        }
        return now;
    }

    /**
     * Cancella tutti i dati in locale
     * */
    public void deleteData() {
        loc.deletePosts();
        loc.deleteImages();
    }
    @Blocking
    public boolean syncImages(){
        List<String> ids;
        try {
            ids = loc.retrieveIDsWithNoImage().get();
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
        for(String s : ids){
            File f = loc.createEmptyImageFile(s);
            try {
                if(rem.retrieveImage(s, f).get()){
                    loc.modifyImage(s, Uri.fromFile(f));
                }
            } catch (ExecutionException | InterruptedException e) {
                return false;
            }
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