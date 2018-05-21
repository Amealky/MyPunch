package com.esgi.mypunch.punchlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.esgi.mypunch.R;
import com.esgi.mypunch.data.mainapi.PunchMyNodeProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PunchListActivity extends AppCompatActivity {

    public static final String TAG = "PunchListActivity";
    private PunchMyNodeProvider provider;

    @BindView(R.id.boxingSessionList) RecyclerView sessionsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch_list);

        setTitle("Boxing sessions");
        provider = new PunchMyNodeProvider();
        ButterKnife.bind(this);
    }
}
