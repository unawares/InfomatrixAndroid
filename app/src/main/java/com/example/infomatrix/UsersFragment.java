package com.example.infomatrix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.infomatrix.adapters.UsersPagerAdapter;
import com.example.infomatrix.database.DBManager;
import com.example.infomatrix.design.FilterModal;
import com.example.infomatrix.design.SynchronizeUsersButtonView;
import com.example.infomatrix.models.User;
import com.example.infomatrix.models.UserRealmObject;
import com.example.infomatrix.models.Users;
import com.example.infomatrix.network.NetworkService;
import com.example.infomatrix.utils.Callback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class UsersFragment extends Fragment {

    private SynchronizeUsersButtonView synchronizeUsersButtonView;
    private TabLayout tabs;
    private ViewPager viewPager;
    private FilterModal filterModal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabs = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.view_pager);
        filterModal = view.findViewById(R.id.filter_modal);
        synchronizeUsersButtonView = view.findViewById(R.id.synchronize_users_button_view);
        viewPager.setAdapter(new UsersPagerAdapter(getChildFragmentManager()));
        tabs.setupWithViewPager(viewPager);

        synchronizeUsersButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronizeUsersButtonView.setLoading(true);
                NetworkService
                        .getInstance()
                        .getUsersApi()
                        .getUsers()
                        .enqueue(new retrofit2.Callback<Users>() {

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
                                            userRealmObject.setTransport(user.isTransport());
                                            userRealmObjects.add(userRealmObject);
                                        }
                                        DBManager
                                                .getInstance()
                                                .updateUsers(userRealmObjects);
                                        try {
                                            UsersListFragment.getInstance().getAdapter().setUsers(DBManager.getInstance().getAllUsers());
                                            Toast.makeText(getContext(), "Users has been synchronized", Toast.LENGTH_SHORT).show();
                                        } catch (NullPointerException exception) {
                                            exception.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Internal Error", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                                }
                                synchronizeUsersButtonView.setLoading(false);
                            }

                            @Override
                            public void onFailure(Call<Users> call, Throwable t) {
                                t.printStackTrace();
                                synchronizeUsersButtonView.setLoading(false);
                            }

                        });
            }
        });
    }

    public void showFilterModal(final Callback callback) {
        filterModal.setVisibility(FilterModal.VISIBLE);
        filterModal.setOnSaveFilters(new FilterModal.OnSaveFilters() {

            @Override
            public void onSavedFilters() {
            }

            @Override
            public void onShown() {

            }

            @Override
            public void onHidden() {
                filterModal.setVisibility(FilterModal.INVISIBLE);
                callback.call();
            }

        });
        filterModal.show();
    }

}
