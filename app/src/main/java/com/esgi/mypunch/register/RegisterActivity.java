package com.esgi.mypunch.register;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esgi.mypunch.R;

public class RegisterActivity extends AppCompatActivity implements RegisterView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setUsernameError(String msg) {

    }

    @Override
    public void setPasswordError(String msg) {

    }

    @Override
    public void navigateToLogin() {

    }
}
