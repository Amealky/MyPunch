package com.esgi.mypunch.register;


public interface OnRegisterFinishedListener {

    void onUsernameError();

    void onPasswordError();

    void onServerError();

    void onSuccess();
}
