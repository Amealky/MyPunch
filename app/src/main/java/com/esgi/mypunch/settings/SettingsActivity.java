package com.esgi.mypunch.settings;


import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;


import com.esgi.mypunch.R;
import com.esgi.mypunch.login.LoginActivity;
import com.esgi.mypunch.navbar.NavContentActivity;

public class SettingsActivity extends AppCompatActivity implements SettingsView{


    private SettingsPresenter settingsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsPresenter = new SettingsPresenterImpl(this);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

    }

    @Override
    protected void onDestroy() {
        settingsPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.navigateNavContent();
                break;
        }
        return true;
    }


    @Override
    public void navigateNavContent() {
        Intent intent = new Intent(this, NavContentActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public SettingsPresenter getSettingsPresenter() {
        return settingsPresenter;
    }
}
