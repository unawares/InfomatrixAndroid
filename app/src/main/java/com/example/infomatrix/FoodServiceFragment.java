package com.example.infomatrix;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.infomatrix.models.Food;
import com.example.infomatrix.models.User;

public class FoodServiceFragment extends BaseServiceFragment {

    private User user;
    private Food food;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            System.out.println("CREATED");
            user = bundle.getParcelable("user");
            food = bundle.getParcelable("food");
        }
    }

    @Override
    public void serve() {
        System.out.println(user.getEmail());
        System.out.println(food.getDescription());
        cancel();
    }
}
