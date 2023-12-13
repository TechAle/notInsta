package com.example.mobileproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.mobileproject.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
public class SingupActivity extends AppCompatActivity {

    //private FirebaseAuth mAuth;

    private Button buttonBack;
    private TextInputLayout inputUsername;
    private TextInputLayout inputEmail;
    private TextInputLayout inputPassword;
    private TextInputLayout inputPasswordRepeat;
    private Button buttonSignup;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        //mAuth = FirebaseAuth.getInstance();

        buttonBack = findViewById(R.id.buttonBack);
        inputUsername = findViewById(R.id.textInputUsername);
        inputEmail = findViewById(R.id.textInputEmail);
        inputPassword = findViewById(R.id.textInputPassword);
        inputPasswordRepeat = findViewById(R.id.textInputPasswordRepeat);
        buttonSignup = findViewById(R.id.buttonSignup);
        buttonTerms = findViewById(R.id.buttonTerms);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SingupActivity.this, LoginActivity.class));
                finish();
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = inputUsername.getEditText().getText().toString();
                String email = inputEmail.getEditText().getText().toString();
                String password = inputPassword.getEditText().getText().toString();
                String passwordRepeat = inputPasswordRepeat.getEditText().getText().toString();

                if(TextUtils.isEmpty(username)) {
                    inputUsername.setError(getString(R.string.error_username));
                    Snackbar.make(
                            findViewById(android.R.id.content),
                            getString(R.string.error_username),
                            Snackbar.LENGTH_SHORT);
                } else {
                    inputUsername.setError(null);
                }
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
                if(!password.equals(passwordRepeat)) {
                    inputPasswordRepeat.setError(getString(R.string.error_passwordMatch));
                    Snackbar.make(
                            findViewById(android.R.id.content),
                            getString(R.string.error_passwordMatch),
                            Snackbar.LENGTH_SHORT);
                } else {
                    inputPasswordRepeat.setError(null);
                }

                /*mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Snackbar.make(
                                            findViewById(android.R.id.content),
                                            getString(R.string.success_signup),
                                            Snackbar.LENGTH_SHORT);
                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                    finish();
                                } else {
                                    Snackbar.make(
                                            findViewById(android.R.id.content),
                                            getString(R.string.error_signup),
                                            Snackbar.LENGTH_SHORT);
                                }
                            }
                        });*/
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