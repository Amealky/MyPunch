package com.esgi.mypunch.navbar;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.esgi.mypunch.R;
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

public class NavContentActivity extends AppCompatActivity implements NavContentView {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.navigation_bar)
    NavigationTabStrip appNavigationTabStrip;

    private NavPageAdapter navPageAdapter;

    private NavContentPresenter navContentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navcontent);
        ButterKnife.bind(this);
        navContentPresenter = new NavContentPresenterImpl(this);


        List<Fragment> fragments = new Vector<>();

        fragments.add(Fragment.instantiate(this, PunchListFragment.class.getName()));
        fragments.add(new Fragment());

        navPageAdapter = new NavPageAdapter(super.getSupportFragmentManager(), fragments);

        viewPager.setAdapter(navPageAdapter);
        appNavigationTabStrip.setViewPager(viewPager, 0);
        appNavigationTabStrip.setTitles("Sessions", "Synthèse");
        appNavigationTabStrip.setTabIndex(0, true);
        appNavigationTabStrip.setStripColor(Color.RED);
        appNavigationTabStrip.setTypeface("fonts/typeface.ttf");

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        boolean hasBluetooth = sharedPreferences.getBoolean("pref_bluetooth_checkbox", );
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(hasBluetooth){
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
        }else{
           /* if(mBluetoothAdapter.isEnabled()){
                sharedPreferences.
            }*/
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
