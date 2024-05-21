package com.example.mobileproject.UI.Home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
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
public class PostsViewModel extends ViewModel implements UserResponseCallback{

    /**
     * parametro per memorizzare il fragment corrente
     */
    private int fragment;
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
    private final List<MutableLiveData<Result>> posts;
    private final List<List<Post>> loadedPosts;

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
        this.repoU = repoU;
        repoU.setCallback(this);
        posts = new ArrayList<>();
        this.tag = null;
        this.page = new int[fragmentNumber];
        this.allPosts = new boolean[fragmentNumber];
        this.loading = new boolean[fragmentNumber];
        this.firstLoading = new boolean[fragmentNumber];
        this.loadedPosts = new ArrayList<>();
        for(int i = 0; i < fragmentNumber; i++){
            this.page[i] = 0;
            this.firstLoading[i] = true;
            this.loading[i] = false;
            this.allPosts[i] = false;
            this.loadedPosts.add(null);
            posts.add(null);
        }
        this.userTags = new HashSet<>();
        this.fragment = 0;
        this.user = new MutableLiveData<>();
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
    public boolean addTag(String tag){
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
    /*public void removeTag(){
        userTags.clear();
        flush(fragment);
    }
    public boolean removeTag(String tag){
        boolean success = userTags.remove(tag);
        if(success){
            flush(fragment);
            getPosts();
        }
        return success;
    }*/

    /**
     * Metodo per chiamare la repo relativa ai post. Il comportamento dipende dal fragment impostato
     */
    public MutableLiveData<Result> getPosts(FragmentType f){
        switch(f){
            case GLOBAL:
                if(posts.get(fragment) == null){
                    if(userTags.size() != 0){
                        posts.set(fragment, repoP.retrievePostsWithTagsLL(userTags.toArray(new String[userTags.size()]), page[fragment]));
                    } else {
                        posts.set(fragment, repoP.retrievePostsLL(page[fragment]));
                    }
                }
                break;
            case FOUND:
                if(this.tag != null && !this.tag.isEmpty()){
                    if(tag.startsWith("#")){
                        String[] s = new String[1];
                        s[0] = tag;
                        posts.set(fragment, repoP.retrievePostsWithTagsLL(s, page[fragment]));
                    }
                    posts.set(fragment, repoP.retrievePostsbyAuthor(tag, page[fragment]));
                }
                break;
            case OWNED:
                posts.set(fragment, repoP.retrieveUserPosts(page[fragment]));
                break;
            default:
                throw new RuntimeException();
        }
        return posts.get(fromEnumToInt(f));
    }

    public MutableLiveData<Result> getActualPosts(){ //versione senza chiamata
        return posts.get(fragment);
    }
    public MutableLiveData<Result> getSponsoredPosts(){
        return repoP.retrieveSponsoredPosts();
    }

    public int getPage() {
        return page[fragment];
    }
    public void setPage(int page) {
        this.page[fragment] = page;
    }
    public void setLoading(boolean loading) {
        this.loading[fragment] = loading;
    }
    public void setFirstLoading(boolean firstLoading) {
        this.firstLoading[fragment] = firstLoading;
    }
    public boolean isLoading() {
        return loading[fragment];
    }
    public boolean isFirstLoading() {
        return firstLoading[fragment];
    }
    public void findPosts(FragmentType f){
        switch(f){
            case GLOBAL:
                repoP.retrievePostsLL(page[0]);
                break;
            case FOUND:
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
                break;
            case OWNED:
                repoP.retrieveUserPosts(page[2]);
                break;
            default:
                throw new RuntimeException();
        }
    }
    public void setAllPosts(boolean allPosts) {
        this.allPosts[fragment] = allPosts;
    }
    public boolean areAllPosts() {
        return allPosts[fragment];
    }
    private void flush(int fragmentNum){
        this.posts.set(fragmentNum, null);
        this.page[fragmentNum] = 0;
        this.allPosts[fragmentNum] = false;
        this.loading[fragmentNum] = false;
        this.firstLoading[fragmentNum] = true;
    }

    /**
     * Metodo per modificare il parametro fragment
     */
    public void setCurrentFragment(int fragment){
        this.fragment = fragment;
    }
    public MutableLiveData<Users> getUser(){
        if(user.getValue() == null){
            repoU.getLoggedUser();
        }
        return user;
    }
    public void savePosts(List<Post> pl, int fragmentNumber){
        loadedPosts.set(fragmentNumber, pl);
    }
    public List<Post> getSavedPosts(int fragmentNumber){
        List<Post> tmp = loadedPosts.get(fragmentNumber);
        if (tmp == null) tmp = new ArrayList<>();
        return tmp;
    }
    @Override
    public void onResponseUser(Result r) {
        if(r.successful()){
            user.setValue(((Result.UserResponseSuccess) r).getData().getUsersList().get(0));
        } else {
            user.setValue(null);
        }
    }
}