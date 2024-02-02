package com.example.mobileproject.UI.fragments.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mobileproject.R;
import com.example.mobileproject.ViewModels.Users.UsersViewModel;
import com.example.mobileproject.utils.DataEncryptionUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class PasswordResetFragment extends Fragment {

    private Button buttonBack;
    private TextInputLayout inputEmail;
    private Button buttonSendEmail;
    private UsersViewModel usersViewModel;
    private DataEncryptionUtil dataEncryptionUtil;

    public PasswordResetFragment() {
    }

    public static PasswordResetFragment newInstance(String param1, String param2) {
        return new PasswordResetFragment();
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
        return inflater.inflate(R.layout.fragment_password_reset, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonBack = view.findViewById(R.id.buttonBack);
        inputEmail = view.findViewById(R.id.textInputEmail);
        buttonSendEmail = view.findViewById(R.id.buttonSendEmail);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_passwordResetFragment_to_loginFragment);
            }
        });

        buttonSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getEditText().getText().toString().trim();

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

                if (!inputError) {
                    usersViewModel.sendPasswordReset(email);
                    Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            "Email sent",
                            Snackbar.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.action_passwordResetFragment_to_loginFragment);
                } else {
                    usersViewModel.setAuthenticationError(true);
                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                            R.string.check_login_data_message, Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}