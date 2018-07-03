package com.esgi.mypunch.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.esgi.mypunch.R;
import com.esgi.mypunch.data.SharedPreferencesKeys;
import com.esgi.mypunch.data.mainapi.PunchMyNodeProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingsFragment extends PreferenceFragment {

    public static final String TAG = "SettingsFragment";
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
        String token = prefs.getString(SharedPreferencesKeys.CONNEXION_TOKEN, null);
        // remove from device
        prefs.edit().putString(SharedPreferencesKeys.CONNEXION_TOKEN, null).apply();
        // remove from database
        PunchMyNodeProvider provider = new PunchMyNodeProvider();
        Call<Void> response = provider.logout(token);
        response.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Log.i(TAG, "Successful logout.");
                } else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
}
