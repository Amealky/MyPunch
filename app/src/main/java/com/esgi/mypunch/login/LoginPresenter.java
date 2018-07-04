package com.esgi.mypunch.login;


import com.esgi.mypunch.data.dtos.User;

public interface LoginPresenter {

    void validateCredentials(String username, String password);

    void onDestroy();

    void saveUser(User user);

    void checkToken();
}
