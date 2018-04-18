package com.example.caroline.mypunch;

/**
 * Created by caroline on 18/04/2018.
 */

public class LoginPresenterImpl implements LoginPresenter {

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
}
