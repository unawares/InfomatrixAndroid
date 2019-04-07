package com.example.infomatrix.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryLogResponse implements Parcelable {

    @SerializedName("lid")
    @Expose
    private String uuid;

    @SerializedName("user")
    @Expose
    private String fullName;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("action")
    @Expose
    private String action;

    @SerializedName("tracedby")
    @Expose
    private String tracedBy;

    @SerializedName("dateof")
    @Expose
    private String date;

    public HistoryLogResponse() {

    }

    protected HistoryLogResponse(Parcel in) {
        uuid = in.readString();
        fullName = in.readString();
        role = in.readString();
        code = in.readString();
        action = in.readString();
        tracedBy = in.readString();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(fullName);
        dest.writeString(role);
        dest.writeString(code);
        dest.writeString(action);
        dest.writeString(tracedBy);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HistoryLogResponse> CREATOR = new Creator<HistoryLogResponse>() {
        @Override
        public HistoryLogResponse createFromParcel(Parcel in) {
            return new HistoryLogResponse(in);
        }

        @Override
        public HistoryLogResponse[] newArray(int size) {
            return new HistoryLogResponse[size];
        }
    };

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTracedBy() {
        return tracedBy;
    }

    public void setTracedBy(String tracedBy) {
        this.tracedBy = tracedBy;
    }

    public Date getDate() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return simpleDateFormat.parse(this.date);
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void setDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.date = simpleDateFormat.format(date);
    }

}
