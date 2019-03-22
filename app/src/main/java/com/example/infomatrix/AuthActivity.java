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

import com.example.infomatrix.network.NetworkService;
import com.example.infomatrix.models.Token;
import com.example.infomatrix.models.UserAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity implements Button.OnClickListener {

    private UserAuth userAuth;

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

        userAuth = new UserAuth();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signInButton = findViewById(R.id.sign_in_button);

        email.setText(preferences.getString("email", ""));
        password.setText(preferences.getString("password", ""));

        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        userAuth.setEmail(email.getText().toString().trim());
        userAuth.setPassword(password.getText().toString().trim());

        NetworkService.getInstance()
                .getTokenApi()
                .obtainToken(userAuth)
                .enqueue(new Callback<Token>() {

                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        Token token;
                        if (response.isSuccessful() && (token = response.body()) != null) {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("token", token.getToken());
                            editor.putString("email", userAuth.getEmail());
                            editor.putString("password", userAuth.getPassword());
                            editor.apply();
                            NetworkService.getInstance().setToken(token.getToken());
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        t.printStackTrace();
                    }

                });

    }
}
