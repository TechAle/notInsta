package com.example.mobileproject.UI.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
//import androidx.activity.EdgeToEdge;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
//import android.view.ViewGroup;

import com.example.mobileproject.R;
import com.example.mobileproject.UI.Camera.CameraActivity;
import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.utils.DataStoreSingleton;
import com.example.mobileproject.utils.FragmentUtils;
import com.example.mobileproject.utils.Result;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    private PostsViewModel PVM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);

        ServiceLocator sl = ServiceLocator.getInstance();
        PostRepository pr = sl.getPostRepo(getApplication());
        UserRepository ur = sl.getUserRepo(/*getApplication()*/);
        if(pr == null || ur == null) {
            Log.wtf("WTF", "WTF");
            finish();
            return;
        }
        PVM = new ViewModelProvider(this, new PostsVMFactory(pr, ur))
            .get(PostsViewModel.class);
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
        BottomNavigationView bottomBar = findViewById(R.id.bottomNavigationView);
        NavHostFragment n = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.fragment_window_host);
        /*AppBarConfiguration barConfig = new AppBarConfiguration.Builder(R.id.startingFragment,
                R.id.searchFragment, R.id.profileFragment).build();*/
        NavController ctrl = n.getNavController();
        NavigationUI.setupWithNavController(bottomBar, ctrl);
        //TODO: cambiare la chiamate SharedPreferences con una a DataStore
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String selectedLanguage = sharedPref.getString("selected_language", "en");
        FragmentUtils.loadLanguage(selectedLanguage, this, getResources());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(b -> {
            startActivity(new Intent(this, CameraActivity.class));
        });
    }
}