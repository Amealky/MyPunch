package com.esgi.mypunch.data.mainapi;

import com.esgi.mypunch.data.dtos.BoxingSession;
import com.esgi.mypunch.data.dtos.Credentials;
import com.esgi.mypunch.data.dtos.Token;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PunchMyNodeService {
    @POST("./auth/login")
    Call<Token> getToken(@Body Credentials credentials);
    @GET("sessions")
    Call<List<BoxingSession>> getUserPunches(@Header("token") String token);
}
