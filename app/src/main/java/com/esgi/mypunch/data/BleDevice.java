package com.esgi.mypunch.data;

import android.bluetooth.BluetoothDevice;

import com.esgi.mypunch.data.enums.CONNECTION_STATE;

public class BleDevice {

    private BluetoothDevice bluetoothDevice;

    public CONNECTION_STATE isConnected = CONNECTION_STATE.DISCONNECTED;

    public boolean isFavorite = false;

    public BleDevice(BluetoothDevice bluetoothDevice){
        this.bluetoothDevice = bluetoothDevice;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

}
