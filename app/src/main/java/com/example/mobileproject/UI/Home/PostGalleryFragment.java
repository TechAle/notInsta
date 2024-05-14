package com.example.mobileproject.UI.Home;

import androidx.fragment.app.Fragment;

import android.view.View;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.Result;
import com.google.android.material.snackbar.Snackbar;

import static com.example.mobileproject.utils.Constants.ELEMENTS_LAZY_LOADING;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public final class PostGalleryFragment extends GenericGalleryFragment{
    public PostGalleryFragment() {
        // Required empty public constructor
    }
    public static PostGalleryFragment newInstance() {
        PostGalleryFragment fragment = new PostGalleryFragment();
        return fragment;
    }
    @Override
    protected void FetchAction(View v) {
        PVM.getPosts().observe(getViewLifecycleOwner(), result -> {
            if(result.successful()){//Successo nel recuperare i dati
                PostResp resp = ((Result.PostResponseSuccess) result).getData();
                List<Post> res = resp.getPostList();
                if(!PVM.isLoading()){ //Se non è attiva una chiamata
                    /*if(res.size() == 0){
                        PVM.setAllPosts(true);
                        postList.remove(null);
                        return;
                    }*/
                    if(PVM.isFirstLoading()){ //Se è il primo caricamento -> lista interna vuota o aggiornamento completo richiesto
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
                    PVM.setLoading(false); //rendo disponibile un altra chiamata
                    int initial_size = postList.size();
                    for(int i = 0; i < postList.size(); i++){
                        if(postList.get(i) == null){
                            postList.remove(null);
                        }
                    }
                    int s = res.size();
                    for(int i = 0; i < s; i++){
                        postList.add(res.get(i));
                    }
                    if(s < ELEMENTS_LAZY_LOADING){
                        PVM.setAllPosts(true);
                    }
                    if(s == 0){
                        pa.notifyItemRangeRemoved(initial_size, initial_size-1);
                        PVM.setAllPosts(true);
                    }
                    else if(s == 1){
                        pa.notifyItemChanged(initial_size);
                    }
                    else {
                        pa.notifyItemRangeInserted(initial_size, postList.size());
                    }
                }
            } else {
                //TODO: codice relativo + errore
                //binding.progressBar.setVisibility(View.GONE);
                Snackbar.make(v, "Error while retrieving posts", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}