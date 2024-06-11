package com.example.mobileproject.service;

import static com.example.mobileproject.utils.Constants.TOP_HEADLINES_LIMIT_PARAMETER;
import static com.example.mobileproject.utils.Constants.TOP_HEADLINES_ENDPOINT;

import com.example.mobileproject.models.Product;
import com.example.mobileproject.models.StoreApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StoreAPIService {
    @GET(TOP_HEADLINES_ENDPOINT)
    Call<List<Product>> getProducts(
            @Query(TOP_HEADLINES_LIMIT_PARAMETER) int limit);
}
