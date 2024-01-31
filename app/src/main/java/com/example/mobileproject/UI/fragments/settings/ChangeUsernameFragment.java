package com.example.mobileproject.UI.fragments.settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mobileproject.R;
import com.example.mobileproject.UI.fragments.home.SettingsFragment;
import com.example.mobileproject.models.SettingsViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeUsernameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeUsernameFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View backButton;
    private Button changeButton;
    private SettingsViewModel settingsViewModel;

    public ChangeUsernameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangeUsernameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeUsernameFragment newInstance(String param1, String param2) {
        ChangeUsernameFragment fragment = new ChangeUsernameFragment();
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

        View view = inflater.inflate(R.layout.fragment_change_username, container, false);

        backButton = view.findViewById(R.id.backButtonChangeUsername);
        changeButton = view.findViewById(R.id.changeUsernameButton);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.fragment_window_host, SettingsFragment.class, null)
                        .commit();

            }
        });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextInputLayout inputUsername = getView().findViewById(R.id.textInputUsername);
                String username = inputUsername.getEditText().getText().toString();

                //da spostare in viewmodel??
                if (username.length() < 5) {
                    inputUsername.setError(getString(R.string.error_username));
                    Snackbar.make(
                            getView().findViewById(R.id.layout),
                            getString(R.string.error_username),
                            Snackbar.LENGTH_SHORT).show();
                } else {
                    //todo firebase

                    inputUsername.setError(getString(R.string.empty));
                    inputUsername.getEditText().setText(getText(R.string.empty));

                    Snackbar.make(
                            getView().findViewById(R.id.layout),
                            getString(R.string.username_changed),
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }
}