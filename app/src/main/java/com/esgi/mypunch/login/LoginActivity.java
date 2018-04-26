package com.esgi.mypunch.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.esgi.mypunch.R;
import com.esgi.mypunch.register.RegisterActivity;

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
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToPunchList() {
        // TODO
    }
}
