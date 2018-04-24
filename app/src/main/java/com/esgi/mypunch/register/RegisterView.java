package com.esgi.mypunch.register;


public interface RegisterView {

    void showProgress();

    void hideProgress();

    void setUsernameError(String msg);

    void setPasswordError(String msg);

    void navigateToLogin();
}
