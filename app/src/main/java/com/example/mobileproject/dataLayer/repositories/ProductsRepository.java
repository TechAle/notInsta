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
    private final NewsDao newsDao;
    private final ResponseCallback responseCallback;

    public ProductsRepository(Application application, ResponseCallback responseCallback) {
        this.application = application;
        this.storeAPIService = ServiceLocator.getInstance().getNewsApiService();
        NewsRoomDatabase newsRoomDatabase = ServiceLocator.getInstance().getNewsDao(application);
        this.newsDao = newsRoomDatabase.newsDao();
        this.responseCallback = responseCallback;
    }

    @Override
    public void fetchProducts(int limit, long lastUpdate) {

        long currentTime = System.currentTimeMillis();

        if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            Call<StoreApiResponse> storeResponseCall = storeAPIService.getProducts(limit);

            storeResponseCall.enqueue(new Callback<StoreApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<StoreApiResponse> call,
                                       @NonNull Response<StoreApiResponse> response) {

                    if (response.body() != null && response.isSuccessful()) {
                        List<Product> productList = response.body().getProducts();
                        saveDataInDatabase(productList);
                    } else {
                        responseCallback.onFailure(application.getString(R.string.error_retrieving_products));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StoreApiResponse> call, @NonNull Throwable t) {
                    responseCallback.onFailure(t.getMessage());
                }
            });
        } else {
            Log.d(TAG, application.getString(R.string.data_read_from_local_database));
            readDataFromDatabase(lastUpdate);
        }
    }


    private void saveDataInDatabase(List<Product> productList) {
        NewsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Product> allProducts = newsDao.getAll();


            for (Product product : allProducts) {

                if (productList.contains(product)) {
                    productList.set(productList.indexOf(product), product);
                }
            }

            List<Long> insertedNewsIds = newsDao.insertNewsList(productList);
            for (int i = 0; i < productList.size(); i++) {
                productList.get(i).setId(insertedNewsIds.get(i));
            }

            responseCallback.onSuccess(productList, System.currentTimeMillis());
        });
    }


    private void readDataFromDatabase(long lastUpdate) {
        NewsRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(newsDao.getAll(), lastUpdate);
        });
    }


}
