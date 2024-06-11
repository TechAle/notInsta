package com.example.mobileproject.UI.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
//import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.mobileproject.R;
import com.example.mobileproject.databinding.FragmentSearchBinding;
/*import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.Result;
import com.example.mobileproject.models.Post.Post;

import java.util.List;*/

/**
 * A simple {@link Fragment} subclass.
 */
public final class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private PostsViewModel PVM;

    public SearchFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PVM = new ViewModelProvider(requireActivity()).get(PostsViewModel.class);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
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
            if(post != null){
                Glide.with(this).load(post.getImage()).placeholder(R.drawable.ic_launcher_foreground).into(binding.sponsorImage);
                binding.sponsorText.setText(post.getAutore());
                binding.sponsorLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}