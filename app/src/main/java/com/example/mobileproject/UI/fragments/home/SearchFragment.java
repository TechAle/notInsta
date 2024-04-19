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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.mobileproject.R;
import com.example.mobileproject.ViewModels.Posts.PostsVMFactory;
import com.example.mobileproject.ViewModels.Posts.PostsViewModel;
import com.example.mobileproject.ViewModels.Users.UsersVMFactory;
import com.example.mobileproject.ViewModels.Users.UsersViewModel;
import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.databinding.FragmentSearchBinding;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.FragmentUtils;
import com.example.mobileproject.utils.PostAdapter;
import com.example.mobileproject.utils.Result;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private PostsViewModel PVM;
    private UsersViewModel PSM;
    private PostAdapter pa;
    private List<Post> postList;

    public SearchFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PostRepository pr = ServiceLocator.getInstance().getPostRepo();
        if(pr != null){
            PVM = new ViewModelProvider(requireActivity()).get(PostsViewModel.class); //Prendo quello dell'Activity
        }
        postList = new ArrayList<>();
        UserRepository ps = ServiceLocator.getInstance().getUserRepo(requireActivity().getApplication());

        if(pr != null){
            PSM = new ViewModelProvider(requireActivity(), new UsersVMFactory(ps)).get(UsersViewModel.class);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        PVM.setCurrentFragment(1);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout ll = view.findViewById(R.id.sponsorLayout);
        ll.setVisibility(View.GONE);
        SearchGalleryFragment f = binding.fragmentContainerView3.getFragment();
        binding.searchButton.setOnClickListener(c -> {
            f.loadPosts(binding.inputSearch.getText().toString());
        });
        /*posts.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                     /*   MutableLiveData<Result> m = PVM.getActualPosts();

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
        PVM.getSponsodedPosts(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), post -> {
            if(post.successful()) {
                PostResp sponsoredPost = ((Result.PostResponseSuccess) post).getData();
                List<Post> sponsored = sponsoredPost.getPostList();
                Post finalPost = sponsored.get((int) (Math.random()*sponsored.size()));
                if (finalPost.getImage() == null)
                    FragmentUtils.loadImage(storageRef, "POSTS/" + finalPost.getId() + ".png", view, R.id.sponsorImage);
                else
                    FragmentUtils.loadImage(finalPost.getImage(), view, R.id.sponsorImage);
                FragmentUtils.updateTextById(view, R.id.sponsorText, "Sponsor: " + finalPost.getDescrizione());
                t.setVisibility(View.VISIBLE);
                PVM.getPosts().removeObservers(getViewLifecycleOwner());
            }});*/
    }
}