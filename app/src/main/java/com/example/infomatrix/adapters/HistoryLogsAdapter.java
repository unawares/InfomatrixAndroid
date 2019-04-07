package com.example.infomatrix.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.infomatrix.R;
import com.example.infomatrix.models.HistoryLogRealmObject;
import com.example.infomatrix.models.ServiceLog;

import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryLogsAdapter extends RecyclerView.Adapter<HistoryLogsAdapter.HistoryLogViewHolder> {

    private Context context;
    private List<HistoryLogRealmObject> historyLogRealmObjects;

    public HistoryLogsAdapter(Context context, List<HistoryLogRealmObject> historyLogRealmObjects) {
        this.context = context;
        this.historyLogRealmObjects = historyLogRealmObjects;
    }

    @NonNull
    @Override
    public HistoryLogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_log_item, viewGroup, false);
        return new HistoryLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryLogViewHolder historyViewHolder, int position) {
        historyViewHolder.bind(historyLogRealmObjects.get(position));
    }

    @Override
    public int getItemCount() {
        return historyLogRealmObjects.size();
    }

    public static class HistoryLogViewHolder extends RecyclerView.ViewHolder {

        private TextView fullName;
        private TextView action;
        private TextView date;

        public HistoryLogViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.full_name);
            action = itemView.findViewById(R.id.action);
            date = itemView.findViewById(R.id.date);
        }

        public void bind(HistoryLogRealmObject historyLogRealmObject) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            fullName.setText(historyLogRealmObject.getFullName());
            if (historyLogRealmObject.getAction() != null) {
                action.setText(ServiceLog.Action.valueOf(historyLogRealmObject.getAction()).toDisplayString());
            }
            date.setText(simpleDateFormat.format(historyLogRealmObject.getDate()));
        }

    }
}
