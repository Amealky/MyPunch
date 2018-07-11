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
import com.esgi.mypunch.data.BleDevice;
import com.esgi.mypunch.data.enums.CONNECTION_STATE;


import java.util.HashSet;
import java.util.List;
import java.util.Set;


import butterknife.BindView;
import butterknife.ButterKnife;

public class BluetoothDevicesAdapter extends RecyclerView.Adapter<BluetoothDevicesAdapter.MyViewHolder> {
    private List<BleDevice> bluetoothDevices;
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
        @BindView(R.id.favoriteBluetooth)
        ImageButton favoriteBluetooth;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Log.d(TAG, "view holder called");
        }
    }

    public BluetoothDevicesAdapter(List<BleDevice> bluetoothDevices) {
        Log.d(TAG, "BluetoothDevicesAdapter");
        this.bluetoothDevices = bluetoothDevices;
    }

    public void updateData(List<BleDevice> bluetoothDevices) {
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
    public void onBindViewHolder(final BluetoothDevicesAdapter.MyViewHolder holder, final int position) {
        final BleDevice device = bluetoothDevices.get(position);

        if(device.getBluetoothDevice().getName() != null){
            holder.titleDevice.setText(device.getBluetoothDevice().getName());
        }else {
            holder.titleDevice.setText(R.string.no_name);
        }

        refreshHolder(device, holder);

        holder.addressDevice.setText(device.getBluetoothDevice().getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(device.isConnected == CONNECTION_STATE.DISCONNECTED){
                    listener.onDeviceClick(device, position);
                    refreshHolder(device, holder);
                }

            }
        });
        holder.disconnectBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDisconnectBluetoothClick(device);
                refreshHolder(device, holder);
            }
        });
        holder.favoriteBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Set the values

                device.isFavorite = !device.isFavorite;
                refreshHolder(device, holder);
                listener.onFavoriteDeviceClicked(device);

            }
        });



    }

    public void refreshHolder(BleDevice device, MyViewHolder holder){
        if(device.isConnected == CONNECTION_STATE.DISCONNECTED){
            holder.connectProgressBar.setVisibility(View.GONE);
            holder.disconnectBluetooth.setVisibility(View.GONE);
            holder.iconBluetooth.setImageResource(R.drawable.ic_disconnected_ble);
            holder.iconBluetooth.setVisibility(View.VISIBLE);
        }
        if(device.isConnected == CONNECTION_STATE.CONNECTING){
            holder.iconBluetooth.setVisibility(View.GONE);
            holder.disconnectBluetooth.setVisibility(View.GONE);
            holder.connectProgressBar.setVisibility(View.VISIBLE);
        }
        if(device.isConnected == CONNECTION_STATE.CONNECTED){
            holder.connectProgressBar.setVisibility(View.GONE);
            holder.iconBluetooth.setVisibility(View.VISIBLE);
            holder.iconBluetooth.setImageResource(R.drawable.ic_connected_ble);
            holder.disconnectBluetooth.setVisibility(View.VISIBLE);
        }

        if(device.isFavorite){
            holder.favoriteBluetooth.setImageResource(R.drawable.ic_favorite_fill);
        }else{
            holder.favoriteBluetooth.setImageResource(R.drawable.ic_favorite_empty);
        }
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }

    public interface BluetoothDeviceAdapterListener {
        void onDeviceClick(BleDevice device, int pos);
        boolean onDisconnectBluetoothClick(BleDevice device);
        void onFavoriteDeviceClicked(BleDevice device);
    }
}
