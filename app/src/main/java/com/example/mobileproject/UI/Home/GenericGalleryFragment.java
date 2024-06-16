package com.example.mobileproject.UI.Home;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.mobileproject.R;
import com.example.mobileproject.UI.ShowPostActivity;
import com.example.mobileproject.dataLayer.repositories.PostManager;
import com.example.mobileproject.databinding.FragmentPostGalleryBinding;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.PostAdapter;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public abstract class GenericGalleryFragment extends Fragment {
    protected FragmentPostGalleryBinding binding;
    protected PostsViewModel.FragmentType type;
    protected List<Post> postList;
    protected PostAdapter pa;
    protected PostsViewModel PVM;
    protected String user;
    protected int itemLoaded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PostManager pr = ServiceLocator.getInstance().getPostRepo(requireActivity().getApplication());
        if(pr != null){
            PVM = new ViewModelProvider(requireActivity()).get(PostsViewModel.class);

        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.unexpected_error), Snackbar.LENGTH_SHORT).show();
        }
        postList = new ArrayList<>();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
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
        pa = new PostAdapter(postList, requireActivity().getApplication(), p -> {
            Intent i = new Intent(requireActivity(), ShowPostActivity.class);
            i.putExtra("post", p);
            startActivity(i);
        });
        posts.setLayoutManager(lmp);
        posts.setAdapter(pa);
        fetchAction(v);
        posts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (internetConnection()                //la connessione deve esserci, altrimenti nulla avrebbe senso
                        && !PVM.areAllPosts(type)) //se non sono tutti i risultati
                {
                    itemLoaded = lmp.getItemCount();
                    int pastVisiblesItems = Arrays.stream(lmp.findLastVisibleItemPositions(new int[2])).sum();

                    int visibleItemCount = lmp.getChildCount();

                    // Condition to enable the loading of other news while the user is scrolling the list
                    if (!PVM.isLoading(type)
                            && !PVM.areAllPosts(type)
                            && (itemLoaded == visibleItemCount
                               || (itemLoaded <= (pastVisiblesItems))
                               && dy > 0)
                    ) {
                        PVM.setLoading(type, true);
                        postList.add(null);
                        pa.notifyItemRangeInserted(postList.size(), postList.size() + 1);
                        PVM.setPage(type,PVM.getPage(type) + 1); //"giro" la pagina
                        findAction(); //Inizio ad andare a prendere altri post
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

    protected abstract void fetchAction(View v);
    protected abstract void findAction();
}
