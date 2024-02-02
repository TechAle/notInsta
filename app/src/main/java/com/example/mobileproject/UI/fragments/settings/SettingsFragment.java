package com.example.mobileproject.UI.fragments.settings;

import static com.example.mobileproject.utils.Constants.PICK_IMAGE_REQUEST;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import com.example.mobileproject.R;
import com.example.mobileproject.UI.activities.LoginActivity;
import com.example.mobileproject.ViewModels.Users.UsersVMFactory;
import com.example.mobileproject.ViewModels.Users.UsersViewModel;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.utils.FragmentUtils;
import com.example.mobileproject.utils.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private NavController ctrl;

    private View changeUsernameButton;
    private View changePasswordButton;

    private Spinner languagesSpinner;
    private Button signOutButton;
    private Button deleteAccountButton;
    private static final String PREF_SELECTED_LANGUAGE = "selected_language";
    private boolean firstSelected = true;

    private UsersViewModel PVM;

    private Button changeImageButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctrl = NavHostFragment.findNavController(this);

        UserRepository pr = ServiceLocator.getInstance().getUserRepo();
        if (pr != null) {
            PVM = new ViewModelProvider(requireActivity(), new UsersVMFactory(pr)).get(UsersViewModel.class);
        }

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String selectedLanguage = sharedPref.getString(PREF_SELECTED_LANGUAGE, "en");
        FragmentUtils.loadLanguage(selectedLanguage, getActivity(), getResources());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        changeUsernameButton = view.findViewById(R.id.changeUsernameText);
        changePasswordButton = view.findViewById(R.id.changePasswordText);
        changeImageButton = view.findViewById(R.id.changePictureButton);
        signOutButton = view.findViewById(R.id.signOutButton);
        deleteAccountButton = view.findViewById(R.id.DeleteAccountButton);

        languagesSpinner = view.findViewById(R.id.languagesSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.languages_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languagesSpinner.setAdapter(adapter);

        languagesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!firstSelected)
                    switch (position) {
                        case 0:
                            FragmentUtils.loadLanguage("en", getActivity(), getResources());
                            break;
                        case 1:
                            FragmentUtils.loadLanguage("it", getActivity(), getResources());
                            break;
                    }
                firstSelected = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        changeUsernameButton.setOnClickListener(view1 ->
                ctrl.navigate(R.id.action_settingsFragment_to_changeUsernameFragment)
        );

        changePasswordButton.setOnClickListener(view1 ->
                ctrl.navigate(R.id.action_settingsFragment_to_changePasswordFragment)
        );

        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PVM.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("logout_message", "You have logged out successfully");
                startActivity(intent);
                getActivity().finish();
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PVM.deleteAccount();
            }
        });

        return view;


    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            PVM.changeImage(selectedImageUri);
        }
    }
}