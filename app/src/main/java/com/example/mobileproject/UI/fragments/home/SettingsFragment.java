package com.example.mobileproject.UI.fragments.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.mobileproject.R;
import com.example.mobileproject.UI.activities.HomeActivity;
import com.example.mobileproject.UI.activities.LoginActivity;
import com.example.mobileproject.UI.fragments.settings.ChangePasswordFragment;
import com.example.mobileproject.UI.fragments.settings.ChangeUsernameFragment;
import com.example.mobileproject.models.SettingsViewModel;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
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


        changeUsernameButton = view.findViewById(R.id.changeUsernameText);
        changePasswordButton = view.findViewById(R.id.changePasswordText);
        languagesSpinner = view.findViewById(R.id.languagesSpinner);
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
                        .replace(R.id.fragment_window_host, ChangeUsernameFragment.class, null)
                        .commit();

            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.fragment_window_host, ChangePasswordFragment.class, null)
                        .commit();
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