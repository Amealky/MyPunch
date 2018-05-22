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
        onSuccess();
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
