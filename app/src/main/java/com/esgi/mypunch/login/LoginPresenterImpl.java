package com.esgi.mypunch.login;


public class LoginPresenterImpl implements LoginPresenter, OnLoginFinishedListener {

    private LoginView loginView;

    LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void validateCredentials(String username, String password) {
        loginView.showProgress();
        // TODO call web service
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void onUsernameError() {
        loginView.setUsernameError();
        loginView.hideProgress();
    }

    @Override
    public void onPasswordError() {
        loginView.setPasswordError();
        loginView.hideProgress();
    }

    @Override
    public void onServerError() {
        loginView.hideProgress();
        // TODO onServerError
    }

    @Override
    public void onSuccess() {
        loginView.navigateToPunchList();
    }
}
