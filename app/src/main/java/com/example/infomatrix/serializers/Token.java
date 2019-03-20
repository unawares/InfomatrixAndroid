package com.example.infomatrix.serializers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token implements Parcelable {

    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private Token(Parcel parcel) {
        token = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(token);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator<Token>() {

        public Token createFromParcel(Parcel parcel) {
            return new Token(parcel);
        }

        public Token[] newArray(int size) {
            return new Token[size];
        }

    };
}
