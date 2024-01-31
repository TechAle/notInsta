package com.example.mobileproject.UI.fragments.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import com.example.mobileproject.models.Users.UsersResp;
import com.example.mobileproject.utils.FragmentUtils;
import com.example.mobileproject.utils.Result;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    private PostsViewModel PVM;
    private UsersViewModel PSM;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PostRepository pr = ServiceLocator.getInstance().getPostRepo();
        if(pr != null){
            PVM = new ViewModelProvider(requireActivity(), new PostsVMFactory(pr)).get(PostsViewModel.class);
        }

        UserRepository ps = ServiceLocator.getInstance().getUserRepo();
        if(pr != null){
            PSM = new ViewModelProvider(requireActivity(), new UsersVMFactory(ps)).get(UsersViewModel.class);
        }

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout t = view.findViewById(R.id.sponsorLayout);
        t.setVisibility(View.GONE);

        PVM.getSponsodedPosts().observe(getViewLifecycleOwner(), post -> {
            if(post.successful()) {
                PostResp sponsoredPost = ((Result.PostResponseSuccess) post).getData();
                List<Post> sponsored = sponsoredPost.getPostList();
                Post finalPost = sponsored.get(0);
                FragmentUtils.loadImage(storageRef, "POSTS/" + finalPost.getId() + ".png", view, R.id.sponsorImage);
                FragmentUtils.updateTextById(view, R.id.sponsorText, "Sponsor: " + finalPost.getDescrizione());
                t.setVisibility(View.VISIBLE);
                PVM.getPosts().removeObservers(getViewLifecycleOwner());
            }});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}