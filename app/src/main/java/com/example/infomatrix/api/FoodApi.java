package com.example.infomatrix.api;

import com.example.infomatrix.serializers.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FoodApi {

    @GET("/foods/")
    public Call<List<Food>> getFoods();

}
