package com.example.mobileproject.dataLayer.repositories;

import android.content.ContentResolver;
import android.net.Uri;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.mobileproject.dataLayer.sources.CallbackPosts;
import com.example.mobileproject.dataLayer.sources.GeneralPostRemoteSource;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.Result;

import java.util.List;

public class PostRepository implements CallbackPosts {

    private final MutableLiveData<Result> posts;
    private final MutableLiveData<Result> ready;
    private final GeneralPostRemoteSource rem;
    private Uri image;              //Ma a cosa serve questo???

    public void resetPosts() {
        this.posts.setValue(null);
    }

    public PostRepository(GeneralPostRemoteSource rem){
        this.rem = rem;
        this.rem.setCallback(this);
        posts = new MutableLiveData<>();
        ready = new MutableLiveData<>();
    }

    //assegnamento in callback
    public MutableLiveData<Result> retrievePosts(){
        rem.retrievePosts();
        return posts;
    }

    //assegnamento in callback
    public MutableLiveData<Result> retrievePosts(String tag){
        rem.retrievePostByDocumentId(tag);
        return posts;
    }
    public MutableLiveData<Result> retrieveUserPosts(String idUser, int page){
        rem.retrievePostsByAuthor(idUser, page);
        return posts;
    }

    public MutableLiveData<Result> retrievePostsLL(int page){ //Lazy Loading
        rem.retrievePostsLL(page);
        return posts;
    }
    public MutableLiveData<Result> retrievePostsWithTagsLL(String tags[], int page){ //Lazy Loading
        rem.retrievePostsWithTagsLL(tags, page);
        return posts;
    }
    public MutableLiveData<Result> retrieveSponsoredPosts(LifecycleOwner ow){
        rem.retrievePostsSponsor(this, ow);
        return posts;
    }

    public MutableLiveData<Result> createPost(Post post) {
        rem.createPosts(post);
        return ready;
    }

    @Override
    public void onSuccess(List<Post> res) {
        /*if (posts.getValue() != null && posts.getValue().successful()) { //Lazy Loading
            List<Post> l = ((Result.PostResponseSuccess)posts.getValue()).getData().getPostList();
            l.addAll(res);
            Result.PostResponseSuccess result = new Result.PostResponseSuccess(new PostResp(l));
            posts.postValue(result);
        } else {*/
            Result.PostResponseSuccess result = new Result.PostResponseSuccess(new PostResp(res));
            posts.postValue(result);
        //}
    }


    @Override
    public void onSuccess() {
    }

    @Override
    public void onFailure(Exception e) {
        Result.Error resultError = new Result.Error(e.getMessage());
        posts.postValue(resultError);
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
}
