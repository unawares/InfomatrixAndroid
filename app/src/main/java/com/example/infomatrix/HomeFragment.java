package com.example.infomatrix;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.infomatrix.adapters.FoodsAdapter;
import com.example.infomatrix.network.NetworkService;
import com.example.infomatrix.models.Food;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final int FOOD_SERVICE_REQUEST_CODE = 239;
    private static final int TRANSPORTATION_REQUEST_CODE = 330;

    private RecyclerView foodsRecyclerView;
    private ImageView transportationToCampQrCodeButton;
    private ImageView transportationFromCampQrCodeButton;

    public void init() {
        List<Food> foods = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Food food = new Food();
            food.setCreated(new Date());
            food.setDate(new Date());
            food.setDescription("Some Description " + i);
            food.setId(i);
            food.setTitle("Title " + i);
            food.setUpdated(new Date());
            foods.add(food);
        }
        FoodsAdapter foodsAdapter = new FoodsAdapter(getContext(), foods);
        foodsAdapter.setOnFoodItemClickListener(onFoodItemClickListener);
        foodsRecyclerView.setAdapter(foodsAdapter);
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

        init();
    }

    FoodsAdapter.OnFoodItemClickListener onFoodItemClickListener = new FoodsAdapter.OnFoodItemClickListener() {

        @Override
        public void onFoodItemClickListener(Food food) {
            BarcodeReaderActivity.setFragmentController(new BarcodeReaderActivity.FragmentController<BarcodeReaderActivity.BaseFragment>() {
                @Override
                public BarcodeReaderActivity.BaseFragment loadFragment() {
                    return new FoodFragment();
                }
            });
            Intent intent = new Intent(BarcodeReaderActivity.getLaunchIntent(getContext(),true, false));
            startActivityForResult(intent, FOOD_SERVICE_REQUEST_CODE);
        }

    };

    View.OnClickListener onTransportationClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(BarcodeReaderActivity.getLaunchIntent(getContext(),true, false));
            startActivityForResult(intent, TRANSPORTATION_REQUEST_CODE);
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static class FoodFragment extends BarcodeReaderActivity.BaseFragment {

        public static class FoodServiceBoxFragment extends Fragment {

            @Nullable
            @Override
            public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                return inflater.inflate(R.layout.fragment_service_box, container, false);
            }
        }

        private FrameLayout fragmentServiceBox;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_food_service, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }

        @Override
        protected void onScanned(Barcode barcode) {
            pause();
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.service_box, new FoodServiceBoxFragment());
            fragmentTransaction.commit();
        }

    }
}
