package com.esgi.mypunch.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.esgi.mypunch.BaseActivity;
import com.esgi.mypunch.R;
import com.esgi.mypunch.navbar.NavContentActivity;
import com.esgi.mypunch.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.pseudoField) EditText pseudoField;
    @BindView(R.id.pswdField) EditText pswdField;
    @BindView(R.id.loginButton) Button loginButton;
    @BindView(R.id.registerButton) Button registerButton;
    @BindView(R.id.loginProgressBar) ProgressBar loginProgressBar;

    private LoginPresenter loginPresenter;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        loginPresenter = new LoginPresenterImpl(this);
        ButterKnife.bind(this);
        loginProgressBar.setVisibility(View.GONE);
        loginPresenter.checkToken();
    }

    @Override
    protected void onDestroy() {
        loginPresenter.onDestroy();
        super.onDestroy();
    }

    @OnClick(R.id.loginButton) public void clickOnLogin() {
        String pseudo = pseudoField.getText().toString();
        String pwsd = pswdField.getText().toString();
        loginPresenter.validateCredentials(pseudo, pwsd);
    }

    @OnClick(R.id.registerButton) public void clickOnRegister() {
        navigateRegister();
    }

    @Override
    public void showProgress() {
        loginButton.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);
        loginProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        loginProgressBar.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUsernameError(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPasswordError(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setServerError(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToPunchList() {
        Intent intent = new Intent(this, NavContentActivity.class);
        startActivity(intent);
        finish();
    }
}
