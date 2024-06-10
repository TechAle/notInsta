package com.example.mobileproject.UI.Home;

import android.view.View;

import static com.example.mobileproject.utils.Constants.ELEMENTS_LAZY_LOADING;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public final class ProfileGalleryFragment extends GenericGalleryFragment {
    public ProfileGalleryFragment() {
        super();
        type = PostsViewModel.FragmentType.OWNED;
    }

    @NonNull
    @Contract(" -> new")
    public static ProfileGalleryFragment newInstance() {
        return new ProfileGalleryFragment();
    }

    @Override
    protected void fetchAction(View v) {
        PVM.getOwnedPosts().observe(getViewLifecycleOwner(), list -> {
            if (!PVM.isLoading(type)) { //Se non è attiva una chiamata
                if (PVM.isFirstLoading(type)) { //Se è il primo caricamento -> lista interna vuota o aggiornamento completo
                    PVM.setFirstLoading(type, false);
                    int lastSize = postList.size();
                    postList.clear();
                    pa.notifyItemRangeRemoved(0, lastSize);
                    postList.addAll(list);
                    pa.notifyItemRangeInserted(0, postList.size());
                    if (postList.size() < ELEMENTS_LAZY_LOADING) { // Ho meno post di quelli possibili, allora non ne ho altri
                        PVM.setAllPosts(type, true);
                    }
                } else {//Caricamento da viewModel alla ricostruzione del fragment. Non effettua altre chiamate remote
                    postList.clear();
                    postList.addAll(list);
                    pa.notifyItemRangeInserted(0, list.size());
                    if (postList.size() < ELEMENTS_LAZY_LOADING) { // Ho meno post di quelli possibili, allora non ne ho altri
                        PVM.setAllPosts(type, true);
                    }
                }
                //bindings
            } else {
                PVM.setLoading(type, false); //rendo disponibile un altra chiamata
                int initial_size = postList.size();
                for (int i = 0; i < postList.size(); i++) {
                    if (postList.get(i) == null) {
                        postList.remove(null);
                    }
                }
                int itemInserted = 0;
                int s = list.size();
                for (int i = initial_size - 1; i < s; i++) {
                    postList.add(list.get(i));
                    itemInserted++;
                }
                if (itemInserted < ELEMENTS_LAZY_LOADING) {
                    PVM.setAllPosts(type, true);
                }
                if (itemInserted == 0) {
                    pa.notifyItemRangeRemoved(initial_size, initial_size - 1);
                } else if (itemInserted == 1) {
                    pa.notifyItemChanged(initial_size);
                } else {
                    pa.notifyItemRangeInserted(initial_size, postList.size());
                }
            }
        });
    }

    @Override
    protected void findAction() {
        PVM.findOwnedPosts();
    }
}