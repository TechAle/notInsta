package com.example.mobileproject.UI.Settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mobileproject.R;
import com.example.mobileproject.UI.Login.UsersVMFactory;
import com.example.mobileproject.UI.Login.UsersViewModel;
import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button changeButton;
    private UsersViewModel PVM;


    public ChangePasswordFragment() {
    }


    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ServiceLocator sl = ServiceLocator.getInstance();
        UserRepository ur = sl.getUserRepo(/*this.requireActivity().getApplication()*/);
        PostRepository pr = sl.getPostRepo(this.requireActivity().getApplication());
        if (ur != null && pr != null) {
            PVM = new ViewModelProvider(requireActivity(), new UsersVMFactory(ur, pr)).get(UsersViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        changeButton = view.findViewById(R.id.changePasswordButton);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextInputLayout inputOldPassword = getView().findViewById(R.id.inputOldPassword);
                TextInputLayout inputNewPassword = getView().findViewById(R.id.inputNewPassword);

                String oldPassword = inputOldPassword.getEditText().getText().toString();
                String newPassword = inputNewPassword.getEditText().getText().toString();


                if (oldPassword.equals(newPassword)) {
                    inputOldPassword.setError(getString(R.string.password_different));
                    inputNewPassword.setError(getString(R.string.password_different));

                    Snackbar.make(
                            getView().findViewById(R.id.layout),
                            getString(R.string.password_different),
                            Snackbar.LENGTH_SHORT).show();
                } else if (newPassword.length() < 6) {
                    inputOldPassword.setError(getString(R.string.empty));
                    inputNewPassword.setError(getString(R.string.password_length));

                    Snackbar.make(
                            getView().findViewById(R.id.layout),
                            getString(R.string.password_length),
                            Snackbar.LENGTH_SHORT).show();
                } else if (!containsSpecialCharacters(newPassword)) {
                    inputNewPassword.setError(getString(R.string.pass_special));

                    Snackbar.make(
                            getView().findViewById(R.id.layout),
                            getString(R.string.pass_special),
                            Snackbar.LENGTH_SHORT).show();

                } else {

                    PVM.editPassword(newPassword).observe(getViewLifecycleOwner(), output -> {
                        inputOldPassword.setError(getString(R.string.empty));
                        inputNewPassword.setError(getString(R.string.empty));
                        inputNewPassword.getEditText().setText(getText(R.string.empty));
                        inputOldPassword.getEditText().setText(getText(R.string.empty));

                        if (output.successful()) {
                            Snackbar.make(
                                    getView().findViewById(R.id.layout),
                                    getString(R.string.password_changed),
                                    Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(
                                    getView().findViewById(R.id.layout),
                                    getString(R.string.error_password),
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    });


                }


            }
        });

        return view;
    }

    private boolean containsSpecialCharacters(String newPassword) {
        String specialCharacters = "[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+";
        Pattern pattern = Pattern.compile(specialCharacters);
        Matcher matcher = pattern.matcher(newPassword);
        return matcher.find();
    }
}