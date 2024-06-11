package com.example.mobileproject.dataLayer.sources;

import android.graphics.Bitmap;

import java.io.File;
import java.util.concurrent.Future;

public abstract class GeneralPostImageRemoteSource {
    public abstract Future<Boolean> createImage(String id, Bitmap bmp);

    public abstract Future<Boolean> retrieveImage(String id, File dst);
}
