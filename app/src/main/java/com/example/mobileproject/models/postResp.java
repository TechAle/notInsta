package com.example.mobileproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class PostResp implements Parcelable {
    private boolean is_loading;
    private List<Post> l;
    protected PostResp(Parcel in) {
    }

    public PostResp(){}
    public PostResp(List<Post> l){
        this.l = l;
    }
    public List<Post> getPostList(){
        return l;
    }
    public static final Creator<PostResp> CREATOR = new Creator<PostResp>() {
        @Override
        public PostResp createFromParcel(Parcel in) {
            return new PostResp(in);
        }

        @Override
        public PostResp[] newArray(int size) {
            return new PostResp[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
    }
}
