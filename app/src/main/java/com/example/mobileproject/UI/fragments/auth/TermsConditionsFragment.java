package com.example.mobileproject.UI.fragments.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mobileproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TermsConditionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermsConditionsFragment extends Fragment {

    private Button buttonBack;

    public TermsConditionsFragment() {
    }

    public static TermsConditionsFragment newInstance() {
        return new TermsConditionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_terms_conditions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonBack = view.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_termsConditionsFragment_to_loginFragment);
            }
        });
    }
}