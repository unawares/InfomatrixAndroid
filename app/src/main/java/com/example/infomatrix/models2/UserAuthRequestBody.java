package com.example.infomatrix.models2;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAuthRequestBody implements Parcelable {

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    public UserAuthRequestBody() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    protected UserAuthRequestBody(Parcel in) {
        email = in.readString();
        password = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserAuthRequestBody> CREATOR = new Creator<UserAuthRequestBody>() {
        @Override
        public UserAuthRequestBody createFromParcel(Parcel in) {
            return new UserAuthRequestBody(in);
        }

        @Override
        public UserAuthRequestBody[] newArray(int size) {
            return new UserAuthRequestBody[size];
        }
    };

}
