package com.esgi.mypunch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.esgi.mypunch.data.dtos.BoxingSession;
import com.esgi.mypunch.punchlist.PunchListFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PunchDetailsActivity extends BaseActivity {

    public static final String TAG = "PunchDetailsActivity";
    private BoxingSession bSession;
    private PieChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;

    Date Dduree;
    long duree;

    @BindView(R.id.dateValue)
    TextView dureeValue;
    @BindView(R.id.nbcoup_value)
    TextView nbcoup_value;
    @BindView(R.id.min_value)
    TextView min_value;
    @BindView(R.id.max_value)
    TextView max_value;
    @BindView(R.id.moyenne_chart)
    ColorfulRingProgressView moyenneChart;
    @BindView(R.id.tvPercent)
    TextView average_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch_details);
        ButterKnife.bind(this);


        Serializable content = getIntent().getSerializableExtra(PunchListFragment.SESSION_KEY);
        if (content != null) {
            bSession = (BoxingSession) content;

            nbcoup_value.setText(String.valueOf(bSession.getNbPunches()));
            min_value.setText(String.valueOf(bSession.getMin_power()));
            max_value.setText(String.valueOf(bSession.getMax_power()));
            float moyenne = bSession.getAverage_power();
            float percentage = (moyenne/252)*100;
            moyenneChart.setPercent(percentage);
            average_value.setText(String.valueOf(bSession.getAverage_power()));

            duree = bSession.getEnd().getTime() - bSession.getStart().getTime();
            duree -= 3600000;
            DateFormat hourFormat = SimpleDateFormat.getTimeInstance();

            Dduree = new Date();
            Dduree.setTime(duree);

            dureeValue.setText(hourFormat.format(Dduree));

            Log.d(TAG, bSession.toString());
            renderBoxingSession(bSession);
        } else {
            Log.e(TAG, "couldn't get session");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void renderBoxingSession(BoxingSession bSession) {
        String nbPunches = bSession.getNbPunches() + "";
        String minPower = bSession.getMin_power() + "";
        String avgPower = bSession.getAverage_power() + "";
        String maxPower = bSession.getMax_power() + "";

        Date startDate = bSession.getStart();
        Date endDate = bSession.getEnd();
        DateFormat dayFormat = SimpleDateFormat.getDateInstance();
        DateFormat hourFormat = SimpleDateFormat.getTimeInstance();





        String title = dayFormat.format(startDate.getTime()- 3600000) + " " + hourFormat.format(startDate.getTime() - 3600000 * 2);
        setTitle(title);
    }
}
