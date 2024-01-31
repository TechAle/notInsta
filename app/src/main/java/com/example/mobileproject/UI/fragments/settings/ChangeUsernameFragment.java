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
import com.example.mobileproject.ViewModels.Users.UsersVMFactory;
import com.example.mobileproject.ViewModels.Users.UsersViewModel;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.models.SettingsViewModel;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ChangeUsernameFragment extends Fragment {

    private String mParam1;
    private String mParam2;
    private View backButton;
    private Button changeButton;
    private SettingsViewModel settingsViewModel;
    private UsersViewModel PVM;

    public ChangeUsernameFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        UserRepository pr = ServiceLocator.getInstance().getUserRepo();
        if(pr != null){
            PVM = new ViewModelProvider(requireActivity(), new UsersVMFactory(pr)).get(UsersViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String id = "0TsbiPUaL5qfFQiH6572";

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
                    PVM.editUsername(id, username).observe(getViewLifecycleOwner(), output -> {
                        if (output.successful()) {
                            Snackbar.make(
                                    getView().findViewById(R.id.layout),
                                    getString(R.string.username_changed),
                                    Snackbar.LENGTH_SHORT).show();
                            inputUsername.setError(getString(R.string.empty));
                            inputUsername.getEditText().setText(getText(R.string.empty));
                        } else {
                            inputUsername.setError("Username not avaible");
                        }
                    });
                }
            }
        });


        return view;
    }
}