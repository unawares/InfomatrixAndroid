package com.example.infomatrix;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infomatrix.database.DBManager;
import com.example.infomatrix.models.ServiceLog;
import com.example.infomatrix.models.MessagedResponse;
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

public class TransportServiceFragment extends BarcodeReaderActivity.BaseFragment {

    public enum TransportationType {
        TO,
        FROM
    }

    public static class AlreadyServedBoxFragment extends Fragment {

        private User user;
        private ServiceLog serviceLog;

        private ActionsListener actionsListener;

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

        public ActionsListener getActionsListener() {
            return actionsListener;
        }

        public void setActionsListener(ActionsListener actionsListener) {
            this.actionsListener = actionsListener;
        }
    }

    public static class TransportServiceBoxFragment extends Fragment {

        private ProgressBar progress;
        private ActionsListener actionsListener;
        private User user;
        private ServiceLog serviceLog;
        private TextView fullNameTextView;
        private TextView actionTextView;
        private Button submitButton;
        private Button cancelButton;

        public void showErrorAlert(String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Error!!!")
                    .setMessage(message)
                    .setCancelable(false)
                    .setNegativeButton("GOT",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_service_box, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            progress = view.findViewById(R.id.progress);
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
                    submitButton.setEnabled(false);
                    progress.setVisibility(ProgressBar.VISIBLE);
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
                                            showErrorAlert(messagedResponse.getMessage());
                                        }
                                    } else {
                                        showErrorAlert(response.message());
                                    }
                                    submitButton.setEnabled(true);
                                    progress.setVisibility(ProgressBar.INVISIBLE);
                                    submitButton.setOnClickListener(self);

                                }

                                @Override
                                public void onFailure(Call<MessagedResponse> call, Throwable t) {
                                    t.printStackTrace();
                                    submitButton.setEnabled(true);
                                    progress.setVisibility(ProgressBar.INVISIBLE);
                                    submitButton.setOnClickListener(self);
                                    showErrorAlert("Internal Error");
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

    private TransportationType transportationType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transport_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        transportationType = TransportationType.valueOf(getArguments().getString("transportation_type"));
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
            if (user.isTransport()) {
                playSuccessBeep();
                final ServiceLog serviceLog = new ServiceLog();
                serviceLog.setUuid(UUID.randomUUID().toString().replace("-", ""));
                serviceLog.setCode(user.getCode());
                switch (transportationType) {
                    case TO:
                        serviceLog.setComment("To Transport done by " + user.getFullName());
                        serviceLog.setAction(ServiceLog.Action.TO_TRANSPORT);
                        break;
                    case FROM:
                        serviceLog.setComment("From Transport done by " + user.getFullName());
                        serviceLog.setAction(ServiceLog.Action.FROM_TRANSPORT);
                        break;
                }

                Bundle bundle = new Bundle();

                bundle.putParcelable("user_body", user);
                bundle.putParcelable("logs_body", serviceLog);

                if (!DBManager.getInstance().hadService(user.getFullName(), serviceLog.getAction().name())) {
                    final TransportServiceBoxFragment transportServiceBoxFragment = new TransportServiceBoxFragment();
                    transportServiceBoxFragment.setArguments(bundle);
                    transportServiceBoxFragment.setActionsListener(new TransportServiceBoxFragment.ActionsListener() {

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
                            fragmentTransaction.remove(transportServiceBoxFragment);
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
                            fragmentTransaction.remove(transportServiceBoxFragment);
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
                    fragmentTransaction.replace(R.id.service_box, transportServiceBoxFragment);
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