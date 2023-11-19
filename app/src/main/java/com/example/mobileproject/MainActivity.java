package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        if(/*condizione di accesso non effettuato//*){
            Intent i = new Intent(this, Login.class);
            startActivityForResult(i); //ma è deprecato, serve qualcos'altro
        }
        */
        BottomNavigationView bottomBar = findViewById(R.id.bottomNavigationView);
        NavHostFragment n = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.fragment_window_host);
        AppBarConfiguration barConfig = new AppBarConfiguration.Builder(R.id.startingFragment,
                R.id.searchFragment, R.id.profileFragment, R.id.settingsFragment).build();
        try {
            NavController ctrl = n.getNavController();
            NavigationUI.setupWithNavController(bottomBar, n.getNavController());
        } catch (NullPointerException e) {
            Log.wtf("AAAA", "fkng null pointer");
        }
    }
}