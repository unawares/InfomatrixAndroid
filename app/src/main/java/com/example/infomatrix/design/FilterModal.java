package com.example.infomatrix.design;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.infomatrix.R;
import com.example.infomatrix.adapters.FiltersAdapter;
import com.example.infomatrix.models.FilterItem;

import java.util.ArrayList;
import java.util.List;

public class FilterModal extends ConstraintLayout {

    private OnSaveFilters onSaveFilters;

    private List<FilterItem> filterItems;

    private CardView modalBox;
    private View overlay;

    private ListView filtersListView;
    private Button showButton;
    private Button saveButton;
    private ImageView closeButton;

    private boolean check;

    private void syncShowButton() {
        check = true;
        for (FilterItem filterItem : filterItems) {
            check = check && !filterItem.isChecked();
        }
        showButton.setText(check ? "Show All" : "Hide All");
    }

    public FilterModal(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.filter_modal_layout, this);
        modalBox = findViewById(R.id.modal_box);
        overlay = findViewById(R.id.overlay);
        filtersListView = findViewById(R.id.filters_list_view);
        showButton = findViewById(R.id.show_button);
        saveButton = findViewById(R.id.save_button);
        closeButton = findViewById(R.id.close_button);
        modalBox.setClickable(true);
        modalBox.setVisibility(CardView.INVISIBLE);
        check = true;

        overlay.setOnTouchListener(new OnTouchListener() {

            private boolean check = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        check = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (check) {
                            hide();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        check = false;
                        break;
                }
                return true;
            }

        });

        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                hide();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                for (FilterItem filterItem : FilterModal.this.filterItems) {
                    editor.putBoolean(filterItem.getKey(), filterItem.isChecked());
                }
                editor.apply();
                if (onSaveFilters != null) {
                    onSaveFilters.onSavedFilters();
                }
            }

        });

        showButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                for (FilterItem filterItem : filterItems) {
                    filterItem.setChecked(check);
                }
                ((FiltersAdapter) filtersListView.getAdapter()).notifyDataSetChanged();
                check = !check;
            }

        });

        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        setFilterItems();
    }

    public void show() {
        overlay.animate().withStartAction(new Runnable() {
            @Override
            public void run() {
                setFilterItems();
                overlay.setVisibility(View.VISIBLE);
            }
        }).setInterpolator(new DecelerateInterpolator()).alpha(0.3f);
        modalBox.animate().withStartAction(new Runnable() {
            @Override
            public void run() {
                modalBox.setVisibility(CardView.VISIBLE);
            }
        }).withEndAction(new Runnable() {
            @Override
            public void run() {
                onSaveFilters.onShown();
            }
        }).setInterpolator(new DecelerateInterpolator()).translationY(-modalBox.getHeight());
    }

    public void hide() {
        overlay.animate().withEndAction(new Runnable() {
            @Override
            public void run() {
                overlay.setVisibility(View.GONE);
            }
        }).setInterpolator(new DecelerateInterpolator()).alpha(0);
        modalBox.animate().withEndAction(new Runnable() {
            @Override
            public void run() {
                modalBox.setVisibility(CardView.GONE);
                onSaveFilters.onHidden();
            }
        }).setInterpolator(new DecelerateInterpolator()).translationY(0);
    }

    public void setFilterItems() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        this.filterItems = FilterItem.build();
        for (FilterItem filterItem : this.filterItems) {
            filterItem.setChecked(sharedPreferences.getBoolean(filterItem.getKey(), true));
        }
        filtersListView.setAdapter(new FiltersAdapter(getContext(), R.layout.modal_filter_item, filterItems, new FiltersAdapter.OnCheckedChangeListener() {
            @Override
            public void onCheckedChangeListener(FilterItem filterItem, boolean isChecked) {
                filterItem.setChecked(isChecked);
                syncShowButton();
            }
        }));
        syncShowButton();
    }

    public OnSaveFilters getOnSaveFilters() {
        return onSaveFilters;
    }

    public void setOnSaveFilters(OnSaveFilters onSaveFilters) {
        this.onSaveFilters = onSaveFilters;
    }

    public interface OnSaveFilters {

        void onShown();

        void onHidden();

        void onSavedFilters();

    }

}
