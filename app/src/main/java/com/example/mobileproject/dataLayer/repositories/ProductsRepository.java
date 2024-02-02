package com.example.mobileproject.dataLayer.repositories;

import static com.example.mobileproject.utils.Constants.FRESH_TIMEOUT;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mobileproject.R;
import com.example.mobileproject.models.Product;
import com.example.mobileproject.models.StoreApiResponse;
import com.example.mobileproject.service.StoreAPIService;
import com.example.mobileproject.utils.ResponseCallback;
import com.example.mobileproject.utils.ServiceLocator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductsRepository implements IProductsRepository {

    private static final String TAG = ProductsRepository.class.getSimpleName();

    private final Application application;
    private final StoreAPIService storeAPIService;
    private final ResponseCallback responseCallback;

    public ProductsRepository(Application application, ResponseCallback responseCallback) {
        this.application = application;
        this.storeAPIService = ServiceLocator.getInstance().getProductsApiService();
        this.responseCallback = responseCallback;
    }

    @Override
    public void fetchProducts(int limit, long lastUpdate) {

        Call<StoreApiResponse> storeResponseCall = storeAPIService.getProducts(limit);

        storeResponseCall.enqueue(new Callback<StoreApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoreApiResponse> call,
                                   @NonNull Response<StoreApiResponse> response) {

                if (response.body() != null && response.isSuccessful()) {
                    List<Product> productList = response.body().getProducts();
                } else {
                    responseCallback.onFailure(application.getString(R.string.error_retrieving_products));
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoreApiResponse> call, @NonNull Throwable t) {
                responseCallback.onFailure(t.getMessage());
            }
        });

    }


}
