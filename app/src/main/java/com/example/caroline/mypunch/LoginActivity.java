package com.example.caroline.mypunch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.pseudoField) EditText pseudoField;
    @BindView(R.id.pswdField) EditText pswdField;
    @BindView(R.id.loginButton) Button loginButton;
    @BindView(R.id.registerButton) Button registerButton;
    @BindView(R.id.loginProgressBar) ProgressBar loginProgressBar;

    private LoginPresenter loginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPresenter = new LoginPresenterImpl(this);
        ButterKnife.bind(this);
        loginProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        loginPresenter.onDestroy();
        super.onDestroy();
    }

    @OnClick(R.id.loginButton) public void clickOnLogin() {
        showProgress();

        String pseudo = pseudoField.getText().toString();
        String pwsd = pseudoField.getText().toString();
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
    public void setUsernameError() {
        String msg = "Unknown user " + pseudoField.getText().toString();
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPasswordError() {
        Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateRegister() {

    }

    @Override
    public void navigateToPunchList() {

    }
}
