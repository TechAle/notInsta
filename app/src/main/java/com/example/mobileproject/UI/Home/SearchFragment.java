package com.example.mobileproject.UI.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.mobileproject.R;
import com.example.mobileproject.databinding.FragmentSearchBinding;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.Result;
import com.example.mobileproject.models.Post.Post;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private PostsViewModel PVM;

    public SearchFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PVM = new ViewModelProvider(requireActivity()).get(PostsViewModel.class); //Prendo quello dell'Activity
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
        binding.sponsorLayout.setVisibility(View.GONE);
        SearchGalleryFragment f = binding.fragmentContainerView3.getFragment();
        binding.searchButton.setOnClickListener(c -> {
            f.loadPosts(binding.inputSearch.getText().toString());
        });
        PVM.getSponsoredPosts().observe(getViewLifecycleOwner(), post -> {
            if(post.successful()) {
                PostResp sponsoredPost = ((Result.PostResponseSuccess) post).getData();
                List<Post> sponsored = sponsoredPost.getPostList();
                Post finalPost = sponsored.get((int) (Math.random()*sponsored.size()));
                Glide.with(this).load(finalPost.getImage()).placeholder(R.drawable.ic_launcher_foreground).into(binding.sponsorImage);
                binding.sponsorText.setText(finalPost.getAutore());
                binding.sponsorLayout.setVisibility(View.VISIBLE);
                /*if (finalPost.getImage() == null)
                    //FragmentUtils.loadImage(storageRef, "POSTS/" + finalPost.getId() + ".png", view, R.id.sponsorImage);
                else
                    FragmentUtils.loadImage(finalPost.getImage(), view, R.id.sponsorImage);
                FragmentUtils.updateTextById(view, R.id.sponsorText, "Sponsor: " + finalPost.getDescrizione());
                t.setVisibility(View.VISIBLE);*/
            }});
    }
}