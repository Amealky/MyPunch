package com.esgi.mypunch;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.esgi.mypunch.services.BluetoothLEService;

public class BaseActivity extends AppCompatActivity {

    protected BluetoothLEService.LocalBinder mBinder;
    protected BluetoothLEService mBluetoothLeService;
    protected ServiceConnection mServiceConnection;
    protected BroadcastReceiver broadcastReceiver;
    protected Intent bindIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        bindIntent = new Intent(this, BluetoothLEService.class);


        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy(){

        super.onDestroy();

    }

    public BluetoothLEService getBluetoothLeService(){
        return mBluetoothLeService;
    }

    public ServiceConnection getmServiceConnection() {
        return mServiceConnection;
    }

    public void setmBluetoothLeService(BluetoothLEService mBluetoothLeService){
        this.mBluetoothLeService = mBluetoothLeService;
    }

    public void setBroadcastReceiver(BroadcastReceiver broadcastReceiver){
        this.broadcastReceiver = broadcastReceiver;
    }

    public void setmServiceConnection(ServiceConnection mServiceConnection){
        this.mServiceConnection = mServiceConnection;
    }


    public void service_init() {
        this.startService(bindIntent);
        this.bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, makeGattUpdateIntentFilter());
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
}
