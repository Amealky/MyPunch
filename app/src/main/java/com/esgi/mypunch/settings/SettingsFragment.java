package com.esgi.mypunch.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;


public class SettingsFragment extends PreferenceFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Preference checkboxBluetooth = (Preference) findPreference("pref_bluetooth_checkbox");
        checkboxBluetooth.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //activate or desactivate bluetooth here
                return true;
            }
        });

        Preference bluetoothList = (Preference) findPreference("pref_list");
        checkboxBluetooth.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //do with selected bluetooth
                return true;
            }
        });

        Preference disconnectUser = (Preference) findPreference("pref_disconnect");
        checkboxBluetooth.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //disconnect and redirect to login page
                return true;
            }
        });
    }
}
