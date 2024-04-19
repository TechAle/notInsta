package com.example.mobileproject.UI.fragments.home;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileproject.R;
import com.example.mobileproject.UI.activities.SettingsActivity;
import com.example.mobileproject.ViewModels.Posts.PostsViewModel;
import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.databinding.FragmentStartingBinding;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.PostAdapter;
import com.example.mobileproject.utils.Result;
import com.example.mobileproject.utils.ServiceLocator;
import com.example.mobileproject.utils.TagsAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartingFragment extends Fragment {

    private FragmentStartingBinding binding;
    private List<Post> postList;
    private PostAdapter pa;
    private PostsViewModel PVM;
    private int itemLoaded;
    private List<String> arrayTags;
    private TagsAdapter ta;
    public StartingFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StartingFragment.
     */
    public static StartingFragment newInstance() {
        return new StartingFragment();
    }

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
        /*UserRepository ur = ServiceLocator.getInstance().getUserRepo(this.getActivity().getApplication());
        if (ur != null) {
            UVM = new ViewModelProvider(requireActivity(), new UsersVMFactory(ur)).get(UsersViewModel.class);
        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "Unexpected Error", Snackbar.LENGTH_SHORT).show();
        }*/

        //TODO: Sostituire questi metodi stub
        arrayTags = new ArrayList<>();
        arrayTags.add("Tag di prova");
        arrayTags.add("Tag 2");
        arrayTags.add("Tag 3");
        arrayTags.add("Tag 4");
        arrayTags.add("Tag 5");
        arrayTags.add("Tag 6");
        arrayTags.add("Tag 7");
        arrayTags.add("Impariamo a usare Github");
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentStartingBinding.inflate(inflater, container, false);
        PVM.setCurrentFragment(0);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Toolbar del fragment
        binding.toolbarStartingFragment.setTitle("!Insta");
        binding.toolbarStartingFragment.inflateMenu(R.menu.settings_menu);
        binding.toolbarStartingFragment.setOnMenuItemClickListener(item -> {
            int action = item.getItemId();
            if(action == R.id.settings_action){
                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);
                return true;
            }
            else {
                return false;
            }
        });

        /*RecyclerView posts = binding.GalleryStart;
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
        posts.setAdapter(pa);*/

        RecyclerView tags = binding.tags;
        RecyclerView.LayoutManager lmt = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);

        /*PVM.getPosts().observe(getViewLifecycleOwner(), result -> {
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
                    {*//*
                        MutableLiveData<Result> m = PVM.getActualPosts();

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
        ta = new TagsAdapter(arrayTags, new TagsAdapter.OnItemClickListener(){
            @Override
            public void onTagClicked(String s, boolean selected){
                //Snackbar.make(view, s, Snackbar.LENGTH_SHORT).show();
                if(selected){
                    PVM.addTag(s);
                } else {
                    PVM.removeTag(s);
                }
            }
        });
        tags.setLayoutManager(lmt);
        tags.setAdapter(ta);

        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);*/
    }
/*

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();


            /*Post toCreate = new Post("0TsbiPUaL5qfFQiH6572", "Bella descrizione",
                    new ArrayList<>(Arrays.asList("tag1", "ciao")), true, FirebaseFirestore.getInstance());

            PVM.createPost(toCreate).observe(getViewLifecycleOwner(), task -> {
                if (task.successful()) {
                    PVM.createImage(imageUri, "POSTS", requireActivity().getContentResolver(), ((Result.PostCreationSuccess) task).getData())
                            .observe(getViewLifecycleOwner(), imageTask -> {
                                if (imageTask.successful()) {
                                    int a = 0;
                                } else {
                                    int b = 0;
                                }

                    });
                } else {
                    int c = 0;
                }
            });*/
            /*
            Users toCreate = new Users("cognome", "nome", "username", "descrizione", Calendar.getInstance().getTime(), new ArrayList<>(Arrays.asList("lol")));
            UVM.createUser(toCreate).observe(getViewLifecycleOwner(), task -> {
                if (task.successful()) {
                    PVM.createImage(imageUri, "PFP", requireActivity().getContentResolver(), ((Result.UserCreationSuccess) task).getData())
                            .observe(getViewLifecycleOwner(), imageTask -> {
                                if (imageTask.successful()) {
                                    int a = 0;
                                } else {
                                    int b = 0;
                                }

                            });
                } else {
                    int c = 0;
                }
            });
        }
    }


    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Uri imageUri;
*/
    private boolean internetConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return nc != null
            && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }
}