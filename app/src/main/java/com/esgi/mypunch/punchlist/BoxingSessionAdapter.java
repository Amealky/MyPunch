package com.esgi.mypunch.punchlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esgi.mypunch.R;
import com.esgi.mypunch.data.dtos.BoxingSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoxingSessionAdapter extends RecyclerView.Adapter<BoxingSessionAdapter.MyViewHolder> {

    private List<BoxingSession> boxingSessions;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sessionDate)     TextView sessionDate;
        @BindView(R.id.sessionDuration) TextView sessionDuration;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }

    public BoxingSessionAdapter(List<BoxingSession> boxingSessions) {
        this.boxingSessions = boxingSessions;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.boxingsession_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BoxingSession session = boxingSessions.get(position);
        // format session date
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        String stringDate = format.format(session.getStart());
        holder.sessionDate.setText(stringDate);
        // session duration
        long startInMillis = session.getStart().getTime();
        long endInMillis = session.getEnd().getTime();
        long durationInMillis = endInMillis - startInMillis;
        long durationInSeconds = durationInMillis * 1000;
        String durationText = durationInSeconds + " seconds";
        holder.sessionDuration.setText(durationText);
    }

    @Override
    public int getItemCount() {
        return boxingSessions.size();
    }
}
