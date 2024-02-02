package com.example.mobileproject.UI.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.mobileproject.R;
public class SettingsActivity extends AppCompatActivity {
    private NavController ctrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(t);
        AppBarConfiguration config = new AppBarConfiguration
                .Builder()
                .setFallbackOnNavigateUpListener(() -> {
                    if(!ctrl.popBackStack()){
                        finish();
                    }
                    return true;
                }).build();

        NavHostFragment n = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_settings_host);
        ctrl = n.getNavController();
        NavigationUI.setupWithNavController(t, ctrl, config);
    }
}