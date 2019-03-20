package com.example.infomatrix.api;

import com.example.infomatrix.serializers.Token;
import com.example.infomatrix.serializers.UserAuth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TokenApi {

    @POST("/users/obtain/")
    public Call<Token> obtainToken(@Body UserAuth data);

    @POST("/users/refresh/")
    public Call<Token> refreshToken(@Body Token data);

    @POST("/users/verify/")
    public Call<Token> verifyToken(@Body Token data);


}
