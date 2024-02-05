package com.example.mobileproject.ViewModels.Posts;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.Result;

import java.util.HashSet;
import java.util.Set;

public class PostsViewModel extends ViewModel {

    private int currentFragment;
    private static final int fragmentNumber = 3;
    private final PostRepository repoP;
    private final UserRepository repoU;
    private Set<String> userTags;       //TODO: controllare il 'final'
    private String tag;
    private int[] page; //TODO: rinominazione
    private boolean[] allPosts; //Vero se sono stati recuperati tutti i post oppure se è cambiato qualcosa nei tag
    private boolean[] loading; //Se ha iniziato una chiamata al server o meno
    private boolean[] firstLoading;
    private String idUser;
    private MutableLiveData<Result>[] posts;

    public PostsViewModel(PostRepository repoP, UserRepository repoU) {
        this.repoP = repoP;
        this.repoU = repoU;
        this.page = new int[fragmentNumber];
        this.allPosts = new boolean[fragmentNumber];
        this.loading = new boolean[fragmentNumber];
        this.firstLoading = new boolean[fragmentNumber];
        for(int i = 0; i < fragmentNumber; i++){
            this.page[i] = 0;
            this.firstLoading[i] = true;
            this.loading[i] = false;
            this.allPosts[i] = false;
        }
        this.userTags = new HashSet<>();
    }
    public boolean addTag(String tag, int fragment){
        switch(fragment){
            case 0:
                if(userTags.add(tag)){
                    flush(fragment);
                    return true;
                }
                return false;
            case 1:
                this.tag = tag;
                flush(fragment);
                return true; //in questo caso può essere anche buttato
            default:
                throw new RuntimeException();
        }
    }
    public boolean removeTag(String tag){
        boolean success = userTags.remove(tag);
        if(success){
            flush(0);
            getPosts(0);
        }
        return success;
    }

    public MutableLiveData<Result> getPosts(int fragment){
        switch(fragment){
            case 0:
                if(posts[fragment] == null){
                    if(userTags.size() != 0){
                        posts[fragment] = repoP.retrievePostsWithTagsLL(userTags.toArray(new String[userTags.size()]), page[fragment]);
                    } else {
                        posts[fragment] = repoP.retrievePostsLL(page[fragment]);
                    }
                }
                break;
            case 1:
                String[] s = new String[1];
                s[0] = tag;
                posts[fragment] = repoP.retrievePostsWithTagsLL(s, page[fragment]);
                break;
            case 2:
                posts[fragment] = repoP.retrieveUserPosts(idUser, page[fragment]);
                break;
            default:
                throw new RuntimeException();
        }
        return posts[fragment];
    }

    public MutableLiveData<Result> getActualPosts(int fragment){ //versione senza chiamata
        return posts[fragment];
    }

    public MutableLiveData<Result> createPost(Post post) {
        return repoP.createPost(post);
    }

    public MutableLiveData<Result>  createImage(Uri imageUri, String document, ContentResolver contentResolver, String id) {
        return repoP.createImage(imageUri, document, contentResolver, id);
    }

    public MutableLiveData<Result> getSponsodedPosts(LifecycleOwner ow){
        return repoP.retrieveSponsoredPosts(ow);
    }

    public int getPage(int fragment) {
        return page[fragment];
    }
    public void setPage(int page, int fragment) {
        this.page[fragment] = page;
    }
    public void setLoading(boolean loading, int fragment) {
        this.loading[fragment] = loading;
    }
    public void setFirstLoading(boolean firstLoading, int fragment) {
        this.firstLoading[fragment] = firstLoading;
    }
    public boolean isLoading(int fragment) {
        return loading[fragment];
    }
    public boolean isFirstLoading(int fragment) {
        return firstLoading[fragment];
    }
    public void findPosts(){
        repoP.retrievePostsLL(page[0]);
    }
    public void setAllPosts(boolean allPosts, int fragment) {
        this.allPosts[fragment] = allPosts;
    }
    public boolean areAllPosts(int fragment) {
        return allPosts[fragment];
    }
    private void flush(int fragment){
        this.posts[fragment] = null;
        this.page[fragment] = 0;
        this.allPosts[fragment] = false;
        this.loading[fragment] = false;
        this.firstLoading[fragment] = true;
    }

    public void setCurrentFragment(int fragment){
        this.currentFragment = fragment;
    }
}