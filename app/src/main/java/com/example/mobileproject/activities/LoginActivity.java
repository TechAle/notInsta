package com.example.mobileproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    //private FirebaseAuth mAuth;

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
        /*FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // startActivity(new Intent(LoginActivity.this, <HOMEPAGE>.class));
        }*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isLogged = false;
        if (isLogged) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
        // Things to do at the login
        setContentView(R.layout.activity_login);

        /*mAuth = FirebaseAuth.getInstance();*/

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

                /*mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Snackbar.make(
                                            findViewById(android.R.id.content),
                                            getString(R.string.success_login),
                                            Snackbar.LENGTH_SHORT);
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class)); //TODO Activity homepage
                                    finish();
                                } else {
                                    Snackbar.make(
                                            findViewById(android.R.id.content),
                                            getString(R.string.error_login),
                                            Snackbar.LENGTH_SHORT);
                                }
                            }
                        });*/
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
                startActivity(new Intent(LoginActivity.this, SingupActivity.class));
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
