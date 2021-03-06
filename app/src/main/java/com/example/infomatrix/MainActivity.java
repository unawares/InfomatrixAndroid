package com.example.infomatrix;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.infomatrix.adapters.UsersAdapter;
import com.example.infomatrix.database.DBManager;
import com.example.infomatrix.models.HistoryLogRealmObject;
import com.example.infomatrix.models.HistoryLogResponse;
import com.example.infomatrix.models.HistoryLogsResponse;
import com.example.infomatrix.models.ServiceLog;
import com.example.infomatrix.models.User;
import com.example.infomatrix.models.UserRealmObject;
import com.example.infomatrix.models.Users;
import com.example.infomatrix.network.NetworkService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.home:
                inflateFragment(new HomeFragment());
                break;
            case R.id.users:
                inflateFragment(new UsersFragment());
                break;
            case R.id.profile:
                inflateFragment(new ProfileFragment());
                break;
        }

        return true;
    }

    private void inflateFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void getUsers() {
        NetworkService
                .getInstance()
                .getUsersApi()
                .getUsers()
                .enqueue(new Callback<Users>() {

                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
                        if (response.isSuccessful()) {
                            Users users = response.body();
                            if (users != null) {
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
                            } else {
                                Toast.makeText(getApplicationContext(), "Internal Error", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {
                        t.printStackTrace();
                    }

                });
    }

}
