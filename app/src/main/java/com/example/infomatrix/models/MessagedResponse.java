package com.example.infomatrix.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessagedResponse extends BaseResponse implements Parcelable {

    @SerializedName("message")
    @Expose
    private String message;

    public MessagedResponse() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected MessagedResponse(Parcel in) {
        super(in);
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
