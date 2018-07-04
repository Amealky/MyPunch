package com.esgi.mypunch.punchlist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esgi.mypunch.R;
import com.esgi.mypunch.data.SharedPreferencesManager;
import com.esgi.mypunch.data.dtos.BoxingSession;
import com.esgi.mypunch.data.dtos.User;
import com.esgi.mypunch.data.mainapi.PunchMyNodeProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PunchListFragment extends Fragment {

    public static final String TAG = "PunchListActivity";
    private PunchMyNodeProvider provider;
    private List<BoxingSession> sessions;
    private BoxingSessionAdapter adapter;
    private Context context;

    @BindView(R.id.boxingSessionList) RecyclerView sessionsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_punch_list, container, false);
        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        sessionsRecyclerView.setLayoutManager(mLayoutManager);
        sessionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sessionsRecyclerView.setAdapter(adapter);
        sessionsRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        provider = new PunchMyNodeProvider();
        sessions = new ArrayList<>();
        context = getActivity().getApplicationContext();
        adapter = new BoxingSessionAdapter(sessions);
        //dummySamples();

        User user = SharedPreferencesManager.getUser(getActivity());
        Log.i(TAG, user.toString());
        Call<List<BoxingSession>> response = provider.getSessionsForUser(user);
        response.enqueue(new Callback<List<BoxingSession>>() {
            @Override
            public void onResponse(Call<List<BoxingSession>> call, Response<List<BoxingSession>> response) {
                if (response.code() == 200) {
                    Log.i(TAG, response.body().toString());
                    sessions = response.body();
                    adapter.updateData(sessions);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<List<BoxingSession>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
/*
    private void dummySamples() {
        sessions = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Calendar startCal = Calendar.getInstance();
            startCal.set(2018, Calendar.MAY, i, 10, 30, 0);
            Calendar endCal = Calendar.getInstance();
            endCal.set(2018, Calendar.MAY, i, 10, 31, 0);
            BoxingSession session = new BoxingSession(startCal.getTime(), endCal.getTime(), 20, 15, 5, 20);
            sessions.add(session);
        }

        Log.d(TAG, sessions.toString());
        adapter.updateData(sessions);
        adapter.notifyDataSetChanged();
    }
    */
}
