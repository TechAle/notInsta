package com.example.mobileproject.dataLayer.sources;

import static com.example.mobileproject.utils.Constants.DB_NAME;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.mobileproject.models.Post.Post;

@Database(entities = {Post.class}, version = 1)
public abstract class PostRoomDatabase extends RoomDatabase {
    public abstract PostDao postDao();
    private static final int THREAD_NUMBER = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(THREAD_NUMBER);
    private static volatile PostRoomDatabase instance;
    public static PostRoomDatabase getInstance(final Context c){
        if(instance == null){
            synchronized (PostRoomDatabase.class){
                if(instance == null){
                    instance = Room.databaseBuilder(c.getApplicationContext(), PostRoomDatabase.class, DB_NAME).build(); //TODO: change this name...
                }
            }
        }
        return instance;
    }
}
