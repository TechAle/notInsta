package com.example.mobileproject.UI.Home;

import androidx.fragment.app.Fragment;

import android.view.View;

import static com.example.mobileproject.utils.Constants.ELEMENTS_LAZY_LOADING;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public final class PostGalleryFragment extends GenericGalleryFragment{
    public PostGalleryFragment() {
        super();
        type = PostsViewModel.FragmentType.GLOBAL;
    }
    public static PostGalleryFragment newInstance() {
        PostGalleryFragment fragment = new PostGalleryFragment();
        return fragment;
    }
    @Override
    protected void fetchAction(View v) {
        binding.progressBar.setVisibility(View.VISIBLE);
        PVM.getGlobalPosts().observe(getViewLifecycleOwner(), list -> {
            if (!PVM.isLoading(type)) { //Se non è attiva una chiamata
                if (PVM.isFirstLoading(type)) { //Se è il primo caricamento -> lista interna vuota o aggiornamento completo richiesto
                    PVM.setFirstLoading(type, false);
                    int lastSize = postList.size();
                    postList.clear();
                    pa.notifyItemRangeRemoved(0, lastSize);
                    postList.addAll(list);
                    pa.notifyItemRangeInserted(0, postList.size());
                    if (postList.size() < ELEMENTS_LAZY_LOADING) { // Ho meno post di quelli possibili, allora non ne ho altri
                        PVM.setAllPosts(type, true);
                    }
                } else {// ???
                    postList.clear();
                    postList.addAll(list);
                    pa.notifyItemRangeInserted(0, list.size());
                    if (postList.size() < ELEMENTS_LAZY_LOADING) { // Ho meno post di quelli possibili, allora non ne ho altri
                        PVM.setAllPosts(type, true);
                    }
                }
                binding.progressBar.setVisibility(View.GONE);
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
                    PVM.setAllPosts(type,true);
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
        PVM.findGlobalPosts();
    }
}