package com.esgi.mypunch.login;


public interface OnLoginFinishedListener {

    void onUsernameError();

    void onPasswordError();

    void onServerError();

    void onSuccess();
}
