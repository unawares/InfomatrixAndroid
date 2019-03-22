package com.example.infomatrix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.infomatrix.models.User;

public abstract class BaseServiceFragment extends Fragment {

    protected OnServiceActionsListener onServiceActionsListener;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_service, container, false);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OnServiceActionsListener getOnServiceActionsListener() {
        return onServiceActionsListener;
    }

    public void setOnServiceActionsListener(OnServiceActionsListener onServiceActionsListener) {
        this.onServiceActionsListener = onServiceActionsListener;
    }

    protected void commit() {
        if (onServiceActionsListener != null) {
            onServiceActionsListener.onServiceCommit();
        }
    }

    protected void cancel() {
        if (onServiceActionsListener != null) {
            onServiceActionsListener.onServiceCancel();
        }
    }

    public interface OnServiceActionsListener {

        void onServiceCommit();
        void onServiceCancel();

    }

    public abstract void serve();
}
