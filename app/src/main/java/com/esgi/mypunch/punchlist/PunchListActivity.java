package com.esgi.mypunch.punchlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.esgi.mypunch.R;
import com.esgi.mypunch.data.dtos.BoxingSession;
import com.esgi.mypunch.data.mainapi.PunchMyNodeProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PunchListActivity extends AppCompatActivity {

    public static final String TAG = "PunchListActivity";
    private PunchMyNodeProvider provider;
    private List<BoxingSession> sessions;
    private BoxingSessionAdapter adapter;

    @BindView(R.id.boxingSessionList) RecyclerView sessionsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch_list);

        setTitle("Boxing sessions");
        provider = new PunchMyNodeProvider();
        ButterKnife.bind(this);
        sessions = new ArrayList<>();

        adapter = new BoxingSessionAdapter(sessions);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        sessionsRecyclerView.setLayoutManager(mLayoutManager);
        sessionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sessionsRecyclerView.setAdapter(adapter);
        sessionsRecyclerView.setVisibility(View.VISIBLE);

        dummySamples();
    }

    private void dummySamples() {
        sessions = new ArrayList<>();
        double avgAcceleration = 5;
        double avgForce = 10;

        for (int i = 1; i <= 10; i++) {
            Calendar startCal = Calendar.getInstance();
            startCal.set(2018, Calendar.MAY, i, 10, 30, 0);
            Calendar endCal = Calendar.getInstance();
            endCal.set(2018, Calendar.MAY, i, 10, 31, 0);
            BoxingSession session = new BoxingSession(startCal.getTime(), endCal.getTime(), avgAcceleration, avgForce);
            sessions.add(session);
        }

        Log.d(TAG, sessions.toString());
        adapter.updateData(sessions);
        adapter.notifyDataSetChanged();
    }
}
