package com.example.mobileproject.utils;

import android.net.Uri;

import androidx.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * Classe di conversione dei tipi, principalmente per Room
 * */
public class DBConverter {
    @TypeConverter
    public static Date dateFromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
    @TypeConverter
    public static Uri toUri(String s){
        return Uri.parse(s);
    }
    @TypeConverter
    public static String fromUri(Uri u){
        return u.toString();
    }
    @TypeConverter
    public static List<String> toList(String s){
        Type t = new TypeToken<List<String>>(){}.getType();
        return new Gson().fromJson(s,t);
    }
    @TypeConverter
    public static String fromList(List<String> s){
        Gson g = new Gson();
        return g.toJson(s);
    }
}
