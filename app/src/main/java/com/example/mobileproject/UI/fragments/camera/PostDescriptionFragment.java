package com.example.mobileproject.UI.fragments.camera;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileproject.R;
import com.example.mobileproject.models.ProcessedImageViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostDescriptionFragment extends Fragment {

    private ProcessedImageViewModel viewModel;

    // View Elements
    private TextInputEditText textInput;
    private TextInputEditText tagInput;
    private Button addTag;
    private LinearLayout linearLayout;
    private Map<String, Button> tags;

    public PostDescriptionFragment() {
        // Required empty public constructor
    }

    public static PostDescriptionFragment newInstance() {
        return new PostDescriptionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ProcessedImageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_description, container, false);
        textInput = view.findViewById(R.id.textInput);
        tagInput = view.findViewById(R.id.tagInput);
        addTag = view.findViewById(R.id.addTagButton);
        linearLayout = view.findViewById(R.id.tagLayout);
        tags = new HashMap<String, Button>();

        textInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                viewModel.getDescription().setValue(s.toString());
            }
        });
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = tagInput.getText().toString();
                if (!text.equals("") && !tags.keySet().contains(text)){
                    Button newTag = new Button(view.getContext());
                    newTag.setText(text);
                    linearLayout.addView(newTag);
                    tags.put(text, newTag);
                    viewModel.getTags().setValue(tags.keySet().toArray(new String[0]));
                    newTag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tags.remove(((Button)view).getText().toString());
                            view.setVisibility(View.GONE);
                            viewModel.getTags().setValue(tags.keySet().toArray(new String[0]));
                        }
                    });
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}