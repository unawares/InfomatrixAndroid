package com.example.infomatrix.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryLogsResponse implements Parcelable {

    @SerializedName("logs")
    @Expose
    List<HistoryLogResponse> logs;

    public HistoryLogsResponse() {

    }

    protected HistoryLogsResponse(Parcel in) {
        logs = in.createTypedArrayList(HistoryLogResponse.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(logs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HistoryLogsResponse> CREATOR = new Creator<HistoryLogsResponse>() {
        @Override
        public HistoryLogsResponse createFromParcel(Parcel in) {
            return new HistoryLogsResponse(in);
        }

        @Override
        public HistoryLogsResponse[] newArray(int size) {
            return new HistoryLogsResponse[size];
        }
    };

    public List<HistoryLogResponse> getLogs() {
        return logs;
    }

    public void setLogs(List<HistoryLogResponse> logs) {
        this.logs = logs;
    }
}
