package com.esgi.mypunch.settings;

import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.widget.ListAdapter;

public class SettingsPresenterImpl implements SettingsPresenter {

    private SettingsView settingsView;

    //Attention le bluetooth ne marche pas sur émulateur
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    SettingsPresenterImpl(SettingsView settingsView) {
        this.settingsView = settingsView;
    }

    @Override
    public void onDestroy() {
        settingsView = null;
    }

    @Override
    public boolean clickBluetooth(boolean checkBoxValue) {
        if(checkBoxValue){
            mBluetoothAdapter.enable();
            return true;
        } else {
            mBluetoothAdapter.disable();
            return false;
        }
    }



}
