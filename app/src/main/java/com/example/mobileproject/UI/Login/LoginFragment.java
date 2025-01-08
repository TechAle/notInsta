package com.example.mobileproject.UI.Login;

import android.content.Intent;
import android.credentials.CredentialManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileproject.R;
import com.example.mobileproject.UI.Home.HomeActivity;

import com.example.mobileproject.databinding.FragmentLoginBinding;
import com.example.mobileproject.utils.DataEncryptionUtil;

import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.utils.Result;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;

import android.app.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.navigation.fragment.NavHostFragment;

import java.io.IOException;
import java.security.GeneralSecurityException;

public final class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private NavController ctrl;
    private TextInputLayout inputEmail;
    private TextInputLayout inputPassword;
    private UsersViewModel UVM;
    private DataEncryptionUtil dataEncryptionUtil;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    private ActivityResultContracts.StartIntentSenderForResult startIntentSenderForResult;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctrl = NavHostFragment.findNavController(this);

        UVM = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);
        //CredentialManager cm =

        dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());

        oneTapClient = Identity.getSignInClient(requireActivity());

        //TODO: deprecated
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id)) // NON è un errore, stringa creata durante la compilazione in automatico
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();

        startIntentSenderForResult = new ActivityResultContracts.StartIntentSenderForResult();

        activityResultLauncher = registerForActivityResult(startIntentSenderForResult, activityResult -> {
            if (activityResult.getResultCode() == Activity.RESULT_OK) {
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(activityResult.getData());
                    String idToken = credential.getGoogleIdToken();
                    if (idToken != null) {
                        // Got an ID token from Google. Use it to authenticate with Firebase.
                        UVM.getGoogleUserMutableLiveData(idToken).observe(getViewLifecycleOwner(), authenticationResult -> {
                            if (authenticationResult.successful()) {
                                Users user = ((Result.UserResponseSuccessUser) authenticationResult).getData();
                                saveLoginData(user.getEmail(), null, user.getId());
                                UVM.setAuthenticationError(false);
                                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                        "Loggato GOOGLE",
                                        Snackbar.LENGTH_SHORT).show();
                                startActivity(new Intent(requireActivity(), HomeActivity.class));
                            } else {
                                UVM.setAuthenticationError(true);
                                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                        getErrorMessage(((Result.Error) authenticationResult).getMessage()),
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (ApiException e) {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                            requireActivity().getString(R.string.unexpected_error),
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());
        inputEmail = binding.textInputEmail;
        inputPassword = binding.textInputPassword;
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressBar2.setVisibility(View.VISIBLE);
                String email = inputEmail.getEditText().getText().toString().trim();
                String password = inputPassword.getEditText().getText().toString().trim();

                boolean inputError = false;

                //TODO: messaggi non conformi agli standard di sicurezza
                if (TextUtils.isEmpty(email)) {
                    inputError = true;
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
                    inputPassword.setError(getString(R.string.error_password));
                    Snackbar.make(
                            requireView(),
                            getString(R.string.error_password),
                            Snackbar.LENGTH_SHORT);
                } else {
                    inputPassword.setError(null);
                }

                if (!inputError) {
                    if (!UVM.isAuthenticationError()) {
                        UVM.getUserMutableLiveData(email, password, true).observe(
                                getViewLifecycleOwner(), result -> {
                                    if (result.successful()) {
                                        Users user = ((Result.UserResponseSuccessUser) result).getData();
                                        saveLoginData(email, password, user.getId());
                                        UVM.setAuthenticationError(false);
                                        binding.progressBar2.setVisibility(View.GONE);
                                        startActivity(new Intent(requireActivity(), HomeActivity.class));
                                    } else {
                                        UVM.setAuthenticationError(true);
                                        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                                getErrorMessage(((Result.Error) result).getMessage()),
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        UVM.getUser(email, password, true);
                    }
                } else {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                            R.string.check_login_data_message, Snackbar.LENGTH_SHORT).show();
                    binding.progressBar2.setVisibility(View.GONE);
                }
            }
        });

        binding.buttonForgottenPassword.setOnClickListener(v ->
                ctrl.navigate(R.id.action_loginFragment_to_passwordResetFragment));

        binding.buttonSignup.setOnClickListener(v -> {
            ctrl.navigate(R.id.action_loginFragment_to_signupFragment);
        });

        binding.buttonLoginGoogle.setOnClickListener(v -> oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        Log.d("TAG", "onSuccess from oneTapClient.beginSignIn(BeginSignInRequest)");
                        IntentSenderRequest intentSenderRequest =
                                new IntentSenderRequest.Builder(result.getPendingIntent()).build();
                        activityResultLauncher.launch(intentSenderRequest);
                    }
                })
                .addOnFailureListener(requireActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Log.d("TAG", e.getLocalizedMessage());

                        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                requireActivity().getString(R.string.error_no_google_account_found_message),
                                Snackbar.LENGTH_SHORT).show();
                    }
                }));

        binding.buttonTerms.setOnClickListener(v ->
                ctrl.navigate(R.id.action_loginFragment_to_termsConditionsFragment));
    }

    @Override
    public void onResume() {
        super.onResume();
        UVM.setAuthenticationError(false);
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

    private String getErrorMessage(String errorType) {
        switch (errorType) {
            case "invalidCredentials":
                return requireActivity().getString(R.string.error_login_password_message);
            case "invalidUserError":
                return requireActivity().getString(R.string.error_login_user_message);
            default:
                return requireActivity().getString(R.string.unexpected_error);
        }
    }
}