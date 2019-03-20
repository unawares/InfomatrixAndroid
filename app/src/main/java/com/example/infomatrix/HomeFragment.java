package com.example.infomatrix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.infomatrix.adapters.FoodsAdapter;
import com.example.infomatrix.network.NetworkService;
import com.example.infomatrix.serializers.Food;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView foodsRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        NetworkService.getInstance()
                .getFoodApi()
                .getFoods()
                .enqueue(new Callback<List<Food>>() {

                    @Override
                    public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                        List<Food> foods = response.body();
                        foodsRecyclerView.setAdapter(new FoodsAdapter(getContext(), foods));
                    }

                    @Override
                    public void onFailure(Call<List<Food>> call, Throwable t) {
                        t.printStackTrace();
                    }

                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        foodsRecyclerView = view.findViewById(R.id.foods_recycler_view);
        foodsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        foodsRecyclerView.setHasFixedSize(true);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(foodsRecyclerView);

        return view;
    }
}
