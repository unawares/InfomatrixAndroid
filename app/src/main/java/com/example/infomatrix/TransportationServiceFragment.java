package com.example.infomatrix;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.infomatrix.models.Food;
import com.example.infomatrix.models.User;
import com.example.infomatrix.models.UserCode;

public class TransportationServiceFragment extends BaseServiceFragment implements Button.OnClickListener {

    private UserCode userCode;
    private String transportation;
    private TextView fullName;
    private TextView action;
    private Button submitButton;
    private Button cancelButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transportation_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userCode = bundle.getParcelable("user_code");
            transportation = bundle.getString("transportation");
        }

        fullName = view.findViewById(R.id.full_name);
        action = view.findViewById(R.id.action);
        submitButton = view.findViewById(R.id.submit_button);
        cancelButton = view.findViewById(R.id.cancel_button);

        fullName.setText(String.format("%s %s", userCode.getUser().getFirstName(), userCode.getUser().getLastName()));
        if (transportation.equals("to")) {
            action.setText("Transportation to the camp!");
        } else {
            action.setText("Transportation from the camp!");
        }

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
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
