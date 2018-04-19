package com.example.caroline.mypunch.login;


public interface LoginPresenter {

    void validateCredentials(String username, String password);

    void onDestroy();
}
