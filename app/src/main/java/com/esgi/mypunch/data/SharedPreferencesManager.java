package com.esgi.mypunch.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.esgi.mypunch.data.dtos.User;


public class SharedPreferencesManager {

    public static final String TAG = "SharedPrefManager";
    public static final String BASE_KEY = "com.esgi.mypunch";
    public static final String CONNEXION_TOKEN = "com.esgi.mypunch.token";
    public static final String ID = "com.esgi.mypunch.userId";
    public static final String EMAIL = "com.esgi.mypunch.email";
    public static final String FIRSTNAME = "com.esgi.mypunch.firstname";
    public static final String LASTNAME = "com.esgi.mypunch.lastname";

    public static User getUser(Context ctxt) {
        SharedPreferences prefs = ctxt.getSharedPreferences(SharedPreferencesManager.BASE_KEY, Context.MODE_PRIVATE);

        int id = prefs.getInt(ID, 0);
        String token = prefs.getString(CONNEXION_TOKEN, null);
        String email = prefs.getString(EMAIL, null);
        String firstname = prefs.getString(FIRSTNAME, null);
        String lastname = prefs.getString(LASTNAME, null);

        return new User(id, email, firstname, lastname, token);
    }

    public static void saveUserData(Context ctxt, User user) {
        SharedPreferences prefs = ctxt.getSharedPreferences(SharedPreferencesManager.BASE_KEY, Context.MODE_PRIVATE);

        prefs.edit().putString(SharedPreferencesManager.CONNEXION_TOKEN, user.getToken()).apply();
        prefs.edit().putInt(SharedPreferencesManager.ID, user.getId()).apply();
        prefs.edit().putString(SharedPreferencesManager.EMAIL, user.getEmail()).apply();
        prefs.edit().putString(SharedPreferencesManager.FIRSTNAME, user.getFirstname()).apply();
        prefs.edit().putString(SharedPreferencesManager.LASTNAME, user.getLastname()).apply();
    }

    public static void eraseUserData(Context ctxt) {
        SharedPreferences prefs = ctxt.getSharedPreferences(SharedPreferencesManager.BASE_KEY, Context.MODE_PRIVATE);

        prefs.edit().putString(SharedPreferencesManager.CONNEXION_TOKEN, null).apply();
        prefs.edit().putString(SharedPreferencesManager.ID, null).apply();
        prefs.edit().putString(SharedPreferencesManager.EMAIL, null).apply();
        prefs.edit().putString(SharedPreferencesManager.FIRSTNAME, null).apply();
        prefs.edit().putString(SharedPreferencesManager.LASTNAME, null).apply();
    }
}
