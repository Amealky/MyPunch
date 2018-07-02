package com.esgi.mypunch.login;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.esgi.mypunch.data.SharedPreferencesKeys;
import com.esgi.mypunch.data.dtos.Credentials;
import com.esgi.mypunch.data.dtos.Token;
import com.esgi.mypunch.data.mainapi.PunchMyNodeProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenterImpl implements LoginPresenter, OnLoginFinishedListener {

    private static final String TAG = "LoginPresenterImpl";
    private LoginView loginView;
    private PunchMyNodeProvider provider;

    LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void validateCredentials(String username, String password) {
        loginView.showProgress();

        Credentials credentials = new Credentials(username, password);
        provider = new PunchMyNodeProvider();
        Call<Token> call = provider.getToken(credentials);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.code() >= 500) {
                    onServerError("Server error.");
                } else if (response.code() >= 400) {
                    onUsernameError("Wrong credentials.");
                } else {
                    Token token = response.body();
                    if (token != null) {
                        Log.i(TAG, "token = " + token.getEncryptedToken());
                        saveToken(token);
                        onSuccess();
                    } else {
                        onServerError("Couldn't join server.");
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                onServerError("Unknown error.");
            }
        });
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void saveToken(Token token) {
        Context ctxt = (Context) this.loginView;
        SharedPreferences prefs = ctxt.getSharedPreferences(SharedPreferencesKeys.BASE_KEY, Context.MODE_PRIVATE);
        prefs.edit().putString(SharedPreferencesKeys.CONNEXION_TOKEN, token.getEncryptedToken()).apply();
    }

    @Override
    public void onUsernameError(String msg) {
        loginView.hideProgress();
        loginView.setUsernameError(msg);
    }

    @Override
    public void onPasswordError(String msg) {
        loginView.hideProgress();
        loginView.setPasswordError(msg);
    }

    @Override
    public void onServerError(String msg) {
        loginView.hideProgress();
        loginView.setServerError(msg);
    }

    @Override
    public void onSuccess() {
        loginView.navigateToPunchList();
    }
}
