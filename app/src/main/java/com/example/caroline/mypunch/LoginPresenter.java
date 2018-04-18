package com.example.caroline.mypunch;

/**
 * Created by caroline on 18/04/2018.
 */

public interface LoginPresenter {

    void validateCredentials(String username, String password);

    void onDestroy();
}
