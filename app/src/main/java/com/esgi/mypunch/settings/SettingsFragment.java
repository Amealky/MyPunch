package com.esgi.mypunch.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.esgi.mypunch.R;
import com.esgi.mypunch.data.SharedPreferencesKeys;


public class SettingsFragment extends PreferenceFragment {


    Preference checkboxBluetooth;
    Preference buttonDeconnection;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        checkboxBluetooth =  findPreference("pref_bluetooth_checkbox");
        buttonDeconnection =  findPreference("pref_disconnect");


        checkboxBluetooth.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //Il faut inversé il faut que le presenter envoie la vue et non l'inverse
                ((SettingsActivity)getActivity()).getSettingsPresenter().clickBluetooth();
                return true;
            }
        });


        buttonDeconnection.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                ((SettingsView)getActivity()).navigateLogin();
                eraseToken();
                return true;
            }
        });

    }

    private void eraseToken() {
        SharedPreferences prefs = getActivity().getSharedPreferences(SharedPreferencesKeys.BASE_KEY, Context.MODE_PRIVATE);
        prefs.edit().putString(SharedPreferencesKeys.CONNEXION_TOKEN, null).apply();
    }
}
