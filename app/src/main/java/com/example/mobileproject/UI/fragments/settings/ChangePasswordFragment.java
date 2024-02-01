package com.example.mobileproject.UI.fragments.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mobileproject.R;
import com.example.mobileproject.ViewModels.Settings.SettingsViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
//    private View backButton;
    private Button changeButton;
    private SettingsViewModel settingsViewModel;


    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        //backButton = view.findViewById(R.id.backButtonChangePassword);
        changeButton = view.findViewById(R.id.changePasswordButton);


     /*   backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.fragment_window_host, SettingsFragment.class, null)
                        .commit();

            }
        });*/

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextInputLayout inputOldPassword = getView().findViewById(R.id.inputOldPassword);
                TextInputLayout inputNewPassword = getView().findViewById(R.id.inputNewPassword);

                String oldPassword = inputOldPassword.getEditText().getText().toString();
                String newPassword = inputNewPassword.getEditText().getText().toString();


                //da spostare in viewmodel??
                if (oldPassword.equals(newPassword)) {
                    inputOldPassword.setError(getString(R.string.password_different));
                    inputNewPassword.setError(getString(R.string.password_different));

                    Snackbar.make(
                            getView().findViewById(R.id.layout),
                            getString(R.string.password_different),
                            Snackbar.LENGTH_SHORT).show();
                } else if (false) {
                    //controllo se la paassword vecchia non coincide

                } else if (newPassword.length() < 6) {
                    inputOldPassword.setError(getString(R.string.empty));
                    inputNewPassword.setError(getString(R.string.password_length));

                    Snackbar.make(
                            getView().findViewById(R.id.layout),
                            getString(R.string.password_length),
                            Snackbar.LENGTH_SHORT).show();
                } else if (false) {
                    //controllo non presenza caratteri speciali
                } else {
                    //todo firebase

                    inputOldPassword.setError(getString(R.string.empty));
                    inputNewPassword.setError(getString(R.string.empty));
                    inputNewPassword.getEditText().setText(getText(R.string.empty));
                    inputOldPassword.getEditText().setText(getText(R.string.empty));

                    Snackbar.make(
                            getView().findViewById(R.id.layout),
                            getString(R.string.password_changed),
                            Snackbar.LENGTH_SHORT).show();
                }


            }
        });

        return view;
    }
}