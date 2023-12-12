package com.example.testjava.UI.post_creation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.testjava.R;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageViewerFragment extends Fragment {

    private String filter;
    private int numberOfSeekBars;

    private ProcessedImageViewModel viewModel;

    // View Elements
    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private SeekBar seekBar3;
    private ImageView imageView;

    public ImageViewerFragment() {
        // Required empty public constructor
    }

    public static ImageViewerFragment newInstance() {
        return new ImageViewerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filter = "non";
        numberOfSeekBars = 3;
        viewModel = new ViewModelProvider(requireActivity()).get(ProcessedImageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_viewer, container, false);
        seekBar1 = view.findViewById(R.id.seekBar1);
        seekBar2 = view.findViewById(R.id.seekBar2);
        seekBar3 = view.findViewById(R.id.seekBar3);

        seekBar1.setProgress(viewModel.getParam1().getValue());
        seekBar2.setProgress(viewModel.getParam2().getValue());
        seekBar3.setProgress(viewModel.getParam3().getValue());

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                viewModel.getParam1().setValue(i);
                viewModel.applyFilter();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                viewModel.getParam2().setValue(i);
                viewModel.applyFilter();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                viewModel.getParam3().setValue(i);
                viewModel.applyFilter();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadUI();
        super.onViewCreated(view, savedInstanceState);
    }

    //------- PRIVATE METHODS ---//

    // Loads the UI according to the fragment's parameters
    private void loadUI() {
        if (!Objects.equals(filter, "none")) {
            switch (numberOfSeekBars) {
                case 1:
                    seekBar1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    seekBar1.setVisibility(View.VISIBLE);
                    seekBar2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    seekBar1.setVisibility(View.VISIBLE);
                    seekBar2.setVisibility(View.VISIBLE);
                    seekBar3.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}