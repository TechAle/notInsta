package com.example.mobileproject.UI.Camera;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobileproject.dataLayer.repositories.PostManager;
import com.example.mobileproject.dataLayer.repositories.PostResponseCallback;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.FilterUtils;
import com.example.mobileproject.utils.Result;

import java.util.Arrays;

//TODO: sistemare bug relativi all'applicazione dei filtri
public final class ProcessedImageViewModel extends ViewModel implements PostResponseCallback{
    private final PostManager pr;

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
    private final MutableLiveData<Result> creationResponse = new MutableLiveData<>();

    public ProcessedImageViewModel(PostManager pr){
        this.pr = pr;
        pr.setCallback(this);
    }
    public MutableLiveData<Bitmap> getProcessedImage() {
        if (processedImage == null) {
            processedImage = new MutableLiveData<>();
        }
        return processedImage;
    }
    public MutableLiveData<Bitmap> getTempImage() {
        if (tempImage == null) {
            tempImage = new MutableLiveData<>();
        }
        return tempImage;
    }
    public MutableLiveData<String> getFilter() {
        if (filter == null) {
            filter = new MutableLiveData<>();
            filter.setValue("hue");
        }
        return filter;
    }
    public MutableLiveData<Integer> getParam1() {
        if (param1 == null) {
            param1 = new MutableLiveData<>();
            param1.setValue(0);
        }
        return param1;
    }
    public MutableLiveData<Integer> getParam2() {
        if (param2 == null) {
            param2 = new MutableLiveData<>();
            param2.setValue(0);
        }
        return param2;
    }
    public MutableLiveData<Integer> getParam3() {
        if (param3 == null) {
            param3 = new MutableLiveData<>();
            param3.setValue(0);
        }
        return param3;
    }
    public MutableLiveData<String> getDescription() {
        if (description == null) {
            description = new MutableLiveData<>();
            description.setValue("");
        }
        return description;
    }
    public MutableLiveData<String[]> getTags() {
        if (tags == null) {
            tags = new MutableLiveData<>();
            tags.setValue(new String[0]);
        }
        return tags;
    }
    public MutableLiveData<Boolean> getIsPromotional() {
        if (isPromotional == null) {
            isPromotional = new MutableLiveData<>();
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

    // Resets the values
    public void reset() {
        getFilter().setValue("hue");
        getParam1().setValue(0);
        getParam2().setValue(0);
        getParam3().setValue(0);
    }

    // Overwrites the processed image (saves the changes)
    public void saveChanges() {
        getProcessedImage().setValue(getTempImage().getValue());
    }

    // Posts the image with the description
    public MutableLiveData<Result> postImage(AppCompatActivity t) {
        saveChanges();
        Bitmap image = getProcessedImage().getValue();
        String desc = getDescription().getValue();
        String[] tagArr = getTags().getValue();
        Boolean isProm = getIsPromotional().getValue();
        try{
            if(pr != null){
                Post p = new Post(null, desc, null, Arrays.asList(tagArr), isProm);
                pr.createPost(p, image);
                return creationResponse;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onResponseCreation(Result r){
        creationResponse.postValue(r);
    }
}
