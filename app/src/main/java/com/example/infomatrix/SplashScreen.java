package com.example.infomatrix;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;

import com.example.infomatrix.network.NetworkService;
import com.example.infomatrix.models.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends Activity {

    static final int DELAY = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String tokenStr = preferences.getString("token", null);
        if (tokenStr != null) {
            Token token = new Token();
            token.setToken(tokenStr);
            token.setToken(tokenStr);
            NetworkService.getInstance()
                    .getTokenApi()
                    .verifyToken(token)
                    .enqueue(new Callback<Token>() {

                        @Override
                        public void onResponse(Call<Token> call, Response<Token> response) {
                            if (response.isSuccessful()) {
                                toMainActivity();
                            } else {
                                toAuthActivity();
                            }
                        }

                        @Override
                        public void onFailure(Call<Token> call, Throwable t) {
                            toAuthActivity();
                        }

                    });
        } else {
            toAuthActivity();
        }

    }

    private void toActivity(final Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, DELAY);
    }

    private void toAuthActivity() {
        toActivity(new Intent(this, AuthActivity.class));
    }

    private void toMainActivity() {
        toActivity(new Intent(this, MainActivity.class));
    }

}
