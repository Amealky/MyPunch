package com.esgi.mypunch.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.esgi.mypunch.R;
import com.esgi.mypunch.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RegisterView {

    @BindView(R.id.registerPseudoField) TextView pseudoField;
    @BindView(R.id.registerPswdField) TextView registerPswdField;
    @BindView(R.id.repeatPswdField) TextView repeatPswdField;
    @BindView(R.id.launchRegistrationButton) TextView launchRegistrationButton;
    @BindView(R.id.registerProgressBar) ProgressBar registerProgressBar;

    private RegisterPresenter registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerPresenter = new RegisterPresenterImpl(this);
        ButterKnife.bind(this);
        registerProgressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.launchRegistrationButton) public void launchRegistration() {
        String username = pseudoField.getText().toString();
        String pswd = registerPswdField.getText().toString();
        String pswdRepeat = repeatPswdField.getText().toString();
        registerPresenter.validateCredentials(username, pswd, pswdRepeat);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerPresenter.onDestroy();
    }

    @Override
    public void showProgress() {
        launchRegistrationButton.setVisibility(View.GONE);
        registerProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        registerProgressBar.setVisibility(View.GONE);
        launchRegistrationButton.setVisibility(View.VISIBLE);
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
    public  void setServerError(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
