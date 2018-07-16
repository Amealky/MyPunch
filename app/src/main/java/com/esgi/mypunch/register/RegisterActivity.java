package com.esgi.mypunch.register;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.esgi.mypunch.BaseActivity;
import com.esgi.mypunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements RegisterView {

    @BindView(R.id.registerEmailField) TextView emailField;
    @BindView(R.id.registerFirstnameField) TextView firstName;
    @BindView(R.id.registerLastnameField) TextView lastName;
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
        String l_email = emailField.getText().toString();
        String l_firstname = firstName.getText().toString();
        String l_lastname = lastName.getText().toString();
        String l_pswd = registerPswdField.getText().toString();
        String l_pswdRepeat = repeatPswdField.getText().toString();
        registerPresenter.validateCredentials(l_email, l_firstname, l_lastname, l_pswd, l_pswdRepeat);
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
        finish();
    }
}
