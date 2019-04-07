package com.example.infomatrix.network2;

import com.example.infomatrix.api2.AuthApi;
import com.example.infomatrix.api2.LogsApi;
import com.example.infomatrix.api2.UsersApi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private static final String AUTHORIZATION = "Bearer";

    private static NetworkService instance;
    private static final String BASE_URL = "http://infomatrix.asia";
    private Retrofit retrofit;
    private String token;

    private NetworkService() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder builder = chain.request().newBuilder();
                        if (token != null && !token.isEmpty()) {
                            builder.addHeader("Authorization", AUTHORIZATION + " " + token);
                        }
                        return chain.proceed(builder.build());
                    }
                })
                .retryOnConnectionFailure(false);
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

    public AuthApi getAuthApi() {
        return retrofit.create(AuthApi.class);
    }

    public UsersApi getUsersApi() {
        return retrofit.create(UsersApi.class);
    }

    public LogsApi getLogsApi() {
        return retrofit.create(LogsApi.class);
    }

}
