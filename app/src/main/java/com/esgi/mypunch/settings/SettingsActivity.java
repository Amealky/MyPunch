package com.esgi.mypunch.settings;


import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.esgi.mypunch.R;
import com.esgi.mypunch.navbar.NavContentActivity;

public class SettingsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);



               /*Preference checkboxBluetooth =  findPreference("pref_bluetooth_checkbox");
        checkboxBluetooth.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //activate or desactivate bluetooth here
                return true;
            }
        });

        Preference bluetoothList =  findPreference("pref_list");
        checkboxBluetooth.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //do with selected bluetooth
                return true;
            }
        });

        Preference disconnectUser = findPreference("pref_disconnect");
        checkboxBluetooth.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //disconnect and redirect to login page
                return true;
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, NavContentActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }


}
