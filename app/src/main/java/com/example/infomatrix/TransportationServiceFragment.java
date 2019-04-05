package com.example.infomatrix;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.example.infomatrix.models.Food;
import com.example.infomatrix.models.User;
import com.example.infomatrix.models.UserCode;

public class TransportationServiceFragment extends BaseServiceFragment implements Button.OnClickListener {

    private CardView box;
    private CardView counter;
    private String transportation;
    private TextView fullName;
    private TextView action;
    private Button submitButton;
    private Button cancelButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transportation_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            transportation = bundle.getString("transportation");
        }

        box = view.findViewById(R.id.box);
        fullName = view.findViewById(R.id.full_name);
        action = view.findViewById(R.id.action);
        submitButton = view.findViewById(R.id.submit_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        counter = view.findViewById(R.id.counter);

        if (transportation.equals("to")) {
            action.setText("Transportation to the camp!");
        } else {
            action.setText("Transportation from the camp!");
        }

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        counter.animate()
                .alpha(1)
                .scaleX(1)
                .scaleY(1)
                .setStartDelay(100)
                .setDuration(50)
                .setInterpolator(new DecelerateInterpolator())
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        counter.setVisibility(CardView.VISIBLE);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                commit();
                break;
            case R.id.cancel_button:
                cancel();
                break;
        }
    }

    @Override
    public void show() {
        box.animate()
                .alpha(1)
                .scaleX(1)
                .scaleY(1)
                .setDuration(50)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        box.setVisibility(CardView.VISIBLE);
                    }
                });
        counter.animate()
                .alpha(0)
                .scaleX(0)
                .scaleY(0)
                .setDuration(50)
                .setInterpolator(new DecelerateInterpolator())
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        counter.setVisibility(CardView.GONE);
                    }
                });
    }

    @Override
    public void hide() {
        box.animate()
                .alpha(0)
                .scaleX(0)
                .scaleY(0)
                .setDuration(50)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        box.setVisibility(CardView.GONE);
                    }
                });
    }

    @Override
    public void serve() throws BaseServiceFragment.UserCodeIsNotSetException {
        super.serve();
        UserCode userCode = getUserCode();
        fullName.setText(userCode.getUser().getFullName());
    }

    @Override
    public void close() {
        super.close();
    }
}
