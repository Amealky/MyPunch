package com.esgi.mypunch.register;


public interface RegisterPresenter {

    void validateCredentials(String email, String firstName, String lastName, String password, String passwordRepeat);

    void onDestroy();
}
