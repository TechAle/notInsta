package com.example.mobileproject.dataLayer.sources;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Product;
import com.example.mobileproject.service.StoreAPIService;
import com.example.mobileproject.utils.ServiceLocator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public final class AdvertisementSource extends GeneralAdvSource {

    private final StoreAPIService storeAPIService;

    public AdvertisementSource() {
        this.storeAPIService = ServiceLocator.getInstance().getProductsApiService();
    }

    @Override
    public void getAdvPost() {
        Call<List<Product>> storeResponseCall = storeAPIService.getProducts(10);
        storeResponseCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    List<Product> productList = response.body();
                    int pos = (int) (Math.random()*productList.size());
                    Post tmp = new Post("?$%&!#!", productList.get(pos).getTitle(),null, null, null, true, Uri.parse(productList.get(pos).getImage()));
                    c.onSuccessAdv(tmp);
                } else {
                    c.onFailureAdv(new Exception("Null pointer"));
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                c.onFailureAdv((Exception) t);
            }
        });
    }
}
