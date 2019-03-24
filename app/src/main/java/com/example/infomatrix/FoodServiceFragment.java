package com.example.infomatrix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.infomatrix.models.Food;
import com.example.infomatrix.models.FoodService;
import com.example.infomatrix.models.Service;
import com.example.infomatrix.models.User;
import com.example.infomatrix.models.UserCode;

import java.util.Arrays;
import java.util.List;

public class FoodServiceFragment extends BaseServiceFragment implements Button.OnClickListener {

    private UserCode userCode;
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
            userCode = bundle.getParcelable("user_code");
            food = bundle.getParcelable("food");
        }

        fullName = view.findViewById(R.id.full_name);
        action = view.findViewById(R.id.action);
        submitButton = view.findViewById(R.id.submit_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        image = view.findViewById(R.id.image);

        fullName.setText(String.format("%s %s", userCode.getUser().getFirstName(), userCode.getUser().getLastName()));
        action.setText(String.format("For %s", food.getTitle()));

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        if (canMakeService()) {
            image.setVisibility(ImageView.VISIBLE);
            submitButton.setVisibility(Button.VISIBLE);
        }
    }

    private boolean canMakeService() {
        List<? extends Service> services = userCode.getServices().get("foods");
        for (Service service : services) {
            FoodService foodService = (FoodService) service;
//            if (food.getId() == foodService.getFood() && foodService.isActive()) {
//                if (foodService.getAmount() < foodService.getOrders().size()) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
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
}
