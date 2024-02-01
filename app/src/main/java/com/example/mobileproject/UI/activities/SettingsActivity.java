package com.example.mobileproject.UI.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.mobileproject.R;
import com.example.mobileproject.databinding.ActivitySettingsBinding;
public class SettingsActivity extends AppCompatActivity {
    private NavController ctrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActivitySettingsBinding bind = ActivitySettingsBinding.inflate(getLayoutInflater());
        AppBarConfiguration config = new AppBarConfiguration.Builder(R.id.settingsFragment, R.id.changeUsernameFragment, R.id.changePasswordFragment)
                .build();
        NavHostFragment n = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_settings_host);
        ctrl = n.getNavController();
        setSupportActionBar(bind.toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            bind.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24);
            bind.toolbar.setNavigationOnClickListener(view -> {
            if(!ctrl.popBackStack()){ //che bella la back stack
                finish();
            }
        });
    }
}