package com.example.mobileproject.UI.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.mobileproject.UI.Home.HomeActivity;
import com.example.mobileproject.utils.DataEncryptionUtil;
import com.example.mobileproject.utils.FragmentUtils;
import com.example.mobileproject.databinding.ActivityLoginBinding;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.firebase.auth.FirebaseAuth;

import com.example.mobileproject.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

public final class LoginActivity extends AppCompatActivity {

    private UsersViewModel UVM;
    private ActivityLoginBinding binding;
    private NavController ctrl;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // outState.putString(KEY, value);
        // outState.putInt(KEY, value);
        // outState.putParcelable(KEY, value); per oggetti
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceLocator sl = ServiceLocator.getInstance();
        UVM = new ViewModelProvider(this, new UsersVMFactory(sl.getUserRepo(), sl.getPostRepo(getApplication())))
                .get(UsersViewModel.class);
        if(UVM.isLogged()){
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }
        //TODO: dare un occhiata qua che non so se è giusto
        /*try{
            DataEncryptionUtil u = new DataEncryptionUtil(getApplication());
            u.readSecretDataOnFile("com.example.mobileproject.encrypted_preferences");
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        } catch (IOException e1){ //non mi sono loggato

        } catch (GeneralSecurityException e2){
            finish();
        }/* pseudocode:
        if(già loggato){
            vai alla home
        }
        */
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState != null) {
            // Oggetto o = savedInstanceState.getParcelable("OGGETTO_SALVATO");
        }

        //TODO: incapsulare la SharedPreferences in un altra classe (propongo di utilizzare DataStore);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String selectedLanguage = sharedPref.getString("selected_language", "en");
        FragmentUtils.loadLanguage(selectedLanguage, this, getResources());
        Toolbar t = binding.toolbar2;
        setSupportActionBar(t);
        AppBarConfiguration config = new AppBarConfiguration
                .Builder()
                .setFallbackOnNavigateUpListener(() -> {
                    if(!ctrl.popBackStack()){
                        //TODO: alert dialog
                        finish();
                    }
                    return true;
                }).build();

        NavHostFragment n = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_login_host);
        ctrl = n.getNavController();
        NavigationUI.setupWithNavController(t, ctrl, config);
    }
}