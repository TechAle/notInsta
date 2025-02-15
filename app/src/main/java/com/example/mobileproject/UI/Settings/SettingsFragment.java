package com.example.mobileproject.UI.Settings;

import static com.example.mobileproject.utils.Constants.PICK_IMAGE_REQUEST;
import static com.example.mobileproject.utils.Constants.PREF_SELECTED_LANGUAGE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.mobileproject.R;
import com.example.mobileproject.UI.Login.LoginActivity;
import com.example.mobileproject.UI.Login.UsersVMFactory;
import com.example.mobileproject.UI.Login.UsersViewModel;
import com.example.mobileproject.dataLayer.repositories.PostManager;
import com.example.mobileproject.dataLayer.repositories.UserRepository;
import com.example.mobileproject.databinding.FragmentSettingsBinding;
import com.example.mobileproject.utils.FragmentUtils;
import com.example.mobileproject.utils.ServiceLocator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public final class SettingsFragment extends Fragment {

    private NavController ctrl;
    private FragmentSettingsBinding binding;
    private Spinner languagesSpinner;
    private boolean firstSelected = true;
    private UsersViewModel UVM;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctrl = NavHostFragment.findNavController(this);

        UserRepository ur = ServiceLocator.getInstance().getUserRepo();
        PostManager pr = ServiceLocator.getInstance().getPostRepo(this.requireActivity().getApplication());
        if (ur != null && pr != null) {
            UVM = new ViewModelProvider(requireActivity(), new UsersVMFactory(ur, pr)).get(UsersViewModel.class);
        }
        else{
            return;
            //TODO: gestione eccezioni
        }
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        String selectedLanguage = sharedPref.getString(PREF_SELECTED_LANGUAGE, "en");
        FragmentUtils.loadLanguage(selectedLanguage, getActivity(), getResources());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        languagesSpinner = binding.languagesSpinner;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.languages_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languagesSpinner.setAdapter(adapter);
        languagesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!firstSelected)
                    switch (position) {
                        case 0:
                            FragmentUtils.loadLanguage("en", getActivity(), getResources());
                            break;
                        case 1:
                            FragmentUtils.loadLanguage("it", getActivity(), getResources());
                            break;
                    }
                firstSelected = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        binding.changeUsernameText.setOnClickListener(v ->
                ctrl.navigate(R.id.action_settingsFragment_to_changeUsernameFragment)
        );

        binding.changePasswordText.setOnClickListener(v ->
                ctrl.navigate(R.id.action_settingsFragment_to_changePasswordFragment)
        );

        binding.changePictureButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            //TODO: deprecated
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });


        binding.signOutButton.setOnClickListener(v -> {
            UVM.signOut(requireActivity().getApplicationContext());
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra((String) getText(R.string.success_logout), getText(R.string.success_logout));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
        binding.DeleteAccountButton.setOnClickListener(v -> UVM.deleteAccount());
        return view;
    }

    @Deprecated
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if(selectedImageUri != null){
                InputStream inputStream;
                Bitmap photo;
                try {
                    inputStream = requireActivity().getContentResolver().openInputStream(selectedImageUri);
                    photo = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                UVM.changeImage(photo);
            }
        }
    }
}