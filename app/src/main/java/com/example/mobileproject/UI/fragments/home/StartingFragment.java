package com.example.mobileproject.UI.fragments.home;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mobileproject.R;
import com.example.mobileproject.ViewModels.Posts.PostsVMFactory;
import com.example.mobileproject.ViewModels.Posts.PostsViewModel;
import com.example.mobileproject.ViewModels.Users.UsersVMFactory;
import com.example.mobileproject.ViewModels.Users.UsersViewModel;
import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.utils.PostAdapter;
import com.example.mobileproject.utils.Result;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class StartingFragment extends Fragment {

    private List<Post> postSet;
    private PostsViewModel PVM;
    private UsersViewModel UVM;
    private int nTotalItem;
    private PostAdapter pa;
    public StartingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PostRepository pr = ServiceLocator.getInstance().getPostRepo();
        if(pr != null){
            PVM = new ViewModelProvider(requireActivity(), new PostsVMFactory(pr)).get(PostsViewModel.class);

        } else { //TODO: Sostituire testo con una risorsa
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "Unexpected Error", Snackbar.LENGTH_SHORT).show();
        }
        UserRepository ur = ServiceLocator.getInstance().getUserRepo();
        if (ur != null) {
            UVM = new ViewModelProvider(requireActivity(), new UsersVMFactory(ur)).get(UsersViewModel.class);
        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "Unexpected Error", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_starting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView tags = view.findViewById(R.id.tags);
        RecyclerView.LayoutManager lmt = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);
        RecyclerView posts = view.findViewById(R.id.posts);
        StaggeredGridLayoutManager lmp = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
//        ArrayAdapter<String> aas = new ArrayAdapter<>(requireContext(), R.layout.taglist_item);

        tags.setLayoutManager(lmt);
        posts.setLayoutManager(lmp);
        PVM.getPosts().observe(getViewLifecycleOwner(), result -> {
            if(result.successful()){
                PostResp resp = ((Result.PostResponseSuccess) result).getData();
                List<Post> res = resp.getPostList();
                if(!PVM.isLoading()){

                } else {
                    PVM.setLoading(false);
                    PVM.setTotalResults(postSet.size());
                    int initial_size = postSet.size();
                    for(int i = 0; i < postSet.size(); i++){
                        if(postSet.get(i) == null){
                            postSet.remove(null);//TODO: esaminare qua
                        }
                    }
                    int start_index = (PVM.getPage()-1)*20; //TODO: volendo il 20 si può sostituire con una costante
                }
                //TODO: codice relativo
            } else {
                //TODO: codice relativo
            }
        });
        posts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (internetConnection()                //la connessione deve esserci, altrimenti nulla avrebbe senso
                && nTotalItem != PVM.getTotalResults()) //se non sono tutti i risultati
                {
                    nTotalItem = lmp.getItemCount();
                    //TODO: controllare la seguente linea
                    int pastVisiblesItems = Arrays.stream(lmp.findLastVisibleItemPositions(new int[2])).sum();

                    int visibleItemCount = lmp.getChildCount();

                    // Condition to enable the loading of other news while the user is scrolling the list
                    if (nTotalItem == visibleItemCount
                    || (nTotalItem <= (pastVisiblesItems + 2)
                        && dy > 0
                        && !PVM.isLoading())
                    && PVM.getPosts().getValue() != null
                    && PVM.getCurrentResults() != PVM.getTotalResults())
                    {
                        MutableLiveData<Result> m = PVM.getPosts();

                        if (m.getValue() != null && m.getValue().successful()) {
                            PVM.setLoading(true);
                            postSet.add(null);
                            pa.notifyItemRangeInserted(postSet.size(),postSet.size() + 1);
                            PVM.setPage(PVM.getPage() + 1); //"giro" la pagina
                            //Inizio ad andare a prendere altrii post
                            //PVM.fetchPost();//TODO: Sistemare qua
                        }
                    }
                }
            }
        });


        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);*/
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();

            /*
            Post toCreate = new Post("0TsbiPUaL5qfFQiH6572", "Bella descrizione",
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
            });*/
        }
    }


    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Uri imageUri;

//TODO: controllare questa funzione. Se ha problemi, decommentare le linee commentate e aggiustare gli import
    private boolean internetConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // <----
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return nc != null
            && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        // ---->
        /*
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
        try{
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");
        } catch (Exception e){
            return false;
        }*/
    }
}