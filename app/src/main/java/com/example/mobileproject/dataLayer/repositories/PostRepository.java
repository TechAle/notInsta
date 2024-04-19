package com.example.mobileproject.dataLayer.repositories;

import android.content.ContentResolver;
import android.net.Uri;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.mobileproject.dataLayer.sources.CallbackPosts;
import com.example.mobileproject.dataLayer.sources.GeneralPostLocalSource;
import com.example.mobileproject.dataLayer.sources.GeneralPostRemoteSource;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.Result;

import java.util.List;

public class PostRepository implements CallbackPosts {

    private final MutableLiveData<Result> postsG;
    private final MutableLiveData<Result> postsO;
    private final MutableLiveData<Result> postsF;
    private final MutableLiveData<Result> ready;
    private final GeneralPostRemoteSource rem;
 //   private final GeneralPostLocalSource loc;
    private Uri image;              //Ma a cosa serve questo???

    /*public void resetPosts() {
        this.posts.setValue(null);
    }*/

    public PostRepository(GeneralPostRemoteSource rem/*, GeneralPostLocalSource loc*/){
        this.rem = rem;
        //this.loc = loc;
        this.rem.setCallback(this);
        //this.loc.setCallback(this);
        postsG = new MutableLiveData<>();
        postsO = new MutableLiveData<>();
        postsF = new MutableLiveData<>();
        ready = new MutableLiveData<>();
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

    public MutableLiveData<Result> retrievePostsLL(int page){ //Lazy Loading
        rem.retrievePostsLL(page);
        return postsG;
    }
    public MutableLiveData<Result> retrievePostsWithTagsLL(String tags[], int page){ //Lazy Loading
        rem.retrievePostsWithTagsLL(tags, page);
        return postsG;
    }
    public MutableLiveData<Result> retrieveSponsoredPosts(LifecycleOwner ow){
        rem.retrievePostsSponsor(ow);
        return postsG;
    }

    public MutableLiveData<Result> createPost(Post post) {
        rem.createPosts(post);
        return ready;
    }

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
    public void onFailure(Exception e) {
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

    /*public boolean setupLocalDB(){
        rem.;
        return posts;
    }

    public void onSuccessInSetupRemote(){
        loc.
    }
    public void onSuccessInSetupLocal(){

    }*/
}
