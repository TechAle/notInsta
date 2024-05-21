package com.example.mobileproject.UI.Home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.PostResponseCallback;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.dataLayer.repositories.UserResponseCallback;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.utils.Result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe ViewModel per l'attività principale
 */
public class PostsViewModel extends ViewModel implements UserResponseCallback, PostResponseCallback {

    private static final int fragmentNumber = 3; //uguale alla dimensione di FragmentType
    private final PostRepository repoP;
    private final UserRepository repoU;
    private final Set<String> userTags;
    private String tag;
    private final int[] page;
    private final boolean[] allPosts; //Vero se sono stati recuperati tutti i post oppure se è cambiato qualcosa nei tag
    private final boolean[] loading; //Se ha iniziato una chiamata al server o meno
    private final boolean[] firstLoading;
    private final MutableLiveData<Users> user;
    private final MutableLiveData<Post> adv;
    private final List<MutableLiveData<List<Post>>> posts;

    public enum FragmentType{
        GLOBAL,
        OWNED,
        FOUND
    }
    /**
     * Costruttore della viewModel
     */
    public PostsViewModel(PostRepository repoP, UserRepository repoU) {
        this.repoP = repoP;
        repoP.setCallback(this);
        this.repoU = repoU;
        repoU.setCallback(this);
        posts = new ArrayList<>();
        this.tag = null;
        this.page = new int[fragmentNumber];
        this.allPosts = new boolean[fragmentNumber];
        this.loading = new boolean[fragmentNumber];
        this.firstLoading = new boolean[fragmentNumber];
        for(int i = 0; i < fragmentNumber; i++){
            this.page[i] = 0;
            this.firstLoading[i] = true;
            this.loading[i] = false;
            this.allPosts[i] = false;
            posts.add(null);
        }
        this.userTags = new HashSet<>();
        this.user = new MutableLiveData<>();
        this.adv = new MutableLiveData<>();
    }

    private int fromEnumToInt(FragmentType f){
        switch(f){
            case FOUND:
                return 1;
            case OWNED:
                return 2;
            case GLOBAL:
                return 0;
            default:
                throw new RuntimeException();
        }
    }
    public boolean setSearchTag(String tag){
        this.tag = tag;
        flush(1);
        return true; //in questo caso può essere anche buttato
    }
    public boolean addTag(String tag){
        if(userTags.add(tag)){
            flush(0);
            return true;
        }
        return false;
    }
    public void removeTag(){
        userTags.clear();
        flush(0);
    }
    /*public boolean removeTag(String tag){//TODO
        boolean success = userTags.remove(tag);
        if(success){
            flush(fragment);
            getPosts();
        }
        return success;
    }*/

    /**
     * Metodo getter del MutableLiveData relativo ai post generici
     */
    public MutableLiveData<List<Post>> getGlobalPosts(){
        MutableLiveData<List<Post>> l = posts.get(0);
        if(l == null){
            posts.set(0, new MutableLiveData<>());
            l = posts.get(0);
            if(userTags.size() != 0){//TODO
                repoP.retrievePostsWithTagsLL(userTags.toArray(new String[userTags.size()]), page[0]);
            } else {
                findGlobalPosts();
            }
        }
        return l;
    }
    public void findGlobalPosts(){
        repoP.retrievePosts(page[0]);
    }
    public MutableLiveData<List<Post>> getFoundPosts(){
        MutableLiveData<List<Post>> l = posts.get(1);
        if(l == null){
            l = new MutableLiveData<>();
            posts.set(1, l);
            if(this.tag != null && !this.tag.isEmpty()){//TODO
                if(tag.startsWith("#")){
                    String[] s = new String[1];
                    s[0] = tag;
                    repoP.retrievePostsWithTagsLL(s, page[1]);
                }
                repoP.retrievePostsbyAuthor(tag, page[1]);
            }
        }
        return l;
    }
    public void findFoundPosts(){
        if(this.tag != null && !this.tag.isEmpty()){
            if(tag.startsWith("#")){
                String[] s = new String[1];
                s[0] = tag;
                repoP.retrievePostsWithTagsLL(s, page[1]);
            }
            else{
                repoP.retrievePostsbyAuthor(tag, page[1]);
            }
        }
    }
    public MutableLiveData<List<Post>> getOwnedPosts(){
        MutableLiveData<List<Post>> l = posts.get(2);
        if(l == null){
            l = new MutableLiveData<>();
            posts.set(2, l);
            findOwnedPosts();
        }
        return l;
    }
    public void findOwnedPosts(){
        repoP.retrieveUserPosts(page[2]);
    }
    public MutableLiveData<Post> getSponsoredPosts(){
        repoP.retrieveSponsoredPosts();
        return adv;
    }

    public int getPage(FragmentType f) {
        return page[fromEnumToInt(f)];
    }
    public void setPage(FragmentType f, int page) {
        this.page[fromEnumToInt(f)] = page;
    }
    public void setLoading(FragmentType f, boolean loading) {
        this.loading[fromEnumToInt(f)] = loading;
    }
    public void setFirstLoading(FragmentType f, boolean firstLoading) {
        this.firstLoading[fromEnumToInt(f)] = firstLoading;
    }
    public boolean isLoading(FragmentType f) {
        return loading[fromEnumToInt(f)];
    }
    public boolean isFirstLoading(FragmentType f) {
        return firstLoading[fromEnumToInt(f)];
    }
    public void setAllPosts(FragmentType f, boolean allPosts) {
        this.allPosts[fromEnumToInt(f)] = allPosts;
    }
    public boolean areAllPosts(FragmentType f) {
        return allPosts[fromEnumToInt(f)];
    }
    private void flush(int fragmentNum){
        this.posts.set(fragmentNum, null);
        this.page[fragmentNum] = 0;
        this.allPosts[fragmentNum] = false;
        this.loading[fragmentNum] = false;
        this.firstLoading[fragmentNum] = true;
    }
    /*
    public void setCurrentFragment(int fragment){
        this.fragment = fragment;
    }*/
    public MutableLiveData<Users> getUser(){
        if(user.getValue() == null){
            repoU.getLoggedUser();
        }
        return user;
    }
    @Override
    public void onResponseUser(Result r) {
        if(r.successful()){
            user.setValue(((Result.UserResponseSuccess) r).getData().getUsersList().get(0));
        } else {
            user.setValue(null);
        }
    }
    @Override
    public void onResponseGlobalPost(Result r) {
        if(r.successful()){
            MutableLiveData<List<Post>> livedata = posts.get(0);
            List<Post> oldList = livedata.getValue();
            List<Post> newList;
            if(oldList == null){
                newList = new ArrayList<>(((Result.PostResponseSuccess) r).getData().getPostList());
            } else {
                newList = new ArrayList<>(livedata.getValue());
                newList.addAll(((Result.PostResponseSuccess) r).getData().getPostList());
            }
            livedata.postValue(newList);
        } else {
            //TODO
        }
    }
    @Override
    public void onResponseFoundPosts(Result r) {
        if(r.successful()){
            MutableLiveData<List<Post>> livedata = posts.get(1);
            List<Post> oldList = livedata.getValue();
            List<Post> newList;
            if(oldList == null){
                newList = new ArrayList<>(((Result.PostResponseSuccess) r).getData().getPostList());
            } else {
                newList = new ArrayList<>(livedata.getValue());
                newList.addAll(((Result.PostResponseSuccess) r).getData().getPostList());
            }
            livedata.postValue(newList);
        } else {
            //TODO
        }
    }
    @Override
    public void onResponseOwnedPosts(Result r) {
        if(r.successful()){
            MutableLiveData<List<Post>> livedata = posts.get(2);
            List<Post> oldList = livedata.getValue();
            List<Post> newList;
            if(oldList == null){
                newList = new ArrayList<>(((Result.PostResponseSuccess) r).getData().getPostList());
            } else {
                newList = new ArrayList<>(livedata.getValue());
                newList.addAll(((Result.PostResponseSuccess) r).getData().getPostList());
            }
            livedata.postValue(newList);
        } else {
            //TODO
        }
    }
    @Override
    public void onResponseAdvPost(Result r){
        if(r.successful()){
            adv.postValue(((Result.PostResponseSuccess) r).getData().getPostList().get(0));
        } else {
            adv.postValue(null);
        }
    }
}