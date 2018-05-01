package com.esgi.mypunch.register;


public interface OnRegisterFinishedListener {

    void onUsernameError(String msg);

    void onPasswordError(String msg);

    void onServerError(String msg);

    void onSuccess();
}
