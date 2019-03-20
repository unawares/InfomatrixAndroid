package com.example.infomatrix.network;

import com.example.infomatrix.api.FoodApi;
import com.example.infomatrix.api.TokenApi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private static NetworkService instance;
    private static final String BASE_URL = "http://192.168.1.8:8000/";
    private Retrofit retrofit;
    private String token;

    private NetworkService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder builder = chain.request().newBuilder();
                        if (token != null) {
                            builder.addHeader("Authorization", "JWT " + token);
                        }
                        return chain.proceed(builder.build());
                    }
                });
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
    }

    public static NetworkService getInstance() {
        if (instance == null) {
            instance = new NetworkService();
        }
        return instance;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenApi getTokenApi() {
        return retrofit.create(TokenApi.class);
    }

    public FoodApi getFoodApi() {
        return retrofit.create(FoodApi.class);
    }

}
