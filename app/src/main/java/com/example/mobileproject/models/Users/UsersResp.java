package com.example.mobileproject.models.Users;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class UsersResp implements Parcelable {
    private boolean is_loading;
    private List<Users> l;
    protected UsersResp(Parcel in) {
    }

    public UsersResp(){}
    public UsersResp(List<Users> l){
        this.l = l;
    }
    public List<Users> getUsersList(){
        return l;
    }
    public static final Creator<UsersResp> CREATOR = new Creator<UsersResp>() {
        @Override
        public UsersResp createFromParcel(Parcel in) {
            return new UsersResp(in);
        }

        @Override
        public UsersResp[] newArray(int size) {
            return new UsersResp[size];
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
