package com.esgi.mypunch.punchlist;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esgi.mypunch.R;
import com.esgi.mypunch.data.dtos.BoxingSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoxingSessionAdapter extends RecyclerView.Adapter<BoxingSessionAdapter.MyViewHolder> {

    private List<BoxingSession> boxingSessions;
    private static final String TAG = "BoxingSessionAdapter";
    private Listener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sessionDate)     TextView sessionDate;
        @BindView(R.id.sessionDuration) TextView sessionDuration;
        @BindView(R.id.sessionOf)       TextView sessionOf;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Log.d(TAG, "view holder called");
        }
    }

    public BoxingSessionAdapter(List<BoxingSession> boxingSessions) {
        Log.d(TAG, "BoxingSessionAdapter");
        this.boxingSessions = boxingSessions;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void updateData(List<BoxingSession> boxingSessions) {
        this.boxingSessions = boxingSessions;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.boxingsession_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        final BoxingSession session = boxingSessions.get(position);
        Date dateStart = new Date();
        dateStart.setTime(session.getStart().getTime() - 3600000);
        // format session date
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        String stringDate = format.format(dateStart);
        holder.sessionDate.setText(stringDate);

        // session duration
        long startInMillis = session.getStart().getTime();
        long endInMillis = session.getEnd().getTime();
        long durationInMillis = endInMillis - startInMillis;
        long durationInSeconds = durationInMillis / 1000;
        String durationText = durationInSeconds + " s";
        holder.sessionDuration.setText(durationText);
        // on item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.onBoxingSessionClick(session);
            }
        });

        if(position%2 != 0){
            holder.itemView.setBackgroundResource(R.color.colorPrimary);
            holder.sessionDate.setTextColor(Color.WHITE);
            holder.sessionDuration.setTextColor(Color.WHITE);
            holder.sessionOf.setTextColor(Color.WHITE);
        }else{
            holder.itemView.setBackgroundResource(R.color.white);
            holder.sessionDate.setTextColor(Color.BLACK);
            holder.sessionDuration.setTextColor(Color.BLACK);
            holder.sessionOf.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return boxingSessions.size();
    }

    public interface Listener {
        void onBoxingSessionClick(BoxingSession bSession);
    }
}
