package com.example.mobileproject.dataLayer.sources;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mobileproject.models.Post.Post;

import java.util.List;

@Dao
public interface PostDao {
/*    @Query("SELECT * FROM posts ORDER BY pubblicazione")
    List<Post> getUserPosts();

    @Query("SELECT * FROM posts WHERE pubblicazione IS NULL")
    List<Post> getNoSyncPosts();

    @Insert
    void insertAll(List<Post> l);

    @Update
    int updateSyncPosts(List<Post> l);

    @Delete
    void deletePost(Post p);
    @Delete
    void deleteAllWithoutQuery(Post... posts);

    @Query("DELETE FROM posts")
    int deleteAll();*/
}
