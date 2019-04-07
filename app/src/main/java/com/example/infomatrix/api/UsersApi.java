package com.example.infomatrix.api;

import com.example.infomatrix.models.Users;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UsersApi {

    @GET("/api/mod/users")
    Call<Users> getUsers();

}
