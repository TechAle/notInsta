package com.example.mobileproject.UI.Home;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileproject.R;
import com.example.mobileproject.UI.Settings.SettingsActivity;
import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.databinding.FragmentStartingBinding;
import com.example.mobileproject.utils.ServiceLocator;
import com.example.mobileproject.utils.TagsAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public final class StartingFragment extends Fragment {

    private FragmentStartingBinding binding;
    private PostsViewModel PVM;
    //private int itemLoaded;
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
        PostRepository pr = ServiceLocator.getInstance().getPostRepo(requireActivity().getApplication());
        if(pr != null){
            PVM = new ViewModelProvider(requireActivity()).get(PostsViewModel.class);
        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "Unexpected Error", Snackbar.LENGTH_SHORT).show();
        }
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
        /*
        RecyclerView tags = binding.tags;
        RecyclerView.LayoutManager lmt = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);
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
        tags.setAdapter(ta);*/
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