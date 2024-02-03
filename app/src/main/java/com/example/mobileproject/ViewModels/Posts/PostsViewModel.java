package com.example.mobileproject.ViewModels.Posts;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.utils.Result;

import java.util.HashSet;
import java.util.Set;

/**
 * ViewModel associato a PostGalleryFragment
 */

//Usatelo solamente per comunicare con PostGalleryFragment, NON USATELO PER SALVARE STATI DI ALTRI FRAGMENT
public class PostsViewModel extends ViewModel {
    private final PostRepository repo;
    private Set<String> tags;       //TODO: controllare il 'final'
    private int page;
    private boolean allPosts; //Vero se sono stati recuperati tutti i post oppure se è cambiato qualcosa nei tag
    private boolean loading; //Se ha iniziato una chiamata al server o meno
    private boolean firstLoading;
    private String idUser;
    private MutableLiveData<Result> posts;

    public PostsViewModel(PostRepository repo) {
        this.repo = repo;
        this.page = 0;
        this.firstLoading = true;
        this.loading = false;
        this.allPosts = false;
        this.tags = new HashSet<>();
    }

    public void setIdUser(String id){
        idUser = id;
        removeTag();
        getPosts();
    }
    public boolean addTag(String tag){
        boolean success = tags.add(tag);
        if(success){
            idUser = null;
            flush();
        }
        getPosts();
        return success;
    }
    public void removeTag(){
        tags.clear();
        flush();
    }
    public boolean removeTag(String tag){
        boolean success = tags.remove(tag);
        if(success){
            flush();
        }
        //getPosts();
        return success;
    }

    //getters & setters
    public MutableLiveData<Result> getPosts(){
        if(posts == null){          //mai caricati o tag non corrispondente
            if(tags.size() != 0){
                posts = repo.retrievePostsWithTagsLL(tags.toArray(new String[tags.size()]), page);
            } else if(idUser != null) {
                posts = repo.retrievePostsbyAuthor(idUser, page);
            } else {                //nessun filtro di ricerca, li prendo tutti
                posts = repo.retrievePostsLL(page); //versione Lazy loading
            }
        }
        return posts;
    }

    public MutableLiveData<Result> getActualPosts(){ //versione senza chiamata
        return posts;
    }

    /*
    //TODO: Fatevi una vostra ViewModel! Ogni activity/fragment ha il suo ViewModel (e solo uno, non di più)
    public MutableLiveData<Result> createPost(Post post) {
        return repo.createPost(post);
    }

    public MutableLiveData<Result>  createImage(Uri imageUri, String document, ContentResolver contentResolver, String id) {
        return repo.createImage(imageUri, document, contentResolver, id);
    }
    public MutableLiveData<Result> getSponsodedPosts(LifecycleOwner ow){
        return repo.retrieveSponsoredPosts(ow);
    }
    */
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public void setLoading(boolean loading) {
        this.loading = loading;
    }
    public void setFirstLoading(boolean firstLoading) {
        this.firstLoading = firstLoading;
    }
    public boolean isLoading() {
        return loading;
    }
    public boolean isFirstLoading() {
        return firstLoading;
    }
    public void findPosts(){
        repo.retrievePostsLL(page);
    }
    public void setAllPosts(boolean allPosts) {
        this.allPosts = allPosts;
    }
    public boolean areAllPosts() {
        return allPosts;
    }
    private void flush(){
        this.posts = null;
        this.page = 0;
        this.allPosts = false;
        this.loading = false;
        this.firstLoading = true;
    }
}