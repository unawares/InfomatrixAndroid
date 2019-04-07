package com.example.infomatrix.api2;

import com.example.infomatrix.models2.UserAuthRequestBody;
import com.example.infomatrix.models2.UserAuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("/api/mod/login")
    Call<UserAuthResponse> login(@Body UserAuthRequestBody userAuthRequest);

}
