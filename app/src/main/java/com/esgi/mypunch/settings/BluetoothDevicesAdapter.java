package com.esgi.mypunch.settings;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esgi.mypunch.R;
import com.esgi.mypunch.data.dtos.BoxingSession;
import com.esgi.mypunch.punchlist.BoxingSessionAdapter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BluetoothDevicesAdapter extends RecyclerView.Adapter<BluetoothDevicesAdapter.MyViewHolder> {
    private List<BluetoothDevice> bluetoothDevices;
    private static final String TAG = "BluetoothDevicesAdapter";

    private BluetoothDeviceAdapterListener listener;

    public void setListener(BluetoothDeviceAdapterListener listener) {
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleDevice)
        TextView titleDevice;
        @BindView(R.id.addressDevice) TextView addressDevice;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Log.d(TAG, "view holder called");
        }
    }

    public BluetoothDevicesAdapter(List<BluetoothDevice> bluetoothDevices) {
        Log.d(TAG, "BluetoothDevicesAdapter");
        this.bluetoothDevices = bluetoothDevices;
    }

    public void updateData(List<BluetoothDevice> bluetoothDevices) {
        this.bluetoothDevices = bluetoothDevices;
    }

    @Override
    public BluetoothDevicesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bluetooth_device_row, parent, false);
        return new BluetoothDevicesAdapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(BluetoothDevicesAdapter.MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        final BluetoothDevice device = bluetoothDevices.get(position);

        holder.titleDevice.setText(device.getName());
        holder.addressDevice.setText(device.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeviceClick(device);
            }
        });


    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }

    public interface BluetoothDeviceAdapterListener {
        void onDeviceClick(BluetoothDevice device);
    }
}
