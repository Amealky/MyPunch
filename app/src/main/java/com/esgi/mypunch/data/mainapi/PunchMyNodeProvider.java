package com.esgi.mypunch.data.mainapi;

import com.esgi.mypunch.data.dtos.BoxingSession;
import com.esgi.mypunch.data.dtos.Credentials;
import com.esgi.mypunch.data.dtos.Token;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PunchMyNodeProvider {
    // change to your web service host
    private static final String BASE_URL = "http://192.168.1.19:8080";

    private PunchMyNodeService pmnService;
    private String token;

    public PunchMyNodeProvider() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();
        pmnService = retrofit.create(PunchMyNodeService.class);
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        return okBuilder.build();
    }

    public Call<Token> getToken(Credentials credentials) {
        return pmnService.getToken(credentials);
    }

    public Call<List<BoxingSession>> getPeopleList() {
        return pmnService.getUserPunches(token);
    }
}
