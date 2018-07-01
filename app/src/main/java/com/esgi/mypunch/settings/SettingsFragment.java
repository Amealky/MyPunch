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
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.esgi.mypunch.R;
import com.esgi.mypunch.services.BluetoothLEService;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class SettingsFragment extends PreferenceFragment implements BluetoothDevicesAdapter.BluetoothDeviceAdapterListener {


    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothLEService mBluetoothLeService;

    List<BluetoothDevice> listBluetoothDevice;
    BluetoothDevicesAdapter bluetoothDevicesAdapter;

    private boolean mScanning = false;


    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;

    CheckBoxPreference checkboxBluetooth;
    Preference buttonDeconnection;
    Preference buttonDevices;

    Dialog dialog;
    ProgressBar dialogProgressBar;
    RecyclerView rvDevices;
    Button bt_Scan;
    TextView tv_emptyList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        dialog = new Dialog(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_bluetooth_devices, null);

        checkboxBluetooth = (CheckBoxPreference) findPreference("pref_bluetooth_checkbox");
        buttonDeconnection =  findPreference("pref_disconnect");
        buttonDevices = findPreference("pref_bluetooth_devices");
        dialogProgressBar = (ProgressBar) view.findViewById(R.id.scanProgressBar);
        dialogProgressBar.setVisibility(View.GONE);
        rvDevices =  view.findViewById(R.id.bluetoothDevices);
        bt_Scan = (Button) view.findViewById(R.id.scanButton);
        tv_emptyList = (TextView) view.findViewById(R.id.text_empty);

        dialog.setContentView(view);

        rvDevices.setLayoutManager(new LinearLayoutManager(getActivity()));

        listBluetoothDevice = new ArrayList<>();

        bluetoothDevicesAdapter = new BluetoothDevicesAdapter(listBluetoothDevice);
        bluetoothDevicesAdapter.setListener(this);
        rvDevices.setAdapter(bluetoothDevicesAdapter);

        bt_Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mScanning){
                    bt_Scan.setText(R.string.scan_devices);
                    dialogProgressBar.setVisibility(View.GONE);
                    tv_emptyList.setVisibility(View.VISIBLE);
                    scanLeDevice(false);

                }else{
                    bt_Scan.setText(R.string.stop_scan_devices);
                    dialogProgressBar.setVisibility(View.VISIBLE);
                    tv_emptyList.setVisibility(View.GONE);
                    scanLeDevice(true);
                }


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
        }else {
            if(mBluetoothAdapter.isEnabled()){
                checkboxBluetooth.setChecked(true);
            }else{
                checkboxBluetooth.setChecked(false);
            }
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



        mHandler = new Handler();

        checkboxBluetooth.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //Il faut inversé il faut que le presenter envoie la vue et non l'inverse
                ((SettingsActivity)getActivity()).getSettingsPresenter().clickBluetooth(checkboxBluetooth.isChecked());
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

    public void refreshDialogDevices(){
        bluetoothDevicesAdapter = new BluetoothDevicesAdapter(listBluetoothDevice);
        bluetoothDevicesAdapter.setListener(this);
        rvDevices.setAdapter(bluetoothDevicesAdapter);
        Log.i("LIST SIZE : ", String.valueOf(listBluetoothDevice.size()));
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (enable) {

                // Stops scanning after a pre-defined scan period.
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mBluetoothLeScanner.stopScan(scanCallback);

                        //listViewLE.invalidateViews();

                        Toast.makeText(getActivity(),
                                "Scan timeout",
                                Toast.LENGTH_LONG).show();

                        dialogProgressBar.setVisibility(View.GONE);
                        bt_Scan.setText(R.string.scan_devices);
                        if (listBluetoothDevice.isEmpty()) {
                            tv_emptyList.setVisibility(View.VISIBLE);
                        } else {
                            tv_emptyList.setVisibility(View.GONE);
                        }

                        mScanning = false;

                    }
                }, SCAN_PERIOD);


                mBluetoothLeScanner.startScan(scanCallback);

                mScanning = true;

            } else {

                mBluetoothLeScanner.stopScan(scanCallback);

                mScanning = false;
            }

        }else{
            Toast.makeText(getActivity(), "Votre version ne supporte pas cette fonction", Toast.LENGTH_SHORT);
        }
    }

    private ScanCallback scanCallback = new ScanCallback() {


        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {


                addBluetoothDevice(result.getDevice());
                rvDevices.invalidate();
                refreshDialogDevices();

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
            /*    private void addDevice(BluetoothDevice device, int rssi) {
        boolean deviceFound = false;

        for (BluetoothDevice listDev : deviceList) {
            if (listDev.getAddress().equals(device.getAddress())) {
                deviceFound = true;
                break;
            }
        }


        devRssiValues.put(device.getAddress(), rssi);
        if (!deviceFound) {
        	deviceList.add(device);
            mEmptyList.setVisibility(View.GONE);




            deviceAdapter.notifyDataSetChanged();
        }*/
            if(!listBluetoothDevice.contains(device)){

                        boolean hasDevice = false;
                        for(int i = 0; i <  listBluetoothDevice.size(); i++){
                            if(listBluetoothDevice.get(i).getAddress() == device.getAddress()){
                                hasDevice = true;
                            }
                        }
                        if(!hasDevice){
                            listBluetoothDevice.add(device);
                        }

            }
        }
    };

    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //*********************//
            if (action.equals(BluetoothLEService.ACTION_GATT_CONNECTED)) {
                Log.d("BROAD", "UART_CONNECT_MSG");

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

    @Override
    public boolean onDeviceClick(BluetoothDevice device) {
        return mBluetoothLeService.connect(device.getAddress());
    }

    @Override
    public boolean onDisconnectBluetoothClick(BluetoothDevice device) {
        return  mBluetoothLeService.disconnect();
    }
}
