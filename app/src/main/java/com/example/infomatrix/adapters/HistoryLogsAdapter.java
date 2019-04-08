package com.example.infomatrix.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.infomatrix.R;
import com.example.infomatrix.models.HistoryLogRealmObject;
import com.example.infomatrix.models.ServiceLog;
import com.example.infomatrix.models.User;
import com.example.infomatrix.models.UserRealmObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryLogsAdapter extends RecyclerView.Adapter<HistoryLogsAdapter.BaseViewHolder> {

    private static final int OTHER_ITEMS_COUNT = 2;
    private static final int HEADER = 0;
    private static final int SEARCH = 1;
    private static final int ITEM = 2;

    private Context context;
    private List<HistoryLogRealmObject> historyLogRealmObjects;
    private List<HistoryLogRealmObject> filteredHistoryLogRealmObjects;

    private void filter(String search) {
        filteredHistoryLogRealmObjects.clear();
        for (HistoryLogRealmObject historyLogRealmObject : historyLogRealmObjects) {
            if (historyLogRealmObject.getFullName().toLowerCase().contains(search.toLowerCase())) {
                filteredHistoryLogRealmObjects.add(historyLogRealmObject);
            }
        }
        notifyDataSetChanged();
    }

    public HistoryLogsAdapter(Context context, List<HistoryLogRealmObject> historyLogRealmObjects) {
        this.context = context;
        this.filteredHistoryLogRealmObjects = new ArrayList<>();
        this.historyLogRealmObjects = historyLogRealmObjects;
        filter("");
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup container, int type) {
        View view = null;
        switch (type) {
            case HEADER:
                view = LayoutInflater.from(context).inflate(R.layout.header_item, container, false);
                return new HeaderViewHolder(view);
            case SEARCH:
                view = LayoutInflater.from(context).inflate(R.layout.search_item, container, false);
                return new SearchViewHolder(view);
            case ITEM:
            default:
                view = LayoutInflater.from(context).inflate(R.layout.history_log_item, container, false);
                return new HistoryLogViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int position) {
        if (baseViewHolder instanceof HistoryLogViewHolder) {
            ((HistoryLogViewHolder) baseViewHolder).bind(filteredHistoryLogRealmObjects.get(position - OTHER_ITEMS_COUNT));
            ((HistoryLogViewHolder) baseViewHolder).index.setText(Integer.toString(position - OTHER_ITEMS_COUNT + 1));
        } else if (baseViewHolder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) baseViewHolder).headerTextView.setText("Logs");
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return HEADER;
            case 1:
                return SEARCH;
            default:
                return ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return filteredHistoryLogRealmObjects.size() + OTHER_ITEMS_COUNT;
    }


    abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        private BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

    class HeaderViewHolder extends BaseViewHolder {

        private TextView headerTextView;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTextView = itemView.findViewById(R.id.header_text_view);
        }
    }

    class SearchViewHolder extends BaseViewHolder {

        private EditText editText;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.search_edit_text);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    filter(s.toString().trim());
                }

            });
        }

    }

    class HistoryLogViewHolder extends BaseViewHolder {

        private TextView index;
        private TextView fullName;
        private TextView action;
        private TextView date;

        public HistoryLogViewHolder(@NonNull View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.index);
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
