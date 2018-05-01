package com.esgi.mypunch.login;


public interface LoginView {

    void showProgress();

    void hideProgress();

    void setUsernameError(String msg);

    void setPasswordError(String msg);

    void setServerError(String msg);

    void navigateRegister();

    void navigateToPunchList();
}
