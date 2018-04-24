package com.esgi.mypunch.register;


public interface RegisterPresenter {

    void validateCredentials(String username, String password);

    void onDestroy();
}
