package com.example.mobileproject.dataLayer.sources;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.mobileproject.utils.Result;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreRemoteSource extends GeneralPostRemoteSource{

    FirebaseFirestore db;
    public FirestoreRemoteSource(){
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public MutableLiveData<Result> retrievePosts(){
        db.collection("post")
                .orderBy("data")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        c.onSuccess(task.getResult());
                        /*for(QueryDocumentSnapshot i : task.getResult()){
                            //TODO: fai qualcosa qui
                        }*/
                    }
                    else{
                        c.onFailure(task.getException());
                        //Log.d("NET_ERR","In loading infos: ", task.getException());
                    }
                });
    }

    @Override
    public MutableLiveData<Result> retrievePosts(String tag){
        db.collection("post")
                .whereEqualTo("tag", tag)
                .orderBy("data")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        c.onSuccess(task.getResult());
                    }
                    else{
                        c.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void postPost() {
        //TODO: Funzione vuota. Modificarla o eliminarla
    }
}
