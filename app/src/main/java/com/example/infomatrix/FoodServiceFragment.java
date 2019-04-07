package com.example.infomatrix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infomatrix.backend.UsersBackend;
import com.example.infomatrix.models.Food;
import com.example.infomatrix.models.MessagedResponse;
import com.example.infomatrix.models.ServiceLog;
import com.example.infomatrix.models.User;
import com.example.infomatrix.network.NetworkService;
import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderActivity;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodServiceFragment extends BarcodeReaderActivity.BaseFragment {

    private Food food;

    public static class FoodServiceBoxFragment extends Fragment {

        private ActionsListener actionsListener;
        private User user;
        private ServiceLog serviceLog;
        private TextView fullNameTextView;
        private TextView actionTextView;
        private Button submitButton;
        private Button cancelButton;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_service_box, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            fullNameTextView = view.findViewById(R.id.full_name);
            actionTextView = view.findViewById(R.id.action);
            submitButton = view.findViewById(R.id.submit_button);
            cancelButton = view.findViewById(R.id.cancel_button);

            Bundle bundle = getArguments();

            if (bundle != null) {
                serviceLog = bundle.getParcelable("logs_body");
                user = bundle.getParcelable("user_body");
                fullNameTextView.setText(user.getFullName());
                actionTextView.setText(serviceLog.getAction().toDisplayString());
            }

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitButton.setOnClickListener(null);
                    final View.OnClickListener self = this;
                    NetworkService
                            .getInstance()
                            .getLogsApi()
                            .addLog(serviceLog)
                            .enqueue(new Callback<MessagedResponse>() {

                                @Override
                                public void onResponse(Call<MessagedResponse> call, Response<MessagedResponse> response) {
                                    if (response.isSuccessful()) {
                                        MessagedResponse messagedResponse = response.body();
                                        if (messagedResponse.isSuccess()) {
                                            Toast.makeText(getContext(), messagedResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                            if (actionsListener != null) {
                                                actionsListener.onSubmit();
                                            }
                                        } else {
                                            Toast.makeText(getContext(), messagedResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                                    }
                                    submitButton.setOnClickListener(self);
                                }

                                @Override
                                public void onFailure(Call<MessagedResponse> call, Throwable t) {
                                    t.printStackTrace();
                                    submitButton.setOnClickListener(self);
                                }

                            });
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (actionsListener != null) {
                        actionsListener.onCancel();
                    }
                }
            });

        }

        public ActionsListener getActionsListener() {
            return actionsListener;
        }

        public void setActionsListener(ActionsListener actionsListener) {
            this.actionsListener = actionsListener;
        }

        public interface ActionsListener {

            void onSubmit();

            void onCancel();

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        food = bundle.getParcelable("food");
    }

    @Override
    protected void onScanned(Barcode barcode) {
        pause();
        User user = UsersBackend.getInstance().getUser(barcode.displayValue);
        if (user != null) {
            ServiceLog serviceLog = new ServiceLog();
            serviceLog.setUuid(UUID.randomUUID().toString().replace("-", ""));
            serviceLog.setCode(user.getCode());
            System.out.println(food);
            switch (food.getFoodType()) {
                case BREAKFAST:
                    serviceLog.setComment("Breakfast done by " + user.getFullName());
                    serviceLog.setAction(ServiceLog.Action.BREAKFAST);
                    break;
                case LUNCH:
                    serviceLog.setComment("Lunch done by " + user.getFullName());
                    serviceLog.setAction(ServiceLog.Action.LUNCH);
                    break;
                case DINNER:
                    serviceLog.setComment("Dinner done by " + user.getFullName());
                    serviceLog.setAction(ServiceLog.Action.DINNER);
                    break;
            }
            final FoodServiceFragment.FoodServiceBoxFragment foodServiceBoxFragment = new FoodServiceFragment.FoodServiceBoxFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("user_body", user);
            bundle.putParcelable("logs_body", serviceLog);
            foodServiceBoxFragment.setArguments(bundle);

            foodServiceBoxFragment.setActionsListener(new FoodServiceBoxFragment.ActionsListener() {

                @Override
                public void onSubmit() {
                    FragmentManager fragmentManager = getChildFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(foodServiceBoxFragment);
                    fragmentTransaction.runOnCommit(new Runnable() {
                        @Override
                        public void run() {
                            resume();
                        }
                    });
                    fragmentTransaction.commit();
                }

                @Override
                public void onCancel() {
                    FragmentManager fragmentManager = getChildFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(foodServiceBoxFragment);
                    fragmentTransaction.runOnCommit(new Runnable() {
                        @Override
                        public void run() {
                            resume();
                        }
                    });
                    fragmentTransaction.commit();
                }

            });

            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.service_box, foodServiceBoxFragment);
            fragmentTransaction.commit();
        } else  {
            Toast.makeText(getContext(), "Invalid QR code", Toast.LENGTH_SHORT).show();
            resume();
        }

    }

}
