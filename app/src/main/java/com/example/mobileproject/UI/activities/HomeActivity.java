package com.example.mobileproject.UI.activities;

//import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
//import android.view.ViewGroup;

import com.example.mobileproject.R;
import com.example.mobileproject.ViewModels.Posts.PostsVMFactory;
import com.example.mobileproject.ViewModels.Posts.PostsViewModel;
import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.utils.FragmentUtils;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private PostsViewModel PVM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        if(FirebaseAuth.getInstance().getCurrentUser() == null){ //nessun utente loggato, fare il login e terminare l'attività. Ma non deve essere nel datalayer???
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
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
        NavController ctrl = n.getNavController();
        NavigationUI.setupWithNavController(bottomBar, ctrl);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String selectedLanguage = sharedPref.getString("selected_language", "en");
        FragmentUtils.loadLanguage(selectedLanguage, this, getResources());

        ServiceLocator sl = ServiceLocator.getInstance();
        PostRepository pr = sl.getPostRepo();
        UserRepository ur = sl.getUserRepo(getApplication());
        if(pr != null && ur != null){
            PVM = new ViewModelProvider(this, new PostsVMFactory(sl.getPostRepo(), sl.getUserRepo(getApplication())))
                    .get(PostsViewModel.class);
        }/*
        else{
            //Snackbar.make( ,"Unexpected error",Snackbar.LENGTH_SHORT).show();

        }*/
    }
}