package com.esgi.mypunch.data.mainapi;

import com.esgi.mypunch.data.dtos.BoxingSession;
import com.esgi.mypunch.data.dtos.Credentials;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PunchMyNodeProvider {
    private static final String BASE_URL = "?";

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

    public Call<String> getRoot(Credentials credentials) {
        return pmnService.getToken(credentials);
    }

    public Call<List<BoxingSession>> getPeopleList() {
        return pmnService.getUserPunches(token);
    }
}
