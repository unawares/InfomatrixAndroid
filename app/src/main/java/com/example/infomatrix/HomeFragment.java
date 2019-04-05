package com.example.infomatrix;

import android.content.Intent;
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
import android.widget.ImageView;

import com.example.infomatrix.adapters.FoodsAdapter;
import com.example.infomatrix.network.NetworkService;
import com.example.infomatrix.models.Food;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView foodsRecyclerView;
    private ImageView transportationToCampQrCodeButton;
    private ImageView transportationFromCampQrCodeButton;

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
                        FoodsAdapter foodsAdapter = new FoodsAdapter(getContext(), foods);
                        foodsAdapter.setOnFoodItemClickListener(onFoodItemClickListener);
                        foodsRecyclerView.setAdapter(foodsAdapter);

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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transportationToCampQrCodeButton = view.findViewById(R.id.transportation_to_qr_code_button);
        transportationFromCampQrCodeButton = view.findViewById(R.id.transportation_from_qr_code_button);

        foodsRecyclerView = view.findViewById(R.id.foods_recycler_view);
        foodsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        foodsRecyclerView.setHasFixedSize(true);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(foodsRecyclerView);

        transportationToCampQrCodeButton.setOnClickListener(onTransportationClickListener);
        transportationFromCampQrCodeButton.setOnClickListener(onTransportationClickListener);
    }

    FoodsAdapter.OnFoodItemClickListener onFoodItemClickListener = new FoodsAdapter.OnFoodItemClickListener() {

        @Override
        public void onFoodItemClickListener(Food food) {
//            Intent intent = new Intent(getContext(), ScannedBarcodeActivity.class);
//            intent.putExtra("service", "FOOD");
//            intent.putExtra("food", food);
//            startActivity(intent);
        }

    };

    View.OnClickListener onTransportationClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(getContext(), ScannedBarcodeActivity.class);
//            switch (v.getId()) {
//                case R.id.transportation_to_qr_code_button:
//                    intent.putExtra("service", "TO_CAMP");
//                    break;
//                case R.id.transportation_from_qr_code_button:
//                    intent.putExtra("service", "FROM_CAMP");
//                    break;
//            }
//            startActivity(intent);
        }

    };

}
