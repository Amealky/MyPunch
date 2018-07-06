package com.esgi.mypunch.NewSession;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.esgi.mypunch.R;
import com.esgi.mypunch.navbar.NavContentActivity;
import com.esgi.mypunch.settings.SettingsFragment;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewSessionActivity extends AppCompatActivity {



    //Activity elements
    @BindView(R.id.bt_dateStart)
    Button bt_dateStart;

    @BindView(R.id.bt_dateEnd)
    Button bt_dateEnd;

    //Dialog elements
    Dialog dialog;


    TextView dialogTitle;

    ImageButton bt_exit_dialog;

    Button bt_reset;

    Button bt_validate;

    NumberPicker np_hours;

    NumberPicker np_minute;

    NumberPicker np_seconde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);
        ButterKnife.bind(this);

        dialog = new Dialog(this);
        View view = this.getLayoutInflater().inflate(R.layout.dialog_time_picker, null);
        np_hours = (NumberPicker) view.findViewById(R.id.np_hours);
        np_minute = (NumberPicker) view.findViewById(R.id.np_minute);
        np_seconde = (NumberPicker) view.findViewById(R.id.np_seconde);
        dialogTitle = (TextView) view.findViewById(R.id.timePickerTitle);
        bt_exit_dialog = (ImageButton) view.findViewById(R.id.exit_dialogButton);
        bt_reset = (Button) view.findViewById(R.id.bt_reset);
        bt_validate = (Button) view.findViewById(R.id.bt_validate);
        resetPicker();
        dialog.setContentView(view);

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
                showDialogPicker();
            }
        });

        bt_dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTitle.setText(R.string.termine_dans);
                showDialogPicker();
            }
        });

        np_hours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

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

    public void resetPicker(){
        np_hours.setMaxValue(60);
        np_hours.setMinValue(0);

        np_minute.setMaxValue(60);
        np_minute.setMinValue(0);

        np_seconde.setMaxValue(60);
        np_seconde.setMinValue(0);
    }

    public void showDialogPicker() {

        resetPicker();
        dialog.show();
    }
}
