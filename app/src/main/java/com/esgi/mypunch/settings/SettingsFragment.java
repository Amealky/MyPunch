package com.esgi.mypunch.settings;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.esgi.mypunch.R;
import com.esgi.mypunch.login.LoginActivity;
import com.esgi.mypunch.navbar.NavContentActivity;

import java.util.ArrayList;
import java.util.List;


public class SettingsFragment extends PreferenceFragment {


    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;

    List<BluetoothDevice> listBluetoothDevice;
    ListAdapter adapterLeScanResult;

    private boolean mScanning;

    private static final int RQS_ENABLE_BLUETOOTH = 1;

    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;

    Preference checkboxBluetooth;
    Preference buttonDeconnection;
    Preference buttonDevices;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        checkboxBluetooth =  findPreference("pref_bluetooth_checkbox");
        buttonDeconnection =  findPreference("pref_disconnect");
        buttonDevices = findPreference("pref_bluetooth_devices");

        // Check if BLE is supported on the device.
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(),
                    "BLUETOOTH_LE not supported in this device!",
                    Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        getBluetoothAdapterAndLeScanner();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(),
                    "bluetoothManager.getAdapter()==null",
                    Toast.LENGTH_SHORT).show();
            getActivity().finish();
            return;
        }

        int permissionCheck = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheck = getActivity().checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheck += getActivity().checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        }
        if (permissionCheck != 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }

        listBluetoothDevice = new ArrayList<>();

        adapterLeScanResult = new ArrayAdapter<BluetoothDevice>(
                getActivity(), android.R.layout.simple_list_item_1, listBluetoothDevice);

        mHandler = new Handler();

        checkboxBluetooth.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //Il faut inversé il faut que le presenter envoie la vue et non l'inverse
                ((SettingsActivity)getActivity()).getSettingsPresenter().clickBluetooth();
                return true;
            }
        });

        buttonDevices.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ((SettingsActivity)getActivity()).getSettingsPresenter().clickDevices(adapterLeScanResult);
                return  true;
            }
        });


        buttonDeconnection.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                ((SettingsView)getActivity()).navigateLogin();
                return true;
            }
        });
        scanLeDevice(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, RQS_ENABLE_BLUETOOTH);
            }
        }
    }


    private void getBluetoothAdapterAndLeScanner(){
        // Get BluetoothAdapter and BluetoothLeScanner.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }

        mScanning = false;
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {

            listBluetoothDevice.clear();
            //listViewLE.invalidateViews();

            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBluetoothLeScanner.stopScan(scanCallback);
                    }
                    //listViewLE.invalidateViews();

                    Toast.makeText(getActivity(),
                            "Scan timeout",
                            Toast.LENGTH_LONG).show();

                    mScanning = false;
                    //btnScan.setEnabled(true);
                }
            }, SCAN_PERIOD);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothLeScanner.startScan(scanCallback);
            }
            mScanning = true;
            //btnScan.setEnabled(false);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothLeScanner.stopScan(scanCallback);
            }
            mScanning = false;
           // btnScan.setEnabled(true);
        }
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                addBluetoothDevice(result.getDevice());
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            for(ScanResult result : results){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    addBluetoothDevice(result.getDevice());
                }
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Toast.makeText(getActivity(),
                    "onScanFailed: " + String.valueOf(errorCode),
                    Toast.LENGTH_LONG).show();
        }

        private void addBluetoothDevice(BluetoothDevice device){
            if(!listBluetoothDevice.contains(device)){
                listBluetoothDevice.add(device);
                //listViewLE.invalidateViews();
            }
        }
    };
}
