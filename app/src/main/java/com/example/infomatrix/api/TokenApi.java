package com.example.infomatrix.api;

import com.example.infomatrix.models.Token;
import com.example.infomatrix.models.UserAuth;
import com.example.infomatrix.models.UserCode;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TokenApi {

    @POST("/users/obtain/")
    public Call<Token> obtainToken(@Body UserAuth data);

    @POST("/users/refresh/")
    public Call<Token> refreshToken(@Body Token data);

    @POST("/users/verify/")
    public Call<Token> verifyToken(@Body Token data);

    @GET("/users/codes/{code}/")
    public Call<UserCode> getUserCode(@Path("code") String code);

}
