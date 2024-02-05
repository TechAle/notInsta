package com.example.mobileproject.UI.fragments.home;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileproject.R;
import com.example.mobileproject.ViewModels.Posts.PostsVMFactory;
import com.example.mobileproject.ViewModels.Posts.PostsViewModel;
import com.example.mobileproject.ViewModels.Users.UsersVMFactory;
import com.example.mobileproject.ViewModels.Users.UsersViewModel;
import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.databinding.FragmentPostGalleryBinding;
import com.example.mobileproject.databinding.FragmentStartingBinding;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.PostAdapter;
import com.example.mobileproject.utils.Result;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostGalleryFragment extends Fragment {

    private FragmentPostGalleryBinding binding;
    private List<Post> postList;
    private PostAdapter pa;
    private PostsViewModel PVM;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String user;
    private int itemLoaded;

    public PostGalleryFragment() {
        // Required empty public constructor
    }
/*
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tag Parameter 1.
     * @param user Parameter 2.
     * @return A new instance of fragment PostGalleryFragment.
     */
    public static PostGalleryFragment newInstance(/*String tag, String user*/) {
        PostGalleryFragment fragment = new PostGalleryFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, tag);
        args.putString(ARG_PARAM2, user);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceLocator sl = ServiceLocator.getInstance();
        PostRepository pr = ServiceLocator.getInstance().getPostRepo();
        if(pr != null){
            PVM = new ViewModelProvider(requireActivity()).get(PostsViewModel.class);

        } else { //TODO: Sostituire testo con una risorsa
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "Unexpected Error", Snackbar.LENGTH_SHORT).show();
        }
        postList = new ArrayList<>();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentPostGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState){
        super.onViewCreated(v, savedInstanceState);
        itemLoaded = -1;

        RecyclerView posts = binding.gallery;
        StaggeredGridLayoutManager lmp = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        pa = new PostAdapter(postList, requireActivity().getApplication(), new PostAdapter.OnItemClickListener(){
            //Qua non metto una funzione anonima
            @Override
            public void onItemClicked() {
                //Volendo si può sostituire questa linea con qualcosa di altro
                Snackbar.make(v, "Item Clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
        posts.setLayoutManager(lmp);
        posts.setAdapter(pa);
        //binding.progressBar.setVisibility(View.VISIBLE);
        // progressbar non inserita, ma probabilmente all'interno del fragment assieme alla recyclerView

        /*PVM.getPosts().observe(getViewLifecycleOwner(), result -> { //Listener
            if(result.successful()){//Successo nel recuperare i dati
                PostResp resp = ((Result.PostResponseSuccess) result).getData();
                List<Post> res = resp.getPostList();

                if(!PVM.isLoading()){ //Se non è attiva una chiamata
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
                        if(postList.size() < 20){
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
                    int start_index = (PVM.getPage())*20; //TODO: volendo il 20 si può sostituire con una costante definita altrove
                    for(int i = start_index; i < res.size(); i++){
                        postList.add(res.get(i));
                    }
                    pa.notifyItemRangeInserted(initial_size, postList.size());
                    if(res.size() < 20){
                        PVM.setAllPosts(true);
                    }
                }
            } else {
                //TODO: codice relativo + errore
                //binding.progressBar.setVisibility(View.GONE);
            }
        });

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
                    if (itemLoaded == visibleItemCount
                            || (itemLoaded <= (pastVisiblesItems + 2)
                                && dy > 0
                                && !PVM.isLoading())
                            && PVM.getPosts().getValue() != null
                            && !PVM.areAllPosts())
                    {
                        MutableLiveData<Result> m = PVM.getActualPosts();

                        if (m.getValue() != null && m.getValue().successful()) {
                            PVM.setLoading(true);
                            postList.add(null);
                            pa.notifyItemRangeInserted(postList.size(),postList.size() + 1);
                            PVM.setPage(PVM.getPage() + 1); //"giro" la pagina
                            //Inizio ad andare a prendere altri post
                            PVM.findPosts();//TODO: Sistemare qua
                        }
                    }
                }
            }
        });*/
    }
    private boolean internetConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return nc != null
                && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }
}