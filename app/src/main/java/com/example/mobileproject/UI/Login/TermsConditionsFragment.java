package com.example.mobileproject.UI.Login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TermsConditionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public final class TermsConditionsFragment extends Fragment {

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
    }
}