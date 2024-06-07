package com.example.mobileproject.dataLayer.sources;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.Constants;

import java.util.List;

@Dao
public interface PostDao {
    @Query("SELECT * FROM posts ORDER BY pubblicazione LIMIT " + Constants.ELEMENTS_LAZY_LOADING + " OFFSET :offset")
    List<Post> getUserPosts(int offset);
    @Query("SELECT * FROM posts WHERE pubblicazione IS NULL")
    List<Post> getNoSyncPosts();
    @Query("SELECT * FROM posts WHERE id = :id")
    Post getPost(String id);
    @Insert
    void insertAll(List<Post> l);
    @Insert
    void insertPost(Post p);
    @Delete
    void deletePost(Post p);
    @Delete
    void deleteAllWithoutQuery(Post... posts);
    @Query("DELETE FROM posts")
    void deleteAll();
    @Query("UPDATE posts SET image = :img WHERE id = :id")
    void updateImage(String id, String img);
}
