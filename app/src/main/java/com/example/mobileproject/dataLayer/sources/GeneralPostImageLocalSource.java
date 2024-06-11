package com.example.mobileproject.dataLayer.sources;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

public abstract class GeneralPostImageLocalSource {
    public abstract Bitmap getImage(Uri img);

    public abstract Uri createImage(Bitmap bmp, String id);

    public abstract void deleteImages();

    public abstract File createEmptyImageFile(String id);

    public abstract Uri renameImage(Uri img, String id);
}
