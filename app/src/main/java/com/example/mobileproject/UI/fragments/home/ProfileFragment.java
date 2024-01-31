package com.example.mobileproject.UI.fragments.home;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mobileproject.R;
import com.example.mobileproject.ViewModels.Users.UsersVMFactory;
import com.example.mobileproject.ViewModels.Users.UsersViewModel;
import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.models.Users.UsersResp;
import com.example.mobileproject.utils.FragmentUtils;
import com.example.mobileproject.utils.Result;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
        /*PostRepository t = ServiceLocator.getInstance().getPostRepo();
        t.retrievePosts();*/

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private UsersViewModel PVM;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserRepository pr = ServiceLocator.getInstance().getUserRepo();
        if(pr != null){
            PVM = new ViewModelProvider(requireActivity(), new UsersVMFactory(pr)).get(UsersViewModel.class);
        }

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String id = "0TsbiPUaL5qfFQiH6572";

        PVM.getUserById(id).observe(getViewLifecycleOwner(), result -> {
            if(result.successful()) {
                UsersResp resp = ((Result.UserResponseSuccess) result).getData();
                List<Users> res = resp.getUsersList();
                Users target = res.get(0);
                FragmentUtils.loadImage(storageRef, "PFP/" + target.getId() + ".png", view, R.id.pfp);

                FragmentUtils.updateTextById(view, R.id.Nome, target.getNome());
                FragmentUtils.updateTextById(view, R.id.username, "@" + target.getUsername());
                FragmentUtils.updateTextById(view, R.id.descrizione, target.getDescrizione());
                FragmentUtils.updateTextById(view, R.id.seguaci, "Seguaci: " + target.getFollowers().size());
                FragmentUtils.updateTextById(view, R.id.seguiti, "Seguiti: " + target.getFollowing().size());
            }
        });
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}