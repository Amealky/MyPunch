package com.example.caroline.mypunch;


public class LoginPresenterImpl implements LoginPresenter, OnLoginFinishedListener {

    private LoginView loginView;

    LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void validateCredentials(String username, String password) {
        // TODO call web service
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void onUsernameError() {
        loginView.setUsernameError();
    }

    @Override
    public void onPasswordError() {
        loginView.setPasswordError();
    }

    @Override
    public void onSuccess() {
        loginView.navigateToPunchList();
    }
}
