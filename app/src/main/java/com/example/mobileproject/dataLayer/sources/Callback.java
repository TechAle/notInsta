package com.example.mobileproject.dataLayer.sources;

import com.example.mobileproject.models.Post;

import java.util.List;

/**
 * Interfaccia per le chiamate di ritorno.
 */

//Nel caso dovessero servire altre chiamate di ritorno, inseritele qua
public interface Callback {
    void onSuccess(List<Post> res);
    void onFailure(Exception e);
    void onUploadSuccess(); //TODO: Usare o eliminare
}
