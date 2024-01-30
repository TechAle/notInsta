package com.example.mobileproject.utils;

import android.view.View;
import android.widget.TextView;

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
}
