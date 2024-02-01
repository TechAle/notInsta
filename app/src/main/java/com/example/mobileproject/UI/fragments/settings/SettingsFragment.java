package com.example.mobileproject.UI.fragments.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.mobileproject.R;
import com.example.mobileproject.UI.activities.HomeActivity;
import com.example.mobileproject.UI.activities.LoginActivity;
import com.example.mobileproject.UI.fragments.settings.ChangePasswordFragment;
import com.example.mobileproject.UI.fragments.settings.ChangeUsernameFragment;
import com.example.mobileproject.ViewModels.Settings.SettingsViewModel;
import com.example.mobileproject.utils.FragmentUtils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private NavController ctrl;
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
    private static final String PREF_SELECTED_LANGUAGE = "selected_language";
    private boolean firstSelected = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        ctrl = NavHostFragment.findNavController(this);

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