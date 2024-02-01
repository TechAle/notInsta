package com.example.mobileproject.models;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobileproject.utils.FilterUtils;

import java.io.FileOutputStream;


public class ProcessedImageViewModel extends ViewModel {

    // Live Data
    private MutableLiveData<Bitmap> processedImage;
    private MutableLiveData<Bitmap> tempImage;
    private MutableLiveData<String> filter;
    private MutableLiveData<Integer> param1;
    private MutableLiveData<Integer> param2;
    private MutableLiveData<Integer> param3;
    private MutableLiveData<String> description;
    private MutableLiveData<String[]> tags;
    private MutableLiveData<Boolean> isPromotional;

    public MutableLiveData<Bitmap> getProcessedImage() {
        if (processedImage == null) {
            processedImage = new MutableLiveData<Bitmap>();
        }
        return processedImage;
    }
    public MutableLiveData<Bitmap> getTempImage() {
        if (tempImage == null) {
            tempImage = new MutableLiveData<Bitmap>();
        }
        return tempImage;
    }
    public MutableLiveData<String> getFilter() {
        if (filter == null) {
            filter = new MutableLiveData<String>();
            filter.setValue("hue");
        }
        return filter;
    }
    public MutableLiveData<Integer> getParam1() {
        if (param1 == null) {
            param1 = new MutableLiveData<Integer>();
            param1.setValue(0);
        }
        return param1;
    }
    public MutableLiveData<Integer> getParam2() {
        if (param2 == null) {
            param2 = new MutableLiveData<Integer>();
            param2.setValue(0);
        }
        return param2;
    }
    public MutableLiveData<Integer> getParam3() {
        if (param3 == null) {
            param3 = new MutableLiveData<Integer>();
            param3.setValue(0);
        }
        return param3;
    }
    public MutableLiveData<String> getDescription() {
        if (description == null) {
            description = new MutableLiveData<String>();
            description.setValue("");
        }
        return description;
    }
    public MutableLiveData<String[]> getTags() {
        if (tags == null) {
            tags = new MutableLiveData<String[]>();
            tags.setValue(new String[0]);
        }
        return tags;
    }
    public MutableLiveData<Boolean> getIsPromotional() {
        if (isPromotional == null) {
            isPromotional = new MutableLiveData<Boolean>();
            isPromotional.setValue(false);
        }
        return isPromotional;
    }


    //------- OPERATIONS ---//

    public void applyFilter() {
        String f = getFilter().getValue();
        int x = getParam1().getValue();
        int y = getParam2().getValue();
        int z = getParam3().getValue();
        Bitmap image = getProcessedImage().getValue();

        getTempImage().setValue(FilterUtils.applyFilter(image, f, x, y, z));
    }

    // Posts the image with the description
    public void postImage() {
        Bitmap image = getProcessedImage().getValue();
        String desc = getDescription().getValue();
        String[] tagArr = getTags().getValue();
        Boolean isProm = getIsPromotional().getValue();
        try{
            //FileOutputStream out = new FileOutputStream(file);
            //image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            //out.flush();
            //out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
