package com.esgi.mypunch.settings;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esgi.mypunch.R;
import com.esgi.mypunch.services.BluetoothLEService;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SettingsFragment extends PreferenceFragment {


    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothLEService mBluetoothLeService;

    List<BluetoothDevice> listBluetoothDevice;
    ListAdapter adapterLeScanResult;

    private boolean mScanning;

    private static final int RQS_ENABLE_BLUETOOTH = 1;

    private Handler mHandler;
    private static final long SCAN_PERIOD = 3000;

    Preference checkboxBluetooth;
    Preference buttonDeconnection;
    Preference buttonDevices;

    Dialog dialog;
    ListView rvDevices;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        checkboxBluetooth =  findPreference("pref_bluetooth_checkbox");
        buttonDeconnection =  findPreference("pref_disconnect");
        buttonDevices = findPreference("pref_bluetooth_devices");

        dialog = new Dialog(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_bluetooth_devices, null);

        rvDevices =  view.findViewById(R.id.bluetoothDevices);

        dialog.setContentView(view);
        Button bt_Scan = (Button) view.findViewById(R.id.scanButton);
        bt_Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanLeDevice(true);

            }
        });

        rvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mBluetoothLeService.connect(((BluetoothDevice) adapterView.getAdapter().getItem(i)).getAddress());
            }
        });


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
                if(((SettingsActivity)getActivity()).getSettingsPresenter().clickBluetooth(checkboxBluetooth.isEnabled())){

                } else {

                }
                return true;
            }
        });

        buttonDevices.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showDialogDevices();
                return  true;
            }
        });



        buttonDeconnection.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                ((SettingsView)getActivity()).navigateLogin();
                return true;
            }
        });
     //   scanLeDevice(true);
        service_init();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(mServiceConnection);
        mBluetoothLeService.stopSelf();
        mBluetoothLeService = null;

    }

    public void showDialogDevices() {

        dialog.show();
    }

    public void refreshDialogDevices(ListAdapter adapterLeScanResult){
        this.rvDevices.setAdapter(adapterLeScanResult);
        Log.i("LIST SIZE : ", String.valueOf(adapterLeScanResult.getCount()));
    }



    private void service_init() {
        Intent bindIntent;
        bindIntent = new Intent(getActivity(), BluetoothLEService.class);
        getActivity().bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLEService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLEService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLEService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLEService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLEService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }

    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mBluetoothLeService = ((BluetoothLEService.LocalBinder) rawBinder).getService();
            Log.d("BLESERVICE", "onServiceConnected mService= " + mBluetoothLeService);
            if (!mBluetoothLeService.initialize()) {
                Log.e("BLESERVICE", "Unable to initialize Bluetooth");
                getActivity().finish();
            }

        }

        public void onServiceDisconnected(ComponentName classname) {
            ////     mService.disconnect(mDevice);
            mBluetoothLeService = null;
        }
    };

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
                    rvDevices.invalidate();
                    refreshDialogDevices(adapterLeScanResult);
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
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {


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
                    if(device.getName() != null){
                        Log.i("NAME  ", device.getName());
                        listBluetoothDevice.add(device);
                    }



                //listViewLE.invalidateViews();
            }
        }
    };

    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            final Intent mIntent = intent;
            //*********************//
            if (action.equals(BluetoothLEService.ACTION_GATT_CONNECTED)) {
                getActivity().closeContextMenu();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                        Log.d("BROAD", "UART_CONNECT_MSG");

                    }
                });
            }

            //*********************//
            if (action.equals(BluetoothLEService.ACTION_GATT_DISCONNECTED)) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                        Log.d("BROAD", "UART_DISCONNECT_MSG");


                        mBluetoothLeService.close();
                        //setUiState();

                    }
                });
            }


            //*********************//
            if (action.equals(BluetoothLEService.ACTION_GATT_SERVICES_DISCOVERED)) {
                mBluetoothLeService.enableTXNotification();
            }
            //*********************//
            if (action.equals(BluetoothLEService.ACTION_DATA_AVAILABLE)) {

                final byte[] txValue = intent.getByteArrayExtra(BluetoothLEService.EXTRA_DATA);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            String text = new String(txValue, "UTF-8");
                            String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                            Log.i("TX", text);
                        } catch (Exception e) {
                            Log.e("BROAD", e.toString());
                        }
                    }
                });
            }
            //*********************//
            if (action.equals(BluetoothLEService.DEVICE_DOES_NOT_SUPPORT_UART)){
                Log.i("BROAD","Device doesn't support UART. Disconnecting");
                mBluetoothLeService.disconnect();
            }


        }
    };
}
