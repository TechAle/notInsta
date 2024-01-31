package com.example.mobileproject.dataLayer.sources;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Users.Users;

import java.util.List;

/**
 * Interfaccia per le chiamate di ritorno.
 */

//Nel caso dovessero servire altre chiamate di ritorno, inseritele qua
public interface CallbackUsers extends CallbackInterface {
    void onSuccess(List<Users> res);

    void onUploadSuccess(); //TODO: Usare o eliminare
}
