package com.example.mobileproject.UI.Home;

import android.view.View;

import androidx.lifecycle.MutableLiveData;

import static com.example.mobileproject.utils.Constants.ELEMENTS_LAZY_LOADING;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.Result;

import java.util.List;

public class SearchGalleryFragment extends GenericGalleryFragment{

    @Override
    protected void FetchAction(View v) {}

    void loadPosts(String s){
        PVM.addTag(s);
        MutableLiveData<Result> r = PVM.getPosts();
        if(r == null) return;
        r.observe(getViewLifecycleOwner(), result -> {
            if(result.successful()){//Successo nel recuperare i dati
                PostResp resp = ((Result.PostResponseSuccess) result).getData();
                List<Post> res = resp.getPostList();
                if(!PVM.isLoading()){ //Se non è attiva una chiamata
                /*if(res.size() == 0){
                    PVM.setAllPosts(true);
                    postList.remove(null);
                    return;
                }*/
                    if(PVM.isFirstLoading()){ //Se è il primo caricamento -> lista interna vuota o aggiornamento completo
                        PVM.setFirstLoading(false);
                        int lastSize = postList.size();
                        postList.clear();
                        pa.notifyItemRangeRemoved(0, lastSize);
                        postList.addAll(res);
                        pa.notifyItemRangeInserted(0, postList.size());
                        if(postList.size() < ELEMENTS_LAZY_LOADING){ // Ho meno post di quelli possibili, allora non ne ho altri
                            PVM.setAllPosts(true);
                        }
                    } else {// ???
                        postList.clear();
                        postList.addAll(res);
                        pa.notifyItemRangeInserted(0, res.size());
                        if(postList.size() < ELEMENTS_LAZY_LOADING){ // Ho meno post di quelli possibili, allora non ne ho altri
                            PVM.setAllPosts(true);
                        }
                    }
                    //bindings
                } else {
                    PVM.setLoading(false);
                /*if(res.size() == 0){
                    PVM.setAllPosts(true);
                    postList.remove(null);
                    return;
                }*/
                    int initial_size = postList.size();
                    for(int i = 0; i < postList.size(); i++){
                        if(postList.get(i) == null){
                            postList.remove(null);
                        }
                    }
                    int sz = res.size();
                    for(int i = 0; i < sz; i++){
                        postList.add(res.get(i));
                    }
                    if(sz < ELEMENTS_LAZY_LOADING){
                        PVM.setAllPosts(true);
                    }
                    if(sz == 0){
                        pa.notifyItemRangeRemoved(initial_size, initial_size-1);
                        PVM.setAllPosts(true);
                    }
                    else if(sz == 1){
                        pa.notifyItemChanged(initial_size);
                    }
                    else {
                        pa.notifyItemRangeInserted(initial_size, postList.size());
                    }
                }
            } else {
                //TODO: codice relativo + errore
                //binding.progressBar.setVisibility(View.GONE);
            }
        });
    }
}

