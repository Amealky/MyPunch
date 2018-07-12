package com.esgi.mypunch.Synthesis;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esgi.mypunch.R;
import com.github.mikephil.charting.charts.LineChart;
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
        LineChart chart = (LineChart) view.findViewById(R.id.chart);


        int[] dataObjects = {56};

        List<Entry> entries = new ArrayList<Entry>();

        for (int data : dataObjects) {

            // turn your data into Entry objects
            entries.add(new Entry(data, data));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset

        LineData lineData = new LineData(dataSet);

        chart.setData(lineData);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);

      //  chart.getXAxis().setDrawGridLines(false);
        chart.invalidate();


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
