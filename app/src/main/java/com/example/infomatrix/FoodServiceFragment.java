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

import com.example.infomatrix.database.DBManager;
import com.example.infomatrix.models.Food;
import com.example.infomatrix.models.MessagedResponse;
import com.example.infomatrix.models.ServiceLog;
import com.example.infomatrix.models.User;
import com.example.infomatrix.models.UserRealmObject;
import com.example.infomatrix.network.NetworkService;
import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderActivity;

import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodServiceFragment extends BarcodeReaderActivity.BaseFragment {

    private Food food;

    public static class AlreadyServedBoxFragment extends Fragment {

        private User user;
        private ServiceLog serviceLog;

        private AlreadyServedBoxFragment.ActionsListener actionsListener;

        private TextView fullNameTextView;
        private TextView actionTextView;
        private Button cancelButton;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_already_served_box, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            fullNameTextView = view.findViewById(R.id.full_name);
            actionTextView = view.findViewById(R.id.action);
            cancelButton = view.findViewById(R.id.cancel_button);

            Bundle bundle = getArguments();

            if (bundle != null) {
                serviceLog = bundle.getParcelable("logs_body");
                user = bundle.getParcelable("user_body");
                fullNameTextView.setText(user.getFullName());
                actionTextView.setText(serviceLog.getAction().toDisplayString());
            }

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (actionsListener != null) {
                        actionsListener.onCancel();
                    }
                }
            });
        }

        public interface ActionsListener {

            void onCancel();

        }

        public AlreadyServedBoxFragment.ActionsListener getActionsListener() {
            return actionsListener;
        }

        public void setActionsListener(AlreadyServedBoxFragment.ActionsListener actionsListener) {
            this.actionsListener = actionsListener;
        }
    }

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
        final UserRealmObject userRealmObject = DBManager.getInstance().getUser(barcode.displayValue);
        if (userRealmObject != null) {
            final User user = new User();
            user.setCode(userRealmObject.getCode());
            user.setFullName(userRealmObject.getFullName());
            user.setRole(User.Role.get(userRealmObject.getRole()));
            user.setFood(userRealmObject.isFood());
            user.setTransport(userRealmObject.isTransport());
            if (user.isFood()) {
                playSuccessBeep();
                final ServiceLog serviceLog = new ServiceLog();
                serviceLog.setUuid(UUID.randomUUID().toString().replace("-", ""));
                serviceLog.setCode(user.getCode());
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
                Bundle bundle = new Bundle();
                bundle.putParcelable("user_body", user);
                bundle.putParcelable("logs_body", serviceLog);
                if (!DBManager.getInstance().hadService(user.getFullName(), serviceLog.getAction().name())) {
                    final FoodServiceFragment.FoodServiceBoxFragment foodServiceBoxFragment = new FoodServiceFragment.FoodServiceBoxFragment();
                    foodServiceBoxFragment.setArguments(bundle);
                    foodServiceBoxFragment.setActionsListener(new FoodServiceBoxFragment.ActionsListener() {

                        @Override
                        public void onSubmit() {
                            DBManager
                                    .getInstance()
                                    .insertHistoryLog(
                                            serviceLog.getUuid(),
                                            user.getFullName(),
                                            serviceLog.getAction().name(),
                                            new Date());
                            FragmentManager fragmentManager = getChildFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
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
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
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
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.service_box, foodServiceBoxFragment);
                    fragmentTransaction.commit();
                } else {
                    final AlreadyServedBoxFragment alreadyServedBoxFragment = new AlreadyServedBoxFragment();
                    alreadyServedBoxFragment.setArguments(bundle);
                    alreadyServedBoxFragment.setActionsListener(new AlreadyServedBoxFragment.ActionsListener() {
                        @Override
                        public void onCancel() {
                            FragmentManager fragmentManager = getChildFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                            fragmentTransaction.remove(alreadyServedBoxFragment);
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
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.replace(R.id.service_box, alreadyServedBoxFragment);
                    fragmentTransaction.commit();
                }
            } else {
                playErrorBeep();
                Toast.makeText(getContext(), "Not Paid", Toast.LENGTH_SHORT).show();
                resume();
            }
        } else {
            playErrorBeep();
            Toast.makeText(getContext(), "Invalid QR code", Toast.LENGTH_SHORT).show();
            resume();
        }

    }

}
