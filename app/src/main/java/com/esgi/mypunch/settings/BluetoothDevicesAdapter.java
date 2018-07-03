package com.esgi.mypunch.settings;


import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.esgi.mypunch.R;



import java.util.List;


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
        @BindView(R.id.connectProgressBar)
        ProgressBar connectProgressBar;
        @BindView(R.id.iconBluetooth)
        ImageView iconBluetooth;
        @BindView(R.id.disconnectBluetooth)
        ImageButton disconnectBluetooth;

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
    public void onBindViewHolder(final BluetoothDevicesAdapter.MyViewHolder holder, int position) {


        Log.d(TAG, "onBindViewHolder");
        holder.connectProgressBar.setVisibility(View.GONE);
        holder.disconnectBluetooth.setVisibility(View.GONE);
        final BluetoothDevice device = bluetoothDevices.get(position);

        if(device.getName() != null){
            holder.titleDevice.setText(device.getName());
        }else {
            holder.titleDevice.setText(R.string.no_name);
        }

        holder.addressDevice.setText(device.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.iconBluetooth.setVisibility(View.GONE);
                holder.connectProgressBar.setVisibility(View.VISIBLE);

                if(listener.onDeviceClick(device)){
                    holder.iconBluetooth.setImageResource(R.drawable.ic_connected_ble);
                    holder.disconnectBluetooth.setVisibility(View.VISIBLE);
                }
                holder.connectProgressBar.setVisibility(View.GONE);
                holder.iconBluetooth.setVisibility(View.VISIBLE);
            }
        });
        holder.disconnectBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener.onDisconnectBluetoothClick(device)){
                    holder.iconBluetooth.setImageResource(R.drawable.ic_disconnected_ble);
                    holder.disconnectBluetooth.setVisibility(View.GONE);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }

    public interface BluetoothDeviceAdapterListener {
        boolean onDeviceClick(BluetoothDevice device);
        boolean onDisconnectBluetoothClick(BluetoothDevice device);
    }
}
