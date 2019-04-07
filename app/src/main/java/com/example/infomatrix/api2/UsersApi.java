package com.example.infomatrix.api2;

import com.example.infomatrix.models2.Users;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UsersApi {

    @GET("/api/mod/users")
    Call<Users> getUsers();

}
