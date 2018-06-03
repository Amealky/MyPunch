package com.esgi.mypunch.settings;

public class SettingsPresenterImpl implements SettingsPresenter {

    private SettingsView settingsView;

    SettingsPresenterImpl(SettingsView settingsView) {
        this.settingsView = settingsView;
    }

    @Override
    public void onDestroy() {
        settingsView = null;
    }

}
