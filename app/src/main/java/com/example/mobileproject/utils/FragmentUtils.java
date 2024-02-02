package com.example.mobileproject.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.mobileproject.R;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;

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

    public static void loadLanguage(String lang, FragmentActivity activity, Resources res) {
        if (!lang.equals(Locale.getDefault().getLanguage())) {

            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("selected_language", lang).apply();

            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration configuration = new Configuration();
            configuration.setLocale(locale);
            res.updateConfiguration(configuration, res.getDisplayMetrics());

            activity.recreate();
        }
    }

    public static void loadImage(String image, View view, int sponsorImage) {
        ImageView imageView = view.findViewById(sponsorImage);
        Glide.with(view)
                .load(image)
                .into(imageView);
    }
}
