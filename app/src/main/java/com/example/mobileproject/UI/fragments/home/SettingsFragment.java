package com.example.mobileproject.UI.fragments.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.mobileproject.R;
import com.example.mobileproject.UI.activities.LoginActivity;
import com.example.mobileproject.UI.fragments.settings.ChangePasswordFragment;
import com.example.mobileproject.UI.fragments.settings.ChangeUsernameFragment;
import com.example.mobileproject.ViewModels.Settings.SettingsViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private View changeUsernameButton;
    private View changePasswordButton;
    private Spinner languagesSpinner;
    private Button signOutButton;
    private Button deleteAccountButton;
    private SettingsViewModel settingsViewModel;
    private static final String PREF_SELECTED_LANGUAGE = "selected_language";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String selectedLanguage = sharedPref.getString(PREF_SELECTED_LANGUAGE, "en");
        setLocale(selectedLanguage);

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                switch (position) {
                    case 0:
                        setLocale("en");
                        break;
                    case 1:
                        setLocale("it");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
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

    }

    private void setLocale(String languageCode) {
        if (!languageCode.equals(Locale.getDefault().getLanguage())) {

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(PREF_SELECTED_LANGUAGE, languageCode).apply();

            Locale locale = new Locale(languageCode);
            Locale.setDefault(locale);
            Configuration configuration = new Configuration();
            configuration.setLocale(locale);
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

            requireActivity().recreate();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings_main, container, false);

    }


}