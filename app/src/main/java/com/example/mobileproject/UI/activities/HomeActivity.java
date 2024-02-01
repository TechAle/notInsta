package com.example.mobileproject.UI.activities;

//import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
//import android.view.ViewGroup;

import com.example.mobileproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Questa cosa la avrei anche fatta, ma da problemi sul mio telefono (Android 9)
        /*ViewCompat.setOnApplyWindowInsetsListener( findViewById(R.id.globalView) , (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.leftMargin = insets.left;
            mlp.bottomMargin = insets.bottom;
            mlp.rightMargin = insets.right;
            mlp.topMargin = insets.top;
            v.setLayoutParams(mlp);
            return WindowInsetsCompat.CONSUMED;
        });*/
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
                R.id.searchFragment, R.id.profileFragment).build();
        try {
            NavController ctrl = n.getNavController();
            NavigationUI.setupWithNavController(bottomBar, ctrl);
        } catch (NullPointerException e) {
            Log.wtf("AAAA", "fkng null pointer");
        }
    }
}