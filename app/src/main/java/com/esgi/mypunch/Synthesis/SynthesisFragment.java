package com.esgi.mypunch.Synthesis;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esgi.mypunch.R;
import com.esgi.mypunch.data.SharedPreferencesManager;
import com.esgi.mypunch.data.dtos.BoxingSession;
import com.esgi.mypunch.data.dtos.User;
import com.esgi.mypunch.data.mainapi.PunchMyNodeProvider;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class SynthesisFragment extends Fragment {

    private PunchMyNodeProvider provider;
    private List<BoxingSession> sessions;
    private List<Integer> moyList = new ArrayList<Integer>();
    private final String TAG = "SynthesisFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_synthesis, container, false);
        provider = new PunchMyNodeProvider();
        sessions = new ArrayList<>();

        User user = SharedPreferencesManager.getUser(getActivity());


        Call<List<BoxingSession>> response = provider.getSessionsForUser(user);
        response.enqueue(new Callback<List<BoxingSession>>() {
            @Override
            public void onResponse(Call<List<BoxingSession>> call, Response<List<BoxingSession>> response) {
                if (response.code() == 200) {
                    sessions = response.body();

                    for(int i = 0; i < sessions.size(); i++){
                        moyList.add(sessions.get(i).getAverage_power());
                    }

                    refreshChart(view);

                } else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<List<BoxingSession>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });



        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void refreshChart(View view){
        final BarChart chart = (BarChart) view.findViewById(R.id.chart);


        List<BarEntry> entries = new ArrayList<BarEntry>();

        int i = 0;
        for (int data : moyList) {
            // turn your data into Entry objects
            entries.add(new BarEntry(i, data));
            i++;
        }
        BarDataSet dataSet = new BarDataSet(entries, "Force moyenne total / séance"); // add entries to dataset
        int color = ContextCompat.getColor(getContext(), R.color.colorPrimary);

        dataSet.setColor(color);
        BarData lineData = new BarData(dataSet);

        chart.setData(lineData);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getXAxis().setEnabled(true);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisRight().setDrawAxisLine(false);

        chart.setEnabled(true);

        chart.setTouchEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setScaleXEnabled(false);
        chart.setScaleYEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        dataSet.setColor(color);
        chart.getDescription().setText("Courbe d'évolution");
        chart.invalidate();
    }



}
