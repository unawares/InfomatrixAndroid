package com.example.infomatrix.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.infomatrix.R;

public class SupervisorHistoryFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public SupervisorHistoryFragment() {
    }

    public static SupervisorHistoryFragment newInstance(int sectionNumber) {
        SupervisorHistoryFragment fragment = new SupervisorHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_supervisors_history, container, false);
        return rootView;
    }
}
