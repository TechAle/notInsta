package com.example.mobileproject.dataLayer.sources;

import com.example.mobileproject.models.Post.Post;

import java.util.List;

public class RoomPostLocalSource extends GeneralPostLocalSource{

    private final PostDao d;

    public RoomPostLocalSource(PostRoomDatabase db){
        this.d = db.postDao();
    }
    public void insertPosts(List<Post> l){

    }

    public void insertPost(){

    }
    @Override
    public void retrievePosts() {

    }

    @Override
    public void retrieveNoSyncPosts() {

    }

    @Override
    public void createPost() {

    }

    @Override
    public void deletePosts() {

    }
}
