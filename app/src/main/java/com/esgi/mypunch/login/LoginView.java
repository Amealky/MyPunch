package com.esgi.mypunch.login;


public interface LoginView {

    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void navigateRegister();

    void navigateToPunchList();
}
