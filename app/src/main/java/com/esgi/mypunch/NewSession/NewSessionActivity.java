package com.esgi.mypunch.NewSession;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.esgi.mypunch.BaseActivity;
import com.esgi.mypunch.R;
import com.esgi.mypunch.data.BleDevice;
import com.esgi.mypunch.data.enums.CONNECTION_STATE;
import com.esgi.mypunch.navbar.NavContentActivity;
import com.esgi.mypunch.services.BluetoothLEService;
import com.esgi.mypunch.settings.SettingsFragment;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewSessionActivity extends BaseActivity {


    public enum DIALOGS{DEBUT, FIN};

    int dhours, dminute, dseconde;
    int fhours, fminute, fseconde;

    boolean sessionRunning = false;
    //Activity elements
    @BindView(R.id.bt_dateStart)
    Button bt_dateStart;

    @BindView(R.id.bt_dateEnd)
    Button bt_dateEnd;

    @BindView(R.id.bt_startSession)
    Button bt_startSession;

    //Dialog elements
    Dialog dialog;

    DIALOGS type = DIALOGS.DEBUT;

    TextView dialogTitle;

    ImageButton bt_exit_dialog;

    Button bt_reset;

    Button bt_validate;

    NumberPicker np_hours;

    NumberPicker np_minute;

    NumberPicker np_seconde;

    View dialog_view;

    View view_stop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);
        ButterKnife.bind(this);


        //mBluetoothLeService = mBinder.getService();
        dhours = dminute = dseconde = fhours = fminute = fseconde = 0;

        dialog = new Dialog(this);
        dialog_view = this.getLayoutInflater().inflate(R.layout.dialog_time_picker, null);
        np_hours = (NumberPicker) dialog_view.findViewById(R.id.np_hours);
        np_minute = (NumberPicker) dialog_view.findViewById(R.id.np_minute);
        np_seconde = (NumberPicker) dialog_view.findViewById(R.id.np_seconde);
        dialogTitle = (TextView) dialog_view.findViewById(R.id.timePickerTitle);
        bt_exit_dialog = (ImageButton) dialog_view.findViewById(R.id.exit_dialogButton);
        bt_reset = (Button) dialog_view.findViewById(R.id.bt_reset);
        bt_validate = (Button) dialog_view.findViewById(R.id.bt_validate);
        resetPicker();
        dialog.setContentView(dialog_view);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        bt_exit_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();
            }
        });

        bt_dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTitle.setText(R.string.commence_dans);
                type = DIALOGS.DEBUT;

                showDialogPicker();
            }
        });

        bt_dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTitle.setText(R.string.termine_dans);
                type = DIALOGS.FIN;
                showDialogPicker();
            }
        });


        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                np_hours.setValue(0);
                np_minute.setValue(0);
                np_seconde.setValue(0);
            }
        });

        bt_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type == DIALOGS.DEBUT){
                    dhours = np_hours.getValue();
                    dminute = np_minute.getValue();
                    dseconde = np_seconde.getValue();
                }else{
                    fhours = np_hours.getValue();
                    fminute = np_minute.getValue();
                    fseconde = np_seconde.getValue();
                }

                refreshDateView();
                dialog.cancel();

            }
        });

        bt_startSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sessionRunning){
                    bt_startSession.setText(R.string.start);
                    sessionRunning = false;
                    mBluetoothLeService.disableTXNotification();
                }else{
                    bt_startSession.setText(R.string.stop);
                    sessionRunning = true;
                    mBluetoothLeService.enableTXNotification();

                }


            }
        });

        setBroadcastReceiver(UARTStatusChangeReceiver);
        setmServiceConnection(mServiceConnection);
        service_init();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
        } catch (Exception ignore) {

        }
        this.unbindService(this.getmServiceConnection());
        mBluetoothLeService.disableTXNotification();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, NavContentActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

    public void slideToTop(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,view.getHeight(),0);
        animate.setDuration(100);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }


    public void resetPicker(){
        np_hours.setMaxValue(24);
        np_hours.setMinValue(0);

        np_minute.setMaxValue(60);
        np_minute.setMinValue(0);

        np_seconde.setMaxValue(60);
        np_seconde.setMinValue(0);
    }

    public void refreshDateView(){

        if(hasTime(dhours, dminute, dseconde)){
            bt_dateStart.setText(dhours + "H " + dminute + "m " + dseconde + "s");
        }else{
            bt_dateStart.setText(R.string.indetermine);
        }

        if(hasTime(fhours, fminute, fseconde)){
            bt_dateEnd.setText(fhours + "H " + fminute + "m " + fseconde + "s");
        }else{
            bt_dateEnd.setText(R.string.indetermine);
        }


    }

    public boolean hasTime(int h, int m, int s){
        if(h == 0 && m == 0 && s == 0){
            return false;
        }
        return true;

    }


    public void showDialogPicker() {

        if(type == DIALOGS.DEBUT){
            np_hours.setValue(dhours);
            np_minute.setValue(dminute);
            np_seconde.setValue(dseconde);
        }else{
            np_hours.setValue(fhours);
            np_minute.setValue(fminute);
            np_seconde.setValue(fseconde);

        }

        dialog.show();
    }



    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {

            setmBluetoothLeService(((BluetoothLEService.LocalBinder) rawBinder).getService());



        }

        public void onServiceDisconnected(ComponentName classname) {
            ////     mService.disconnect(mDevice);
            //mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();



            if (action.equals(BluetoothLEService.ACTION_DATA_AVAILABLE)) {

                final byte[] txValue = intent.getByteArrayExtra(BluetoothLEService.EXTRA_DATA);
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            String text = new String(txValue, "UTF-8");
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
