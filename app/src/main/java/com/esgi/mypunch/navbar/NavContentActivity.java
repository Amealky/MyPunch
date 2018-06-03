package com.esgi.mypunch.navbar;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.esgi.mypunch.R;
import com.esgi.mypunch.punchlist.PunchListFragment;
import com.esgi.mypunch.settings.SettingsActivity;
import com.esgi.mypunch.settings.SettingsFragment;
import com.gigamole.navigationtabstrip.NavigationTabStrip;

import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavContentActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.navigation_bar)
    NavigationTabStrip appNavigationTabStrip;

    NavPageAdapter navPageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navcontent);
        ButterKnife.bind(this);


        List<Fragment> fragments = new Vector<>();

        fragments.add(Fragment.instantiate(this, PunchListFragment.class.getName()));

        navPageAdapter = new NavPageAdapter(super.getSupportFragmentManager(), fragments);

        viewPager.setAdapter(navPageAdapter);
        appNavigationTabStrip.setViewPager(viewPager, 1);

        appNavigationTabStrip.setTitles("Sessions", "Synthèse");
        appNavigationTabStrip.setTabIndex(1, true);
        appNavigationTabStrip.setStripColor(Color.RED);
        appNavigationTabStrip.setTypeface("fonts/typeface.ttf");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu_test à l'ActionBar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //gère le click sur une action de l'ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                callSettings();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void callSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}
