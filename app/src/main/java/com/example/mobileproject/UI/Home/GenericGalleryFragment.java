package com.example.mobileproject.UI.Home;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.databinding.FragmentPostGalleryBinding;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.PostAdapter;
import com.example.mobileproject.utils.Result;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public abstract class GenericGalleryFragment extends Fragment {
    protected FragmentPostGalleryBinding binding;
    protected List<Post> postList;
    protected PostAdapter pa;
    protected PostsViewModel PVM;
    protected String user;
    protected int itemLoaded;
    /*public PostGalleryFragment() {
        // Required empty public constructor
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PostRepository pr = ServiceLocator.getInstance().getPostRepo(requireActivity().getApplication());
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
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        RecyclerView posts = binding.gallery;
        StaggeredGridLayoutManager lmp = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        pa = new PostAdapter(postList, requireActivity().getApplication(), new PostAdapter.OnItemClickListener() {
            //Qua non metto una funzione anonima
            @Override
            public void onItemClicked() {
                //Volendo si può sostituire questa linea con qualcosa di altro
                Snackbar.make(v, "Item Clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
        posts.setLayoutManager(lmp);
        posts.setAdapter(pa);

        FetchAction(v);

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
        });
    }

    private boolean internetConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return nc != null
                && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    protected abstract void FetchAction(View v);
}
