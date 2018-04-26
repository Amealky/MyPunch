package com.esgi.mypunch.login;


public interface LoginPresenter {

    void validateCredentials(String username, String password);

    void onDestroy();
}
