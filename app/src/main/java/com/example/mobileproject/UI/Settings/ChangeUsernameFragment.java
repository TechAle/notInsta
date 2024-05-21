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

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ChangeUsernameFragment extends Fragment {


    private Button changeButton;
    private UsersViewModel PVM;

    public ChangeUsernameFragment() {
        // Required empty public constructor
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
        else{
            requireActivity().finish(); //TODO: errore adeguato
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_username, container, false);
        changeButton = view.findViewById(R.id.changeUsernameButton);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout inputUsername = getView().findViewById(R.id.textInputUsername);
                String username = inputUsername.getEditText().getText().toString();
                if (username.length() < 5) {
                    inputUsername.setError(getString(R.string.error_username));
                    Snackbar.make(
                            getView().findViewById(R.id.layout),
                            getString(R.string.error_username),
                            Snackbar.LENGTH_SHORT).show();
                } else {
                    PVM.editUsername(username).observe(getViewLifecycleOwner(), output -> {
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