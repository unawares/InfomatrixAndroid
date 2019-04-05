package com.example.infomatrix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.infomatrix.models.User;
import com.example.infomatrix.models.UserCode;

public abstract class BaseServiceFragment extends Fragment {

    public class UserCodeIsNotSetException extends Exception {}

    private UserCode userCode;

    protected OnServiceActionsListener onServiceActionsListener;

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

    public void serve() throws UserCodeIsNotSetException {
        if (userCode == null) {
            throw new UserCodeIsNotSetException();
        }
    }

    public void close() {

    }

    public UserCode getUserCode() {
        return userCode;
    }

    public void setUserCode(UserCode userCode) {
        this.userCode = userCode;
    }

    public abstract void show();

    public abstract void hide();
}
