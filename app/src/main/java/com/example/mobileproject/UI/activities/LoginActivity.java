package com.example.mobileproject.UI.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.mobileproject.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextInputLayout inputEmail;
    private TextInputLayout inputPassword;
    private Button buttonLogin;
    private Button buttonSignup;
    private Button buttonLoginGoogle;
    private Button buttonForgotPassword;
    private Button buttonTerms;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //startActivity(new Intent(LoginNew.this, HomeActivity.class));
        }
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
        setContentView(R.layout.activity_login);

        if (savedInstanceState != null) {
            // Oggetto o = savedInstanceState.getParcelable("OGGETTO_SALVATO");
        }

        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.textInputEmail);
        inputPassword = findViewById(R.id.textInputPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);
        buttonLoginGoogle = findViewById(R.id.buttonLoginGoogle);
        buttonForgotPassword = findViewById(R.id.buttonForgottenPassword);
        buttonTerms = findViewById(R.id.buttonTerms);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getEditText().getText().toString();
                String password = inputPassword.getEditText().getText().toString();

                if(TextUtils.isEmpty(email)) {
                    inputEmail.setError(getString(R.string.error_email));
                    Snackbar.make(
                            findViewById(android.R.id.content),
                            getString(R.string.error_email),
                            Snackbar.LENGTH_SHORT);
                } else {
                    inputEmail.setError(null);
                }
                if(TextUtils.isEmpty(password)) {
                    inputPassword.setError(getString(R.string.error_password));
                    Snackbar.make(
                            findViewById(android.R.id.content),
                            getString(R.string.error_password),
                            Snackbar.LENGTH_SHORT);
                } else {
                    inputPassword.setError(null);
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    Snackbar.make(
                                            findViewById(android.R.id.content),
                                            getString(R.string.success_login),
                                            Snackbar.LENGTH_SHORT);
                                    finish();
                                } else {
                                    Snackbar.make(
                                            findViewById(android.R.id.content),
                                            getString(R.string.error_login),
                                            Snackbar.LENGTH_SHORT);
                                }
                            }
                        });
            }
        });

        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Forgot password
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });

        buttonLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Login Google
            }
        });

        buttonTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Terms and Conditions text
            }
        });
    }
}