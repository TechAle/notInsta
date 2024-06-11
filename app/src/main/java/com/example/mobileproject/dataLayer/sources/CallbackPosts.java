package com.example.mobileproject.dataLayer.sources;

import com.example.mobileproject.models.Post.Post;

import java.util.List;

/**
 * Interfaccia per le chiamate di ritorno da source a repository.
 */

//Nel caso dovessero servire altre chiamate di ritorno, inseritele qua
public interface CallbackPosts{
    //void onSuccess(List<Post> res);
    void onSuccessG(List<Post> res);
    void onSuccessO(List<Post> res);
    void onSuccessF(List<Post> res);
    void onFailureG(Exception e);
    void onFailureO(Exception e);
    void onFailureF(Exception e);
    void onLocalSaveSuccess();
    void onSuccessAdv(Post p);
    void onFailureAdv(Exception e);

}
