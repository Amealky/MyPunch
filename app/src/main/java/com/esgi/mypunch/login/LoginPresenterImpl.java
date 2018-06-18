package com.esgi.mypunch.login;


import android.util.Log;

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
        // TODO call web service

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
                    onSuccess();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                onServerError("Unknown error.");
            }
        });
        //onSuccess();
    }

    @Override
    public void onDestroy() {
        loginView = null;
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
