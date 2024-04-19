package com.example.mobileproject.UI.fragments.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.ImageView;


import com.example.mobileproject.R;/*
import com.example.mobileproject.ViewModels.Posts.PostsViewModel;
import com.example.mobileproject.ViewModels.Users.UsersVMFactory;
import com.example.mobileproject.ViewModels.Users.UsersViewModel;
import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.models.Users.UsersResp;
import com.example.mobileproject.utils.FragmentUtils;
import com.example.mobileproject.utils.Result;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;

import java.util.List;
*/
import com.example.mobileproject.ViewModels.Posts.PostsViewModel;
import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.databinding.FragmentProfileBinding;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.PostAdapter;
import com.example.mobileproject.utils.Result;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private List<Post> postList;
    private PostAdapter pa;
    private PostsViewModel PVM;
    private int itemLoaded;
    public ProfileFragment() {
        // Required empty public constructor
        /*PostRepository t = ServiceLocator.getInstance().getPostRepo();
        t.retrievePosts();*/
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }
    /*
    private PostsViewModel ref_underlying_fragment;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Users current_user;*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PostRepository pr = ServiceLocator.getInstance().getPostRepo();
        if(pr != null){
            PVM = new ViewModelProvider(requireActivity()).get(PostsViewModel.class);

        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "Unexpected Error", Snackbar.LENGTH_SHORT).show();
        }
        postList = new ArrayList<>();
        //storageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*RecyclerView posts = binding.GalleryProfile;
        StaggeredGridLayoutManager lmp = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        pa = new PostAdapter(postList, requireActivity().getApplication(), new PostAdapter.OnItemClickListener() {
            //Qua non metto una funzione anonima
            @Override
            public void onItemClicked() {
                //Volendo si può sostituire questa linea con qualcosa di altro
                Snackbar.make(view, "Item Clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
        posts.setLayoutManager(lmp);
        posts.setAdapter(pa);

        PVM.getPosts().observe(getViewLifecycleOwner(), result -> {
            if(result.successful()){//Successo nel recuperare i dati
                PostResp resp = ((Result.PostResponseSuccess) result).getData();
                List<Post> res = resp.getPostList();
                if(!PVM.isLoading()){ //Se non è attiva una chiamata
                    /*if(res.size() == 0){
                        PVM.setAllPosts(true);
                        postList.remove(null);
                        return;
                    }*//*
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
                    }*//*
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
        });*//*
        posts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (internetConnection()                //la connessione deve esserci, altrimenti nulla avrebbe senso
                        && !PVM.areAllPosts()) //se non sono tutti i risultati
                {
                    itemLoaded = lmp.getItemCount();
                    int pastVisiblesItems = Arrays.stream(lmp.findLastVisibleItemPositions(new int[2])).sum();

                    int visibleItemCount = lmp.getChildCount();

                    // Condition to enable the loading of other news while the user is scrolling the list
                    if (!PVM.isLoading()
                            && !PVM.areAllPosts()
                            && (itemLoaded == visibleItemCount
                            || (itemLoaded <= (pastVisiblesItems))
                            && dy > 0)
                            && PVM.getActualPosts().getValue() != null
                    ) {
                    /*if (itemLoaded == visibleItemCount
                            || (itemLoaded <= (pastVisiblesItems + 2)
                                && dy > 0
                                && !PVM.isLoading())
                            && PVM.getActualPosts().getValue() != null
                            && !PVM.areAllPosts())
                    {*/
                      /*  MutableLiveData<Result> m = PVM.getActualPosts();

                        if (m.getValue() != null && m.getValue().successful()) {
                            PVM.setLoading(true);
                            postList.add(null);
                            pa.notifyItemRangeInserted(postList.size(), postList.size() + 1);
                            PVM.setPage(PVM.getPage() + 1); //"giro" la pagina
                            //Inizio ad andare a prendere altri post
                            PVM.findPosts();//TODO: Sistemare qua
                        }
                    }
                }
            }
        });*/

        /*
        //TODO: salvare i dati dell'utente loggato
        PVM.getcurrentUser().observe(getViewLifecycleOwner(), result -> {
            if(result.successful()) {
                UsersResp resp = ((Result.UserResponseSuccess) result).getData();
                List<Users> res = resp.getUsersList();
                Users target = res.get(0);
                FragmentUtils.loadImage(storageRef, "PFP/" + target.getId() + ".png", view, R.id.pfp);

                FragmentUtils.updateTextById(view, R.id.Nome, target.getNome());
                FragmentUtils.updateTextById(view, R.id.username, "@" + target.getUsername());
                FragmentUtils.updateTextById(view, R.id.descrizione, target.getDescrizione());
                FragmentUtils.updateTextById(view, R.id.seguaci, "Seguaci: " + target.getFollowers().size());
                FragmentUtils.updateTextById(view, R.id.seguiti, "Seguiti: " + target.getFollowing().size());
                onLoadComplete(target.getId());
            }
        });*/
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        PVM.setCurrentFragment(2);
        return binding.getRoot();
    }
/*
    public void onLoadComplete(String id){
        ref_underlying_fragment.setIdUser(id);
    }*/
}