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
import com.example.infomatrix.test.FoodsBuilder;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

public class HomeFragment extends Fragment {

    private RecyclerView foodsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        foodsRecyclerView = view.findViewById(R.id.foods_recycler_view);
        foodsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        foodsRecyclerView.setHasFixedSize(true);
        foodsRecyclerView.setAdapter(new FoodsAdapter(getContext(), FoodsBuilder.build()));
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(foodsRecyclerView);

        return view;
    }
}
