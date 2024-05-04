package com.example.mobileproject.dataLayer.sources;

import android.content.ContentResolver;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.Result;

import java.util.List;

/**
 * Interfaccia per le chiamate di ritorno da source a repository.
 */

//Nel caso dovessero servire altre chiamate di ritorno, inseritele qua
public interface CallbackPosts extends CallbackInterface {
    //void onSuccess(List<Post> res);
    void onSuccessG(List<Post> res);
    void onSuccessO(List<Post> res);
    void onSuccessF(List<Post> res);
    void onFailureG(Exception e);
    void onFailureO(Exception e);
    void onFailureF(Exception e);
    /**
     * Callback per la sync da remoto
     */
    void onSuccessSyncRemote(List<Post> res);

    /**
     * Callback per la sync da locale
     */
    void onSuccessSyncLocal(List<Post> res);

    //Set di callback per lo sponsor
    void onSuccessAdv(Post p);
    void onFailureAdv(Exception e);
}
