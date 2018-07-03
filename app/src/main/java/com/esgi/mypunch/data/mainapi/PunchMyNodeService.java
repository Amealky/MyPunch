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
import retrofit2.http.Path;

public interface PunchMyNodeService {
    @POST("./auth/login")
    Call<Token> getToken(@Body Credentials credentials);

    @POST("./auth/logout")
    Call<Void> logout(@Header("Authorization") String token);

    @GET("./auth/check/{token}")
    Call<Void> checkToken(@Path("token") String token);

    @GET("sessions")
    Call<List<BoxingSession>> getUserPunches(@Header("token") String token);
}
