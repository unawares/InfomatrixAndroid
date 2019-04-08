package com.example.infomatrix.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class BaseResponse implements Parcelable {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("beep_error")
    @Expose
    private boolean error;

    public BaseResponse() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    protected BaseResponse(Parcel in) {
        success = in.readByte() != 0;
        error = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeByte((byte) (error ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
