package com.example.mobileproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class StoreApiResponse implements Parcelable {

    private List<Product> products;

    public StoreApiResponse() {
    }

    public StoreApiResponse(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Product> getProducts(int limit) {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeTypedList(this.products);
    }

    public void readFromParcel(Parcel source) {
        this.products = source.createTypedArrayList(Product.CREATOR);
    }

    protected StoreApiResponse(Parcel in) {

        this.products = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Parcelable.Creator<StoreApiResponse> CREATOR = new Parcelable.Creator<StoreApiResponse>() {
        @Override
        public StoreApiResponse createFromParcel(Parcel source) {
            return new StoreApiResponse(source);
        }

        @Override
        public StoreApiResponse[] newArray(int size) {
            return new StoreApiResponse[size];
        }
    };


}


