package com.example.infomatrix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.infomatrix.adapters.HistoryLogsAdapter;
import com.example.infomatrix.database.DBManager;

public class HistoryLogsFragment extends Fragment {

    private static HistoryLogsFragment instance;

    public static HistoryLogsFragment getInstance() {
        return instance;
    }

    private RecyclerView historyLogsRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_logs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        historyLogsRecyclerView = view.findViewById(R.id.history_logs_recycler_view);
        historyLogsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        historyLogsRecyclerView.setAdapter(new HistoryLogsAdapter(getContext(), DBManager.getInstance().getAllHistoryLogs()));
    }
}
