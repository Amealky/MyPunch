package com.example.caroline.mypunch;

/**
 * Created by caroline on 18/04/2018.
 */

public interface LoginView {

    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void navigateRegister();

    void navigateToPunchList();
}
