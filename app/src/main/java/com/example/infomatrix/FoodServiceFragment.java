package com.example.infomatrix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.infomatrix.models.Food;
import com.example.infomatrix.models.FoodService;
import com.example.infomatrix.models.Service;
import com.example.infomatrix.models.Services;
import com.example.infomatrix.models.User;
import com.example.infomatrix.models.UserCode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FoodServiceFragment extends BaseServiceFragment implements Button.OnClickListener {

    private CardView box;
    private Food food;
    private TextView fullName;
    private TextView action;
    private Button submitButton;
    private Button cancelButton;
    private ImageView image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            food = bundle.getParcelable("food");
        }

        box = view.findViewById(R.id.box);
        fullName = view.findViewById(R.id.full_name);
        action = view.findViewById(R.id.action);
        submitButton = view.findViewById(R.id.submit_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        image = view.findViewById(R.id.image);

        action.setText(String.format("For %s", food.getTitle()));

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    private boolean canMakeService(UserCode userCode) {
        Services services = userCode.getServices();
        for (FoodService foodService : services.getFoods()) {
            if (food.getId() == foodService.getFood() && foodService.isActive()) {
                if (foodService.getAmount() > foodService.getOrders().size()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
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
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        box.setVisibility(CardView.VISIBLE);
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
        if (canMakeService(userCode)) {
            image.setVisibility(ImageView.VISIBLE);
            submitButton.setVisibility(Button.VISIBLE);
        }
    }

    @Override
    public void close() {
        super.close();
    }
}
