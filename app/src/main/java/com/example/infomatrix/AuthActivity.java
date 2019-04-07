package com.example.infomatrix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.infomatrix.models.UserAuthRequestBody;
import com.example.infomatrix.models.UserAuthResponse;
import com.example.infomatrix.network.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button signInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        init();
    }

    public void init() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signInButton = findViewById(R.id.sign_in_button);

        email.setText(preferences.getString("email", ""));
        password.setText(preferences.getString("password", ""));

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInButton.setOnClickListener(null);
                final Button.OnClickListener self = this;
                final UserAuthRequestBody userAuthRequest = new UserAuthRequestBody();

                userAuthRequest.setEmail(email.getText().toString().trim());
                userAuthRequest.setPassword(password.getText().toString().trim());
                password.setText("");

                NetworkService
                        .getInstance()
                        .getAuthApi()
                        .login(userAuthRequest)
                        .enqueue(new Callback<UserAuthResponse>() {

                            @Override
                            public void onResponse(Call<UserAuthResponse> call, Response<UserAuthResponse> response) {
                                UserAuthResponse userAuthResponse = response.body();
                                if (response.isSuccessful() && userAuthResponse != null) {
                                    if (userAuthResponse.isSuccess()) {
                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("email", userAuthRequest.getEmail());
                                        editor.putString("password", userAuthRequest.getPassword());
                                        editor.putString("token", userAuthResponse.getAccessToken());
                                        editor.apply();
                                        NetworkService.getInstance().setToken(userAuthResponse.getAccessToken());
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), userAuthResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                                }
                                signInButton.setOnClickListener(self);
                            }

                            @Override
                            public void onFailure(Call<UserAuthResponse> call, Throwable t) {
                                t.printStackTrace();
                                signInButton.setOnClickListener(self);
                            }

                        });

            }
        });
    }

}
