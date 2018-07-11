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
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.esgi.mypunch.R;
import com.esgi.mypunch.data.BleDevice;
import com.esgi.mypunch.data.enums.CONNECTION_STATE;

import com.esgi.mypunch.services.BluetoothLEService;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;

import com.esgi.mypunch.R;
import com.esgi.mypunch.data.SharedPreferencesManager;
import com.esgi.mypunch.data.mainapi.PunchMyNodeProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class SettingsFragment extends PreferenceFragment implements BluetoothDevicesAdapter.BluetoothDeviceAdapterListener {


    private SettingsActivity parentActivity;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;

    List<BleDevice> listBluetoothDevice;
    List<BleDevice> listBluetoothFavoriteDevice;
    Set<String> setBluetoothDeviceFavoriteName;
    Set<String> setBluetoothDeviceFavoriteAddr;
    BluetoothDevicesAdapter bluetoothDevicesAdapter;
    BleDevice bluetoothConnecting;

    private boolean mScanning = false;


    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;

    CheckBoxPreference checkboxBluetooth;

    public static final String TAG = "SettingsFragment";
    public static final String BLUETOOTH_PREFERENCES = "BluetoothPref";

    Preference buttonDeconnection;
    Preference buttonDevices;

    Dialog dialog;
    ProgressBar dialogProgressBar;
    RecyclerView rvDevices;
    Button bt_Scan;
    TextView tv_emptyList;
    ImageButton bt_exit_dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        parentActivity = (SettingsActivity) getActivity();

        SharedPreferences settings = parentActivity.getSharedPreferences(BLUETOOTH_PREFERENCES, Context.MODE_PRIVATE);
        setBluetoothDeviceFavoriteName = settings.getStringSet("BleName", null);
        setBluetoothDeviceFavoriteAddr = settings.getStringSet("BleAddr", null);
        if(setBluetoothDeviceFavoriteName == null){
           setBluetoothDeviceFavoriteName = new HashSet<String>();
        }
        if(setBluetoothDeviceFavoriteAddr == null){
            setBluetoothDeviceFavoriteAddr = new HashSet<String>();
        }


        dialog = new Dialog(parentActivity);
        View view = parentActivity.getLayoutInflater().inflate(R.layout.dialog_bluetooth_devices, null);

        checkboxBluetooth = (CheckBoxPreference) findPreference("pref_bluetooth_checkbox");
        buttonDeconnection =  findPreference("pref_disconnect");
        buttonDevices = findPreference("pref_bluetooth_devices");
        dialogProgressBar = (ProgressBar) view.findViewById(R.id.scanProgressBar);
        dialogProgressBar.setVisibility(View.GONE);
        rvDevices =  view.findViewById(R.id.bluetoothDevices);
        bt_Scan = (Button) view.findViewById(R.id.scanButton);
        tv_emptyList = (TextView) view.findViewById(R.id.text_empty);
        bt_exit_dialog = (ImageButton) view.findViewById(R.id.exit_dialogButton);

        dialog.setContentView(view);

        rvDevices.setLayoutManager(new LinearLayoutManager(parentActivity));

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
                    if (listBluetoothDevice.isEmpty()) {
                        tv_emptyList.setVisibility(View.VISIBLE);
                    }

                    scanLeDevice(false);

                }else{
                    bt_Scan.setText(R.string.stop_scan_devices);
                    dialogProgressBar.setVisibility(View.VISIBLE);
                    tv_emptyList.setVisibility(View.GONE);
                    scanLeDevice(true);
                }


            }
        });

        bt_exit_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });



        // Check if BLE is supported on the device.
        if (!parentActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(parentActivity,
                    "BLUETOOTH_LE not supported in this device!",
                    Toast.LENGTH_SHORT).show();
            parentActivity.finish();
        }

        getBluetoothAdapterAndLeScanner();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(parentActivity,
                    "bluetoothManager.getAdapter()==null",
                    Toast.LENGTH_SHORT).show();
            parentActivity.finish();
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
            permissionCheck = parentActivity.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheck += parentActivity.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        }
        if (permissionCheck != 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                parentActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }



        mHandler = new Handler();

        checkboxBluetooth.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //Il faut inversé il faut que le presenter envoie la vue et non l'inverse
                parentActivity.getSettingsPresenter().clickBluetooth(checkboxBluetooth.isChecked());
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
                parentActivity.navigateLogin();
                eraseToken();
                return true;
            }
        });
        parentActivity.setBroadcastReceiver(UARTStatusChangeReceiver);
        parentActivity.setmServiceConnection(mServiceConnection);
        parentActivity.service_init();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scanLeDevice(false);
        try {
            LocalBroadcastManager.getInstance(parentActivity).unregisterReceiver(UARTStatusChangeReceiver);
        } catch (Exception ignore) {
            Log.e(TAG, ignore.toString());
        }
        parentActivity.unbindService(parentActivity.getmServiceConnection());
      //  parentActivity.getBluetoothLeService().stopSelf();




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
                mHandler.removeCallbacks(null);
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

            boolean hasDevice = false;
            for(int i = 0; i <  listBluetoothDevice.size(); i++){
                if(listBluetoothDevice.get(i).getBluetoothDevice().getAddress().equals(device.getAddress())){
                    hasDevice = true;
                }
            }
            if(!hasDevice){
                listBluetoothDevice.add(new BleDevice(device));
            }
        }
    };
    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            parentActivity.setmBluetoothLeService(((BluetoothLEService.LocalBinder) rawBinder).getService());
            Log.d("BLESERVICE", "onServiceConnected mService= " + parentActivity.getBluetoothLeService());
            if (!parentActivity.getBluetoothLeService().initialize()) {
                Log.e("BLESERVICE", "Unable to initialize Bluetooth");
                parentActivity.finish();
            }

        }

        public void onServiceDisconnected(ComponentName classname) {
            ////     mService.disconnect(mDevice);
            parentActivity.setmBluetoothLeService(null);
        }


    };

    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            bt_Scan.setEnabled(true);

            //*********************//
            if (action.equals(BluetoothLEService.ACTION_GATT_CONNECTED)) {

                Log.d("BROAD", "UART_CONNECT_MSG");
                bluetoothConnecting.isConnected = CONNECTION_STATE.CONNECTED;
                refreshDialogDevices();
            }

            //*********************//
            if (action.equals(BluetoothLEService.ACTION_GATT_DISCONNECTED)) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d("BROAD", "UART_DISCONNECT_MSG");
                        bluetoothConnecting.isConnected = CONNECTION_STATE.DISCONNECTED;
                        refreshDialogDevices();
                        parentActivity.getBluetoothLeService().close();


                    }
                });
            }

            //*********************//
            if (action.equals(BluetoothLEService.DEVICE_DOES_NOT_SUPPORT_UART)){
                Log.i("BROAD","Device doesn't support UART. Disconnecting");
                parentActivity.getBluetoothLeService().disconnect();
            }


        }
    };

    @Override
    public void onDeviceClick(BleDevice device, int pos) {
        bt_Scan.setEnabled(false);
        onDisconnectBluetoothClick(device);
        swapDevice(device, pos);
        for(BleDevice d : listBluetoothDevice){
            d.isConnected = CONNECTION_STATE.DISCONNECTED;
        }
        refreshDialogDevices();

        parentActivity.getBluetoothLeService().connect(device.getBluetoothDevice().getAddress());
        device.isConnected = CONNECTION_STATE.CONNECTING;
        bluetoothConnecting = device;


    }
    public void swapDevice(BleDevice d, int pos){
        BleDevice dTmp = listBluetoothDevice.get(0);
        listBluetoothDevice.set(0, d);
        listBluetoothDevice.set(pos, dTmp);

    }

    @Override
    public boolean onDisconnectBluetoothClick(BleDevice device) {
        return  parentActivity.getBluetoothLeService().disconnect();
    }

    @Override
    public void onFavoriteDeviceClicked(BleDevice device) {
        SharedPreferences settings = parentActivity.getSharedPreferences(BLUETOOTH_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        if(device.isFavorite){
            if(device.getBluetoothDevice().getName() != null){
                setBluetoothDeviceFavoriteName.add(device.getBluetoothDevice().getName());
            }else{

                setBluetoothDeviceFavoriteName.add(getString(R.string.no_name));
            }

            setBluetoothDeviceFavoriteAddr.add(device.getBluetoothDevice().getAddress());
        }else{
            if(device.getBluetoothDevice().getName() != null){
                setBluetoothDeviceFavoriteName.remove(device.getBluetoothDevice().getName());
            }else{
                setBluetoothDeviceFavoriteName.remove(getString(R.string.no_name));
            }
            setBluetoothDeviceFavoriteAddr.remove(device.getBluetoothDevice().getAddress());
        }


        editor.putStringSet("BleName", setBluetoothDeviceFavoriteName);
        editor.putStringSet("BleAddr", setBluetoothDeviceFavoriteAddr);
        editor.apply();
    }

    private void eraseToken() {
        SharedPreferences prefs = getActivity().getSharedPreferences(SharedPreferencesManager.BASE_KEY, Context.MODE_PRIVATE);
        String token = prefs.getString(SharedPreferencesManager.CONNEXION_TOKEN, null);
        // remove from device
        SharedPreferencesManager.eraseUserData(getActivity());
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

