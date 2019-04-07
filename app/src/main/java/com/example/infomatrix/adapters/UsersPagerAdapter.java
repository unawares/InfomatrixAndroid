package com.example.infomatrix.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.infomatrix.HistoryLogsFragment;
import com.example.infomatrix.UsersFragment;
import com.example.infomatrix.UsersListFragment;

public class UsersPagerAdapter extends FragmentPagerAdapter {

    private static final int SIZE = 2;

    public UsersPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new UsersListFragment();
            case 1: return new HistoryLogsFragment();
            default: return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Users";
            case 1: return "History";
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return SIZE;
    }
}
