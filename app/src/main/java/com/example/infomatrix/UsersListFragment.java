package com.example.infomatrix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.example.infomatrix.adapters.UsersAdapter;
import com.example.infomatrix.database.DBManager;
import com.example.infomatrix.models.FilterItem;
import com.example.infomatrix.models.User;
import com.example.infomatrix.models.UserRealmObject;
import com.example.infomatrix.models.Users;
import com.example.infomatrix.network.NetworkService;
import com.example.infomatrix.utils.Callback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class UsersListFragment extends Fragment {

    private static UsersListFragment instance;

    public static UsersListFragment getInstance() {
        return instance;
    }

    private RecyclerView usersRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usersRecyclerView = view.findViewById(R.id.users_recycler_view);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter();
    }

    public void setAdapter() {
        usersRecyclerView.setAdapter(new UsersAdapter(getContext(), DBManager.getInstance().getAllUsers(), new UsersAdapter.OnFilterClickListener() {
            @Override
            public void onFilterClick(Callback callback) {
                try {
                    UsersFragment usersFragment = (UsersFragment) getParentFragment();
                    if (usersFragment != null) {
                        usersFragment.showFilterModal(callback);
                    }
                } finally {

                }
            }
        }));
    }

    public UsersAdapter getAdapter() {
        return (UsersAdapter) usersRecyclerView.getAdapter();
    }

}
