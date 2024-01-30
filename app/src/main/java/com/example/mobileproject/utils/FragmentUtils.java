package com.example.mobileproject.utils;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobileproject.R;
import com.google.firebase.storage.StorageReference;

public class FragmentUtils {
    // Function to update the text of a view based on its ID
    // Function to update the text of a view based on its ID
    public static void updateTextById(View parentView, int viewId, String newText) {
        View view = parentView.findViewById(viewId);

        if (view instanceof TextView) {
            // If the view is a TextView, update its text
            ((TextView) view).setText(newText);
        } else {
            // Handle other view types if needed
        }
    }

    public static void loadImage(StorageReference storageRef, String path, View parentView, int viewId) {
        StorageReference imageRef = storageRef.child(path);
        imageRef.getDownloadUrl().addOnCompleteListener(image -> {
            if (image.isSuccessful()) {
                Uri URLImage = image.getResult();
                // Find the ImageView in your layout
                ImageView imageView = parentView.findViewById(viewId);

                // Use Glide to load the image from the URL into the ImageView
                Glide.with(parentView).load(URLImage).into(imageView);
            }
        });
    }
}
