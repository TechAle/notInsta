package com.example.mobileproject.UI.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.databinding.FragmentProfileBinding;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.utils.PostAdapter;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
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
    private Users current_user;*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PostRepository pr = ServiceLocator.getInstance().getPostRepo(requireActivity().getApplication());
        if(pr != null){
            PVM = new ViewModelProvider(requireActivity()).get(PostsViewModel.class);

        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "Unexpected Error", Snackbar.LENGTH_SHORT).show();
        }
        postList = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Users user = PVM.getUser();
        binding.Nome.setText(user.getNome());
        binding.username.setText(user.getUsername());
        binding.descrizione.setText(user.getDescrizione());
        binding.seguaci.setText(/*R.string.followers*/"Followers:" +
                (user.getFollowers() == null ? "0" : user.getFollowers().size())); //TODO: sistemare questa linea
        binding.seguiti.setText(/*R.string.followers*/"Followee:" +
                (user.getFollowing() == null ? "0" : user.getFollowing().size())); //TODO: sistemare questa linea
        Glide.with(this).load(user.getImageUri()).into(binding.pfp);
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        PVM.setCurrentFragment(2);
        return binding.getRoot();
    }
}