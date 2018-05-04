package com.esgi.mypunch.login;


public interface OnLoginFinishedListener {

    void onUsernameError(String msg);

    void onPasswordError(String msg);

    void onServerError(String msg);

    void onSuccess();
}
