package com.example.mobileproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Classe di utilità per il salvataggio di coppie chiave-valore non crittate. Utilizza la API DataStore.
 */

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
    public void deleteAll(String sharedPreferencesFileName) {
        SharedPreferences sharedPref = c.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
