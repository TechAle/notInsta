package com.example.mobileproject.UI.Home;

import android.view.View;

import static com.example.mobileproject.utils.Constants.ELEMENTS_LAZY_LOADING;

public final class SearchGalleryFragment extends GenericGalleryFragment {
    public SearchGalleryFragment() {
        super();
        type = PostsViewModel.FragmentType.FOUND;
    }

    @Override
    protected void fetchAction(View v) {
        binding.progressBar.setVisibility(View.VISIBLE);
        PVM.getActualFoundPosts().observe(getViewLifecycleOwner(), list -> {
            /*if(list == null){
                return;
            }*/
            if (!PVM.isLoading(type)) { //Se non è attiva una chiamata
                if (PVM.isFirstLoading(type)) { //Se è il primo caricamento -> lista interna vuota o aggiornamento completo
                    PVM.setFirstLoading(type, false);
                    int lastSize = postList.size();
                    postList.clear();
                    pa.notifyItemRangeRemoved(0, lastSize);
                    postList.addAll(list);
                    pa.notifyItemRangeInserted(0, postList.size());
                    if (postList.size() < ELEMENTS_LAZY_LOADING) { // Ho meno post di quelli possibili, allora non ne ho altri. Coincide con quelli ottenuti
                        PVM.setAllPosts(type, true);
                    }
                } else {// Caricamento iniziale
                    postList.clear();
                    postList.addAll(list);
                    pa.notifyItemRangeInserted(0, list.size());
                    if (postList.size() < ELEMENTS_LAZY_LOADING) { // Ho meno post di quelli possibili, allora non ne ho altri
                        PVM.setAllPosts(type, true);
                    }
                }
                binding.progressBar.setVisibility(View.GONE);
            } else {
                PVM.setLoading(type, false);
                int initial_size = postList.size();
                for (int i = 0; i < postList.size(); i++) {
                    if (postList.get(i) == null) {
                        postList.remove(null);
                    }
                }
                int itemInserted = 0;
                int sz = list.size();
                for (int i = initial_size - 1; i < sz; i++) {
                    postList.add(list.get(i));
                    itemInserted++;
                }
                if (itemInserted < ELEMENTS_LAZY_LOADING) {
                    PVM.setAllPosts(type, true);
                }
                if (itemInserted == 0) {
                    pa.notifyItemRangeRemoved(initial_size, initial_size - 1);
                } else if (sz == 1) {
                    pa.notifyItemChanged(initial_size);
                } else {
                    pa.notifyItemRangeInserted(initial_size, postList.size());
                }
            }
        });
    }

    @Override
    protected void findAction() {
        PVM.findFoundPosts();
    }

    void loadPosts(String s) {
        PVM.setSearchTag(s);
        findAction();
    }
}