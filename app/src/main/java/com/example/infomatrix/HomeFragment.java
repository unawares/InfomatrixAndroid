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
import android.widget.Toast;

import com.example.infomatrix.adapters.FoodsAdapter;
import com.example.infomatrix.database.DBManager;
import com.example.infomatrix.design.QrCodeButtonView;
import com.example.infomatrix.design.SynchronizeUsersButtonView;
import com.example.infomatrix.models.Food;
import com.example.infomatrix.models.User;
import com.example.infomatrix.models.UserRealmObject;
import com.example.infomatrix.models.Users;
import com.example.infomatrix.network.NetworkService;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.notbytes.barcode_reader.BarcodeReaderActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final int FOOD_SERVICE_REQUEST_CODE = 239;
    private static final int TRANSPORTATION_REQUEST_CODE = 330;

    private SynchronizeUsersButtonView synchronizeUsersButtonView;
    private RecyclerView foodsRecyclerView;
    private QrCodeButtonView transportationToCampQrCodeButton;
    private QrCodeButtonView transportationFromCampQrCodeButton;

    private void initFoods() {
        List<Food> foods = new ArrayList<>();

        Food breakfast = new Food();
        Food lunch = new Food();
        Food dinner = new Food();

        breakfast.setFoodType(Food.FoodType.BREAKFAST);
        breakfast.setTitle("Breakfast");
        breakfast.setDescription("Infomatrix Food Tracker");
        breakfast.setDrawableId(R.drawable.breakfast);
        foods.add(breakfast);

        lunch.setFoodType(Food.FoodType.LUNCH);
        lunch.setTitle("Lunch");
        lunch.setDescription("Infomatrix Food Tracker");
        lunch.setDrawableId(R.drawable.lunch);
        foods.add(lunch);

        dinner.setFoodType(Food.FoodType.DINNER);
        dinner.setTitle("Dinner");
        dinner.setDescription("Infomatrix Food Tracker");
        dinner.setDrawableId(R.drawable.dinner);
        foods.add(dinner);

        FoodsAdapter foodsAdapter = new FoodsAdapter(getContext(), foods);
        foodsAdapter.setOnFoodItemClickListener(onFoodItemClickListener);
        foodsRecyclerView.setAdapter(foodsAdapter);
    }

    public void init() {
        initFoods();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        synchronizeUsersButtonView = view.findViewById(R.id.synchronize_users_button_view);

        transportationToCampQrCodeButton = view.findViewById(R.id.transportation_to_qr_code_button);
        transportationFromCampQrCodeButton = view.findViewById(R.id.transportation_from_qr_code_button);

        foodsRecyclerView = view.findViewById(R.id.foods_recycler_view);
        foodsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        foodsRecyclerView.setHasFixedSize(true);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(foodsRecyclerView);

        transportationToCampQrCodeButton.setOnClickListener(onTransportationClickListener);
        transportationFromCampQrCodeButton.setOnClickListener(onTransportationClickListener);

        synchronizeUsersButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View.OnClickListener self = this;
                synchronizeUsersButtonView.setLoading(true);
                synchronizeUsersButtonView.setOnClickListener(null);
                NetworkService
                        .getInstance()
                        .getUsersApi()
                        .getUsers()
                        .enqueue(new Callback<Users>() {

                            @Override
                            public void onResponse(Call<Users> call, Response<Users> response) {
                                if (getContext() != null) {
                                    if (response.isSuccessful()) {
                                        Users users = response.body();
                                        if (users.isSuccess()) {
                                            List<UserRealmObject> userRealmObjects = new ArrayList<>();
                                            for (User user : users.getUsers()) {
                                                UserRealmObject userRealmObject = new UserRealmObject();
                                                userRealmObject.setCode(user.getCode());
                                                userRealmObject.setFullName(user.getFullName());
                                                userRealmObject.setRole(user.getRole().getIdentifier());
                                                userRealmObject.setFood(user.isFood());
                                                userRealmObject.setTransport(user.isFood());
                                                userRealmObjects.add(userRealmObject);
                                            }
                                            DBManager
                                                    .getInstance()
                                                    .updateUsers(userRealmObjects);
                                            Toast.makeText(getContext(), "Users has been synchronized", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Internal Error", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                synchronizeUsersButtonView.setLoading(false);
                                synchronizeUsersButtonView.setOnClickListener(self);
                            }

                            @Override
                            public void onFailure(Call<Users> call, Throwable t) {
                                t.printStackTrace();
                                synchronizeUsersButtonView.setLoading(false);
                                synchronizeUsersButtonView.setOnClickListener(self);
                            }

                        });
            }
        });

        init();
    }

    FoodsAdapter.OnFoodItemClickListener onFoodItemClickListener = new FoodsAdapter.OnFoodItemClickListener() {

        @Override
        public void onFoodItemClickListener(final Food food) {
            BarcodeReaderActivity.setFragmentController(new BarcodeReaderActivity.FragmentController<BarcodeReaderActivity.BaseFragment>() {
                @Override
                public BarcodeReaderActivity.BaseFragment loadFragment() {
                    FoodServiceFragment foodServiceFragment = new FoodServiceFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("food", food);
                    foodServiceFragment.setArguments(bundle);
                    return foodServiceFragment;
                }
            });
            Intent intent = new Intent(BarcodeReaderActivity.getLaunchIntent(getContext(),true, false));
            startActivityForResult(intent, FOOD_SERVICE_REQUEST_CODE);
        }

    };

    View.OnClickListener onTransportationClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            TransportServiceFragment.TransportationType transportationType = null;
            switch (v.getId()) {
                case R.id.transportation_to_qr_code_button:
                    transportationType = TransportServiceFragment.TransportationType.TO;
                    break;
                case R.id.transportation_from_qr_code_button:
                    transportationType = TransportServiceFragment.TransportationType.FROM;
                    break;
            }
            if (transportationType != null) {
                final TransportServiceFragment.TransportationType type = transportationType;
                BarcodeReaderActivity.setFragmentController(new BarcodeReaderActivity.FragmentController<BarcodeReaderActivity.BaseFragment>() {
                    @Override
                    public BarcodeReaderActivity.BaseFragment loadFragment() {
                        TransportServiceFragment transportServiceFragment = new TransportServiceFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("transportation_type", type.name());
                        transportServiceFragment.setArguments(bundle);
                        return transportServiceFragment;
                    }
                });
                Intent intent = new Intent(BarcodeReaderActivity.getLaunchIntent(getContext(),true, false));
                startActivityForResult(intent, TRANSPORTATION_REQUEST_CODE);
            }
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
