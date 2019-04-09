package com.example.infomatrix.design;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;

import com.example.infomatrix.R;

public class SynchronizeUsersButtonView extends ConstraintLayout {

    public static final int SHIFT_WIDTH = 300;

    private View view;
    private ProgressBar progress;
    private boolean isLoading;
    private CardView foreground;

    public SynchronizeUsersButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.synchronize_users_button, this);
        view = findViewById(R.id.view);
        foreground = findViewById(R.id.foreground);
        progress = findViewById(R.id.progress);
        progress.setIndeterminate(true);
        foreground.setCardBackgroundColor(getResources().getColor(R.color.synchronizeUserBackgroundColor));
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        foreground.setCardBackgroundColor(getResources().getColor(R.color.synchronizeUserBackgroundColorPressed));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        foreground.setCardBackgroundColor(getResources().getColor(R.color.synchronizeUserBackgroundColor));
                        performClick();
                        break;
                }
                return true;
            }
        });
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        if (loading) {
            foreground.animate().setInterpolator(new DecelerateInterpolator()).translationX(SHIFT_WIDTH);
            progress.animate().withStartAction(new Runnable() {
                @Override
                public void run() {
                    progress.setVisibility(ProgressBar.VISIBLE);
                }
            }).setInterpolator(new AccelerateInterpolator())
                    .translationX(SHIFT_WIDTH / 2f + progress.getWidth());
        } else {
            foreground.animate().setInterpolator(new DecelerateInterpolator()).translationX(0);
            progress.animate().withEndAction(new Runnable() {
                @Override
                public void run() {
                    progress.setVisibility(ProgressBar.INVISIBLE);
                }
            }).setInterpolator(new AccelerateInterpolator())
                    .translationX(0);
        }
        isLoading = loading;
    }

}
