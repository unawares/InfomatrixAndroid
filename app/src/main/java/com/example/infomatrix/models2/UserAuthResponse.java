package com.example.infomatrix.models2;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class UserAuthResponse extends MessagedResponse implements Parcelable {

    @SerializedName("token_type")
    @Expose
    private String tokenType;

    @SerializedName("expires_in")
    @Expose
    private Date expiresIn;

    @SerializedName("access_token")
    @Expose
    private String accessToken;

    public UserAuthResponse() {

    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Date getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Date expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    protected UserAuthResponse(Parcel in) {
        super(in);
        tokenType = in.readString();
        expiresIn = (Date) in.readSerializable();
        accessToken = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(tokenType);
        dest.writeSerializable(expiresIn);
        dest.writeString(accessToken);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserAuthResponse> CREATOR = new Creator<UserAuthResponse>() {
        @Override
        public UserAuthResponse createFromParcel(Parcel in) {
            return new UserAuthResponse(in);
        }

        @Override
        public UserAuthResponse[] newArray(int size) {
            return new UserAuthResponse[size];
        }
    };
}
