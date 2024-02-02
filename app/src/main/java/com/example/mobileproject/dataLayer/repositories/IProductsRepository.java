package com.example.mobileproject.dataLayer.repositories;

public interface IProductsRepository {

    void fetchProducts(int limit, long lastUpdate);

    enum JsonParserType {
        JSON_READER,
        JSON_OBJECT_ARRAY,
        GSON,
        JSON_ERROR
    }

}


