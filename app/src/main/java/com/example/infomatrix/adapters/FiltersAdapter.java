package com.example.infomatrix.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.example.infomatrix.R;
import com.example.infomatrix.models.FilterItem;

import java.util.ArrayList;
import java.util.List;

public class FiltersAdapter extends ArrayAdapter<FilterItem> {

    private OnCheckedChangeListener onCheckedChangeListener;

    private Context context;
    private List<FilterItem> filterItems;
    private int listItemLayoutId;

    public FiltersAdapter(Context context, int layoutId, List<FilterItem> filterItems, OnCheckedChangeListener onCheckedChangeListener) {
        super(context, layoutId, filterItems);
        this.context = context;
        this.onCheckedChangeListener = onCheckedChangeListener;
        this.filterItems = filterItems;
        this.listItemLayoutId = layoutId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FilterItem filterItem = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(listItemLayoutId, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bind(filterItem);
        return convertView;
    }

    public class ViewHolder {

        private TextView label;
        private Switch checkbox;

        public ViewHolder(View view) {
            label = view.findViewById(R.id.label);
            checkbox = view.findViewById(R.id.checkbox);
        }

        public void bind(final FilterItem filterItem) {
            label.setText(filterItem.getLabel());
            checkbox.setChecked(filterItem.isChecked());
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    filterItem.setChecked(isChecked);
                    onCheckedChangeListener.onCheckedChangeListener(filterItem);
                }
            });
        }

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public interface OnCheckedChangeListener {

        void onCheckedChangeListener(FilterItem filterItem);

    }

}
