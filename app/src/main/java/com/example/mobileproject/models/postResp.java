package com.example.mobileproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class postResp implements Parcelable {
    protected postResp(Parcel in) {
    }

    public static final Creator<postResp> CREATOR = new Creator<postResp>() {
        @Override
        public postResp createFromParcel(Parcel in) {
            return new postResp(in);
        }

        @Override
        public postResp[] newArray(int size) {
            return new postResp[size];
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
