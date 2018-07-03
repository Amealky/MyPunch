package com.esgi.mypunch.login;


import com.esgi.mypunch.data.dtos.Token;

public interface LoginPresenter {

    void validateCredentials(String username, String password);

    void onDestroy();

    void saveToken(Token token);

    void checkToken();
}
