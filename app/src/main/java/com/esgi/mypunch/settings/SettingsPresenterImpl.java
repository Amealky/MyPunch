package com.esgi.mypunch.settings;

import android.bluetooth.BluetoothAdapter;

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
    public void clickBluetooth() {
        if(mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();
        } else {
            mBluetoothAdapter.enable();
        }
    }

}
