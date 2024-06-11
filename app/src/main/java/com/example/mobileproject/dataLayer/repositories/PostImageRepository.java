package com.example.mobileproject.dataLayer.repositories;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.mobileproject.dataLayer.sources.GeneralPostImageLocalSource;
import com.example.mobileproject.dataLayer.sources.GeneralPostImageRemoteSource;
import com.example.mobileproject.models.Post.Post;

import org.jetbrains.annotations.Blocking;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class PostImageRepository {
    private final GeneralPostImageRemoteSource rem;
    private final GeneralPostImageLocalSource loc;
    public PostImageRepository(GeneralPostImageRemoteSource rem, GeneralPostImageLocalSource loc){
        this.rem = rem;
        this.loc = loc;
    }

    /**
     * Sincronizzazione immagini in base agli identificativi dei post
     *
     * @param ids Lista di ID di post di cui si vuole sincronizzare le immagini
     *
     * @return Lista di identificatori di risorsa, indicanti i file dove sono salvate le immagini
     * */
    @Blocking
    public List<Uri> syncImagesFromRemote(List<String> ids){
        List<Uri> modifiedImages = new ArrayList<>();
        for(String s : ids){
            File f = loc.createEmptyImageFile(s);
            try {
                if(rem.retrieveImage(s, f).get()){
                    modifiedImages.add(Uri.fromFile(f));
                }
            } catch (ExecutionException | InterruptedException e) {
                modifiedImages.add(null);
            }
        }
        return modifiedImages;
    }

    /**
     * Sincronizzazione immagini dei post dati
     *
     * @param pl lista di post
     *
     * @return Lista di successi/fallimenti. Se fallisce il post non viene modificato
     * */
    public List<Post> syncImagesFromLocal(List<Post> pl){
        //gli ID vengono cambiati prima, dovrebbero essere quelli aggiornati
        List<Post> results = new ArrayList<>();
        for(Post p : pl){
            Bitmap bmp = loc.getImage(p.getImage());
            try{
                boolean b = rem.createImage(p.getId(), bmp).get();
                if(b){
                    p.setImage(loc.renameImage(p.getImage(), p.getId()));
                }
            } catch (ExecutionException | InterruptedException e) {

            }
            results.add(p);
        }
        return results;
    }
    /**
     * Cancellazione delle immagini
     * */
    public void deleteLocal(){
        loc.deleteImages();
    }

    /**
     * Creazione immagini in locale
     *
     * @return identificatore del file immagine
     * */
    public Uri createImage(Bitmap bmp, String id) {
        return loc.createImage(bmp, id);
    }
}
