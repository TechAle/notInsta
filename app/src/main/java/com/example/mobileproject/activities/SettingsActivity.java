package com.example.mobileproject.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.mobileproject.R;
import com.example.mobileproject.fragments.settings.SettingsFragment;
import com.example.mobileproject.models.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity {
    private SettingsViewModel settingsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragment_container_view_impostazioni, SettingsFragment.class, null)
                .commit();
    }

}