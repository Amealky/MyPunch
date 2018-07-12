package com.esgi.mypunch.navbar;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.esgi.mypunch.BaseActivity;
import com.esgi.mypunch.R;
import com.esgi.mypunch.Synthesis.SynthesisFragment;
import com.esgi.mypunch.login.LoginPresenter;
import com.esgi.mypunch.login.LoginPresenterImpl;
import com.esgi.mypunch.login.LoginView;
import com.esgi.mypunch.punchlist.PunchListFragment;
import com.esgi.mypunch.settings.SettingsActivity;
import com.esgi.mypunch.settings.SettingsFragment;
import com.gigamole.navigationtabstrip.NavigationTabStrip;

import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavContentActivity extends BaseActivity implements NavContentView {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.navigation_bar)
    NavigationTabStrip appNavigationTabStrip;

    private NavPageAdapter navPageAdapter;

    private NavContentPresenter navContentPresenter;

    private static final int RQS_ENABLE_BLUETOOTH = 1;

    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navcontent);
        ButterKnife.bind(this);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        navContentPresenter = new NavContentPresenterImpl(this);

        List<Fragment> fragments = new Vector<>();

        fragments.add(Fragment.instantiate(this, PunchListFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, SynthesisFragment.class.getName()));

        navPageAdapter = new NavPageAdapter(super.getSupportFragmentManager(), fragments);

        viewPager.setAdapter(navPageAdapter);
        appNavigationTabStrip.setViewPager(viewPager, 0);
        appNavigationTabStrip.setTitles("Sessions", "Synthèse");
        appNavigationTabStrip.setTabIndex(0, true);
        appNavigationTabStrip.setTypeface("fonts/typeface.ttf");

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean hasBluetooth = sharedPreferences.getBoolean("pref_bluetooth_checkbox", true);


        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, RQS_ENABLE_BLUETOOTH);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// Check which request we're responding to
        if (requestCode == RQS_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                mBluetoothAdapter.enable();
            } else {
                mBluetoothAdapter.disable();
            }
        }
    }

    @Override
    protected void onDestroy() {
        navContentPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu à l'ActionBar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //gère le click sur une action de l'ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                this.navigateSettings();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void navigateSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}
