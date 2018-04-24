package com.esgi.mypunch.register;


public class RegisterPresenterImpl implements RegisterPresenter, OnRegisterFinishedListener {

    private RegisterView registerView;
    private final String usernameRules = "Username must have at least 5 characters.";
    private final String passwordRules = "Password must have at least 8 characters.";

    RegisterPresenterImpl(RegisterView registerView) {
        this.registerView = registerView;
    }

    @Override
    public void validateCredentials(String username, String password, String passwordRepeat) {
        registerView.showProgress();
        // TODO send to web service
    }

    @Override
    public void onDestroy() {
        registerView = null;
    }

    @Override
    public void onUsernameError() {
        // TODO if username already taken
        registerView.hideProgress();
        registerView.setUsernameError(usernameRules);
    }

    @Override
    public void onPasswordError() {
        registerView.hideProgress();
        registerView.setPasswordError(passwordRules);
    }

    @Override
    public void onServerError() {
        registerView.hideProgress();
        // TODO other errors
    }

    @Override
    public void onSuccess() {
        registerView.navigateToLogin();
    }
}
