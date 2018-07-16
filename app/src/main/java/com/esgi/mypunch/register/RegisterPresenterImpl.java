package com.esgi.mypunch.register;


import com.esgi.mypunch.data.dtos.CandidateUser;
import com.esgi.mypunch.data.dtos.User;
import com.esgi.mypunch.data.mainapi.PunchMyNodeProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresenterImpl implements RegisterPresenter, OnRegisterFinishedListener {

    private RegisterView registerView;
    private final String usernameRules = "Username must have at least 5 characters.";
    private final String passwordRules = "Password must have at least 8 characters.";
    private final PunchMyNodeProvider provider;

    RegisterPresenterImpl(RegisterView registerView) {
        this.registerView = registerView;
        this.provider = new PunchMyNodeProvider();
    }


    @Override
    public void validateCredentials(String email, String firstName, String lastName, String password, String passwordRepeat) {
        registerView.showProgress();
        // TODO send to web service
        if (!password.equals(passwordRepeat)) {
            onPasswordError("Passwords not equals");
        }
        CandidateUser candidateUser = new CandidateUser(email, firstName, lastName, password);
        Call<User> createdUser = provider.register(candidateUser);
        createdUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (user != null) {
                    onSuccess();
                } else {
                    onPasswordError("No user created");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                String msg = t.getMessage();
                onPasswordError(msg);
            }
        });
    }

    @Override
    public void onDestroy() {
        registerView = null;
    }

    @Override
    public void onUsernameError(String msg) {
        registerView.hideProgress();
        registerView.setUsernameError(msg);
    }

    @Override
    public void onPasswordError(String msg) {
        registerView.hideProgress();
        registerView.setPasswordError(msg);
    }

    @Override
    public void onServerError(String msg) {
        registerView.hideProgress();
        registerView.setServerError(msg);
    }

    @Override
    public void onSuccess() {
        registerView.navigateToLogin();
    }
}
