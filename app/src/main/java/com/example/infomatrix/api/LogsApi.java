package com.example.infomatrix.api;

import com.example.infomatrix.models.ServiceLog;
import com.example.infomatrix.models.MessagedResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LogsApi {

    @POST("/api/mod/logs")
    Call<MessagedResponse> addLog(@Body ServiceLog serviceLog);

}
