package com.example.mobileproject.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.fragment.app.FragmentActivity;

import java.util.Locale;

@Deprecated
public class FragmentUtils {
    //TODO: Is this a data layer function?

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
}
