package com.esgi.mypunch.settings;

public interface SettingsPresenter {


    void validateCredentials(String username, String password, String passwordRepeat);

    void onDestroy();
}
