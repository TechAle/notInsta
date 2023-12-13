package com.example.mobileproject.fragments.camera;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.mobileproject.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageChooserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageChooserFragment extends Fragment {

    private final int SELECT_PICTURE = 200;
    private final int TAKE_PICTURE = 100;
    private Button cameraButton;
    private Button galleryButton;
    private Button okButton;

    public ImageChooserFragment() {
        // Required empty public constructor
    }

    public static ImageChooserFragment newInstance(String param1, String param2) {
        return new ImageChooserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_chooser, container, false);
        cameraButton = view.findViewById(R.id.cameraButton);
        galleryButton = view.findViewById(R.id.galleryButton);
        okButton = view.findViewById(R.id.okButton);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(open_camera, TAKE_PICTURE);
            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                getActivity().startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, ImageViewerFragment.class, null)
                        .commit();
            }
        });

        return view;
    }
}