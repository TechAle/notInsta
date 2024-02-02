package com.example.mobileproject.dataLayer.repositories;

import static com.example.mobileproject.utils.Constants.FRESH_TIMEOUT;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.mobileproject.R;
import com.example.mobileproject.models.Product;
import com.example.mobileproject.models.StoreApiResponse;
import com.example.mobileproject.models.Users.UsersResp;
import com.example.mobileproject.service.StoreAPIService;
import com.example.mobileproject.utils.ResponseCallback;
import com.example.mobileproject.utils.Result;
import com.example.mobileproject.utils.ServiceLocator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductsRepository implements IProductsRepository {


    private final Application application;
    private final StoreAPIService storeAPIService;
    private final MutableLiveData<Result> data;

    public ProductsRepository(Application application) {
        this.application = application;
        this.storeAPIService = ServiceLocator.getInstance().getProductsApiService();
        this.data = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Result> fetchProducts(int limit) {

        Call<List<Product>> storeResponseCall = storeAPIService.getProducts(10);

        storeResponseCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    List<Product> productList = response.body();
                    Result.ProductSuccess result = new Result.ProductSuccess(productList);
                    data.postValue(result);
                } else {
                    Result.Error resultError = new Result.Error(application.getString(R.string.error_retrieving_products));
                    data.postValue(resultError);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Result.Error resultError = new Result.Error(t.getMessage());
                data.postValue(resultError);
            }

        });
        return data;

    }


}
