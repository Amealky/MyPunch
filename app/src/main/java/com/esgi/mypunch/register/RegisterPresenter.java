package com.esgi.mypunch.register;


public interface RegisterPresenter {

    void validateCredentials(String username, String password, String passwordRepeat);

    void onDestroy();
}
