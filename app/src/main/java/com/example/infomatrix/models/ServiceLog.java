package com.example.infomatrix.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceLog implements Parcelable {

    public enum Action {

        @SerializedName("btst") BREAKFAST("btst"),
        @SerializedName("lnch") LUNCH("lnch"),
        @SerializedName("dnnr") DINNER("dnnr"),
        @SerializedName("totp") TO_TRANSPORT("totp"),
        @SerializedName("fmtp") FROM_TRANSPORT("fmtp");

        private String code;

        Action(String code) {
            this.code = code;
        }

        public String toDisplayString() {
            switch (this) {
                case LUNCH:
                    return "Lunch";
                case DINNER:
                    return "Dinner";
                case BREAKFAST:
                    return "Breakfast";
                case TO_TRANSPORT:
                    return "To transport";
                case FROM_TRANSPORT:
                    return "From transport";
                default:
                    return "None";
            }
        }

        public static Action fromCode(String code) {
            switch (code) {
                case "btst":
                    return BREAKFAST;
                case "lnch":
                    return LUNCH;
                case "dnnr":
                    return DINNER;
                case "totp":
                    return TO_TRANSPORT;
                case "fmtp":
                    return FROM_TRANSPORT;
                default:
                    return null;
            }
        }

    }

    @SerializedName("id")
    @Expose
    private String uuid;

    @SerializedName("user")
    @Expose
    private String code;

    @SerializedName("action")
    @Expose
    private Action action;

    @SerializedName("comment")
    @Expose
    private String comment;

    public ServiceLog() {

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    protected ServiceLog(Parcel in) {
        uuid = in.readString();
        code = in.readString();
        comment = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(code);
        dest.writeString(comment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceLog> CREATOR = new Creator<ServiceLog>() {
        @Override
        public ServiceLog createFromParcel(Parcel in) {
            return new ServiceLog(in);
        }

        @Override
        public ServiceLog[] newArray(int size) {
            return new ServiceLog[size];
        }
    };
}
