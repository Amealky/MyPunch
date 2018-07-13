package com.esgi.mypunch.Synthesis;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esgi.mypunch.R;
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


public class SynthesisFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_synthesis, container, false);
        BarChart chart = (BarChart) view.findViewById(R.id.chart);


        int[] dataObjects = {56, 255, 0, 23, 135, 68, 30};
        int i = 0;

        List<BarEntry> entries = new ArrayList<BarEntry>();

        for (int data : dataObjects) {
            // turn your data into Entry objects
            entries.add(new BarEntry(i, data));
            i++;
        }
        BarDataSet dataSet = new BarDataSet(entries, "Force moyenne total / jours"); // add entries to dataset


        dataSet.setColor(R.color.colorPrimaryLight);
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
        int color = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        dataSet.setColor(color);
        chart.getDescription().setText("Synthèse sur une semaine");
        chart.invalidate();


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
