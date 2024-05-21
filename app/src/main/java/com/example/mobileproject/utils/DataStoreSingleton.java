package com.example.mobileproject.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

/**
 * Classe di utilità per il salvataggio di coppie chiave-valore non crittate. Utilizza la API DataStore.
 */

public class DataStoreSingleton {
    private static volatile DataStoreSingleton INSTANCE = null;
    private final RxDataStore<Preferences> ds;
    private final Preferences error = new Preferences(){
        @Override
        public <T> boolean contains(@NonNull Key<T> key) {
            return false;
        }
        @Nullable
        @Override
        public <T> T get(@NonNull Key<T> key) {
            return null;
        }
        @NonNull
        @Override
        public Map<Key<?>, Object> asMap() {
            return new HashMap<>();
        }
    };
    private DataStoreSingleton(Context c){
        ds = new RxPreferenceDataStoreBuilder(c, "Nome").build();//TODO: controllare
    }
    public static DataStoreSingleton getInstance(Context c){
        if (INSTANCE == null) {
            synchronized (DataStoreSingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataStoreSingleton(c);
                }
            }
        }
        return INSTANCE;
    }
    public Long readLongData(String keyName){
        Preferences.Key<Long> val = PreferencesKeys.longKey(keyName);
        Single<Long> value = ds.data().firstOrError().map(x -> x.get(val));
        return value.blockingGet();//TODO: valutare una non bloccante
    }
    public boolean writeLongData(String keyName, Long value){
        Preferences.Key<Long> val = PreferencesKeys.longKey(keyName);
        Single<Preferences> res = ds.updateDataAsync(p -> {
            MutablePreferences mp = p.toMutablePreferences();
            mp.set(val, value);
            return Single.just(mp);
        }).onErrorReturnItem(error);
        return res.blockingGet() != error;
    }
    public String readStringData(String keyname){
        Preferences.Key<String> val = PreferencesKeys.stringKey(keyname);
        Single<Preferences> valq = ds.data().first(error);
        Single<String> value = ds.data().firstOrError().map(x -> x.get(val));
        String s;
        try{
            s = value.blockingGet();
        } catch(Exception e){
            s = "bug";
        }
        return s;
    }
    public boolean writeStringData(String keyname, String value){
        Preferences.Key<String> val = PreferencesKeys.stringKey(keyname);
        Single<Preferences> res = ds.updateDataAsync(p -> {
            MutablePreferences mp = p.toMutablePreferences();
            mp.set(val, value);
            return Single.just(mp);
        }).onErrorReturnItem(error);
        return res.blockingGet() != error;
    }
    //Possibile aggiunta di valori
}
