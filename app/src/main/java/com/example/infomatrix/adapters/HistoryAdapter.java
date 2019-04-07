package com.example.infomatrix.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.infomatrix.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    public HistoryAdapter() {
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item, viewGroup, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
