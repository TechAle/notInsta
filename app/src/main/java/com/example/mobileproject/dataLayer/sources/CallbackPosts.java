package com.example.mobileproject.dataLayer.sources;

import android.content.ContentResolver;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.Result;

import java.util.List;

/**
 * Interfaccia per le chiamate di ritorno.
 */

//Nel caso dovessero servire altre chiamate di ritorno, inseritele qua
public interface CallbackPosts extends CallbackInterface {
    void onSuccess(List<Post> res);
    void onUploadSuccess(); //TODO: Usare o eliminare
}
