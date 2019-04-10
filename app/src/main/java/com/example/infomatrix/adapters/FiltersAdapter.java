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

public class FiltersAdapter extends BaseAdapter {

    private OnCheckedChangeListener onCheckedChangeListener;

    private Context context;
    private List<FilterItem> filterItems;
    private int listItemLayoutId;

    public FiltersAdapter(Context context, int layoutId, List<FilterItem> filterItems, OnCheckedChangeListener onCheckedChangeListener) {
        this.context = context;
        this.onCheckedChangeListener = onCheckedChangeListener;
        this.filterItems = filterItems;
        this.listItemLayoutId = layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilterItem filterItem = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(listItemLayoutId, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bind(filterItem);
        return convertView;
    }

    @Override
    public int getCount() {
        return filterItems.size();
    }

    @Override
    public FilterItem getItem(int position) {
        return filterItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCheckedChangeListener.onCheckedChangeListener(filterItem, checkbox.isChecked());
                }
            });
        }

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public interface OnCheckedChangeListener {

        void onCheckedChangeListener(FilterItem filterItem, boolean isChecked);

    }

}
