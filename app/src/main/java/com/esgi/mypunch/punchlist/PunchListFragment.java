package com.esgi.mypunch.punchlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esgi.mypunch.NewSession.NewSessionActivity;
import com.esgi.mypunch.PunchDetailsActivity;
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

public class PunchListFragment extends Fragment implements BoxingSessionAdapter.Listener {

    public static final String TAG = "PunchListActivity";
    public static final String SESSION_KEY = "boxingSession";
    private PunchMyNodeProvider provider;
    private List<BoxingSession> sessions;
    private BoxingSessionAdapter adapter;
    private Context context;

    @BindView(R.id.boxingSessionList) RecyclerView sessionsRecyclerView;
    @BindView(R.id.fab_addSession)
    FloatingActionButton fab_addSession;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_punch_list, container, false);
        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        sessionsRecyclerView.setLayoutManager(mLayoutManager);
        sessionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sessionsRecyclerView.setAdapter(adapter);
        this.adapter.setListener(this);
        sessionsRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));

        fab_addSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewSessionActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        provider = new PunchMyNodeProvider();
        sessions = new ArrayList<>();
        context = getActivity().getApplicationContext();
        adapter = new BoxingSessionAdapter(sessions);

        User user = SharedPreferencesManager.getUser(getActivity());
        Log.i(TAG, user.toString());
        Call<List<BoxingSession>> response = provider.getSessionsForUser(user);
        response.enqueue(new Callback<List<BoxingSession>>() {
            @Override
            public void onResponse(Call<List<BoxingSession>> call, Response<List<BoxingSession>> response) {
                if (response.code() == 200) {
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



    @Override
    public void onBoxingSessionClick(BoxingSession bSession) {
        Log.d(TAG, "Clicked on session : " + bSession.toString());
        Intent intent = new Intent(this.getActivity(), PunchDetailsActivity.class);
        intent.putExtra(SESSION_KEY, bSession);
        startActivity(intent);
    }
}
