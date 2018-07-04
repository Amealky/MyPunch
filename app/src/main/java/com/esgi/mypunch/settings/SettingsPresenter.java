package com.esgi.mypunch.settings;

import android.widget.ListAdapter;

public interface SettingsPresenter {


    boolean hasBluetooth = false;

    void onDestroy();

    void clickBluetooth(boolean checkBoxValue);


}

