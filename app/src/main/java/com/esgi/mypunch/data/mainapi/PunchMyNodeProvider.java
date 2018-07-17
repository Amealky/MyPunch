package com.esgi.mypunch.data.mainapi;

import com.esgi.mypunch.data.dtos.BoxingSession;
import com.esgi.mypunch.data.dtos.BoxingSessionToSend;
import com.esgi.mypunch.data.dtos.CandidateUser;
import com.esgi.mypunch.data.dtos.Credentials;
import com.esgi.mypunch.data.dtos.User;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PunchMyNodeProvider {
    // change to your web service host
    private static final String BASE_URL = "http://192.168.0.24:8080";

    private PunchMyNodeService pmnService;

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

    public Call<User> connect(Credentials credentials) {
        return pmnService.getToken(credentials);
    }

    public Call<User> register(CandidateUser candidate) {
        return pmnService.register(candidate);
    }

    public Call<Void> logout(String token) {
        return pmnService.logout(token);
    }

    public Call<Void> checkToken(String token) {
        return pmnService.checkToken(token);
    }

    public Call<List<BoxingSession>> getSessionsForUser(User user) {
        return pmnService.getUserPunches(user.getId(), user.getToken());
    }

    public Call<Void> addSession(User user,BoxingSessionToSend session){
        return pmnService.addSessions(session, user.getToken());
    }


}
