package com.example.mobileproject.UI.fragments.home;

import android.view.View;

import androidx.lifecycle.MutableLiveData;

import static com.example.mobileproject.utils.Constants.ELEMENTS_LAZY_LOADING;
import com.example.mobileproject.R;
import com.example.mobileproject.databinding.FragmentPostGalleryBinding;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.Result;

import java.util.List;

public final class ProfileGalleryFragment extends GenericGalleryFragment{
    public ProfileGalleryFragment() {}
    public static ProfileGalleryFragment newInstance() {
        return new ProfileGalleryFragment();
    }

    //TODO: sistemare questa funzione
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
                    int start_index = (PVM.getPage())*ELEMENTS_LAZY_LOADING;
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