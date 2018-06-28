package com.esgi.mypunch.settings;

import android.widget.ListAdapter;

public interface SettingsPresenter {


    void onDestroy();

    void clickBluetooth();

    void clickDevices(ListAdapter adapterLeScanResult);
}
