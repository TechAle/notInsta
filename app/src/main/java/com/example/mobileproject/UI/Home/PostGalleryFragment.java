package com.example.mobileproject.UI.Home;

import androidx.fragment.app.Fragment;

import android.view.View;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.Result;

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
                    if(PVM.isFirstLoading()){ //Se è il primo caricamento -> lista interna vuota o aggiornamento completo
                        PVM.setFirstLoading(false);
                        int lastSize = postList.size();
                        postList.clear();
                        pa.notifyItemRangeRemoved(0, lastSize);
                        postList.addAll(res);
                        pa.notifyItemRangeInserted(0, postList.size());
                        if(postList.size() < 20){ // Ho meno post di quelli possibili, allora non ne ho altri
                            PVM.setAllPosts(true);
                        }
                    } else {// ???
                        postList.clear();
                        postList.addAll(res);
                        pa.notifyItemRangeInserted(0, res.size());
                        if(postList.size() < 20){ // Ho meno post di quelli possibili, allora non ne ho altri
                            PVM.setAllPosts(true);
                        }
                    }
                    //bindings
                } else {
                    PVM.setLoading(false); //rendo disponibile un altra chiamata
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
                    int start_index = (PVM.getPage())*20; //TODO: volendo il 20 si può sostituire con una costante definita altrove
                    for(int i = start_index; i < res.size(); i++){
                        postList.add(res.get(i));
                    }
                    pa.notifyItemRangeInserted(initial_size, postList.size());
                }
            } else {
                //TODO: codice relativo + errore
                //binding.progressBar.setVisibility(View.GONE);
            }
        });
    }
}