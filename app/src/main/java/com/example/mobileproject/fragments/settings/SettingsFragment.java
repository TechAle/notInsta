package com.example.mobileproject.fragments.settings;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.mobileproject.R;

import com.example.mobileproject.activities.HomeActivity;
import com.example.mobileproject.activities.LoginActivity;
import com.example.mobileproject.models.SettingsViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private View backButton;
    private View changeUsernameButton;
    private View changePasswordButton;
    private Switch notifiesSwitch;
    private Switch privateAccountSwitch;
    private Spinner languagesSpinner;
    private Switch showLikesSwitch;
    private Button signOutButton;
    private Button deleteAccountButton;
    private SettingsViewModel settingsViewModel;


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings_main, container, false);


        backButton = view.findViewById(R.id.backButton);
        changeUsernameButton = view.findViewById(R.id.changeUsernameText);
        changePasswordButton = view.findViewById(R.id.changePasswordText);
        notifiesSwitch = view.findViewById(R.id.notifiesSwitch);
        privateAccountSwitch = view.findViewById(R.id.privateAccountSwitch);
        languagesSpinner = view.findViewById(R.id.languagesSpinner);
        showLikesSwitch = view.findViewById(R.id.showLikesSwitch);
        signOutButton = view.findViewById(R.id.signOutButton);
        deleteAccountButton = view.findViewById(R.id.DeleteAccountButton);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();
            }
        });

        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view_impostazioni, ChangeUsernameFragment.class, null)
                        .commit();

            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view_impostazioni, ChangePasswordFragment.class, null)
                        .commit();
            }
        });

        notifiesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        privateAccountSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        showLikesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("logout_message", "You have logged out successfully");
                startActivity(intent);
                getActivity().finish();
                FirebaseAuth.getInstance().signOut();
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return view;


    }


}