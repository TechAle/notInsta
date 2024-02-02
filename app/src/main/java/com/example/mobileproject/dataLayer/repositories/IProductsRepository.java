package com.example.mobileproject.dataLayer.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.mobileproject.utils.Result;

public interface IProductsRepository {

    MutableLiveData<Result> fetchProducts(int limit);


}


