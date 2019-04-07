package com.example.infomatrix.api;

import com.example.infomatrix.models.UserAuthRequestBody;
import com.example.infomatrix.models.UserAuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("/api/mod/login")
    Call<UserAuthResponse> login(@Body UserAuthRequestBody userAuthRequest);

}
