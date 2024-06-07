package com.example.mobileproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

/**
 * Classe di utilità per il salvataggio di coppie chiave-valore non crittate.
 * Utilizza la API SharedPreferences.
 */

//Pensata originariamente per essere usata con la API DataStore
public class DataStoreSingleton {

    private final Context c;
    public DataStoreSingleton(Context c){
        this.c = c;
    }
    public void writeStringData(String sharedPreferencesFileName, String key, String value) {
        SharedPreferences sharedPref = c.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public String readStringData(String sharedPreferencesFileName, String key) {
        SharedPreferences sharedPref = c.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }
    public long readLongData(String sharedPreferencesFileName, String key){
        SharedPreferences sharedPref = c.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        return sharedPref.getLong(key, 0);
    }
    public void writeLongData(String sharedPreferencesFileName, String key, long value) {
        SharedPreferences sharedPref = c.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key, value);
        editor.apply();
    }
    public void writeStringsData(String sharedPreferencesFileName, String key, List<String> values){
        writeStringData(sharedPreferencesFileName, key, DBConverter.fromList(values));
    }
    public List<String> readStringsData(String sharedPreferencesFileName, String key){
        return DBConverter.toList(readStringData(sharedPreferencesFileName, key));
    }
    public void deleteAll(String sharedPreferencesFileName) {
        SharedPreferences sharedPref = c.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
