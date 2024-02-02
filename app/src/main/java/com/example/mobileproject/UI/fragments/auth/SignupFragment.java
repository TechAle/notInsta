package com.example.mobileproject.UI.fragments.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mobileproject.R;
import com.example.mobileproject.ViewModels.Users.UsersViewModel;

import com.example.mobileproject.utils.DataEncryptionUtil;

import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.utils.Result;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SignupFragment extends Fragment {

    private FirebaseAuth mAuth;

    private Button buttonBack;
    private TextInputLayout inputEmail;
    private TextInputLayout inputPassword;
    private TextInputLayout inputPasswordRepeat;
    private Button buttonSignup;
    private Button buttonTerms;

    private UsersViewModel usersViewModel;
    private DataEncryptionUtil dataEncryptionUtil;

    public SignupFragment() {
    }

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usersViewModel = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);
        usersViewModel.setAuthenticationError(false);
        dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        buttonBack = view.findViewById(R.id.buttonBack);
        inputEmail = view.findViewById(R.id.textInputEmail);
        inputPassword = view.findViewById(R.id.textInputPassword);
        inputPasswordRepeat = view.findViewById(R.id.textInputPasswordRepeat);
        buttonSignup = view.findViewById(R.id.buttonSignup);
        buttonTerms = view.findViewById(R.id.buttonTerms);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_loginFragment);
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getEditText().getText().toString().trim();
                String password = inputPassword.getEditText().getText().toString().trim();
                String passwordRepeat = inputPasswordRepeat.getEditText().getText().toString().trim();

                boolean inputError = false;

                if (TextUtils.isEmpty(email)) {
                    inputError = true;
                    usersViewModel.setAuthenticationError(true);
                    inputEmail.setError(getString(R.string.error_email));
                    Snackbar.make(
                            requireView(),
                            getString(R.string.error_email),
                            Snackbar.LENGTH_SHORT);
                } else {
                    inputEmail.setError(null);
                }
                if (TextUtils.isEmpty(password)) {
                    inputError = true;
                    usersViewModel.setAuthenticationError(true);
                    inputPassword.setError(getString(R.string.error_password));
                    Snackbar.make(
                            requireView(),
                            getString(R.string.error_password),
                            Snackbar.LENGTH_SHORT);
                } else {
                    inputPassword.setError(null);
                }
                if (!password.equals(passwordRepeat)) {
                    inputError = true;
                    usersViewModel.setAuthenticationError(true);
                    inputPasswordRepeat.setError(getString(R.string.error_passwordMatch));
                    Snackbar.make(
                            requireView(),
                            getString(R.string.error_passwordMatch),
                            Snackbar.LENGTH_SHORT);
                } else {
                    inputPasswordRepeat.setError(null);
                }

                 if(!inputError) {
                    if (!usersViewModel.isAuthenticationError()) {
                        usersViewModel.getUserMutableLiveData(email, password, false).observe(
                                getViewLifecycleOwner(), result -> {
                                    if (result.successful()) {
                                        Users user = ((Result.UserResponseSuccessUser) result).getData();
                                        saveLoginData(email, password, user.getId());
                                        usersViewModel.setAuthenticationError(false);
                                        // Navigation.findNavController(view).navigate(R.id.); TODO Homepage
                                    } else {
                                        usersViewModel.setAuthenticationError(true);
                                        Snackbar.make(
                                                requireActivity().findViewById(android.R.id.content),
                                                getErrorMessage(((Result.Error) result).getMessage()),
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        usersViewModel.getUser(email, password, false);
                    }
                } else {
                    usersViewModel.setAuthenticationError(true);
                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                            R.string.check_login_data_message, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        buttonTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_termsConditionsFragment);
            }
        });
    }

    private void saveLoginData(String email, String password, String idToken) {
        try {
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    "com.example.mobileproject.encrypted_preferences", "email_address", email);
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    "com.example.mobileproject.encrypted_preferences", "password", password);
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    "com.example.mobileproject.encrypted_preferences", "google_token", idToken);
            dataEncryptionUtil.writeSecreteDataOnFile("",
                    email.concat(":").concat(password));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private String getErrorMessage(String message) {
        switch (message) {
            case "passwordIsWeak":
                return requireActivity().getString(R.string.error_password);
            case "userCollisionError":
                return requireActivity().getString(R.string.error_user_collision_message);
            default:
                return requireActivity().getString(R.string.unexpected_error);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}