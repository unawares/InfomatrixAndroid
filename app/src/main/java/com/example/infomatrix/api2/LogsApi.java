package com.example.infomatrix.api2;

import com.example.infomatrix.models2.ServiceLog;
import com.example.infomatrix.models2.MessagedResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LogsApi {

    @POST("/api/mod/logs")
    Call<MessagedResponse> addLog(@Body ServiceLog serviceLog);

}
