package com.example.infomatrix.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCode implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("services")
    @Expose
    private Map<String, List<? extends Service>> services;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("created")
    @Expose
    private Date created;

    @SerializedName("updated")
    @Expose
    private Date updated;

    @SerializedName("active")
    private boolean active;

    public UserCode() {

    }

    protected UserCode(Parcel in) {
        id = in.readInt();
        user = in.readParcelable(user.getClass().getClassLoader());
        in.readMap(services, services.getClass().getClassLoader());
        code = in.readString();
        active = in.readByte() != 0;
        created = (Date) in.readSerializable();
        updated = (Date) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(user, flags);
        dest.writeMap(services);
        dest.writeString(code);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeSerializable(created);
        dest.writeSerializable(updated);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserCode> CREATOR = new Creator<UserCode>() {
        @Override
        public UserCode createFromParcel(Parcel in) {
            return new UserCode(in);
        }

        @Override
        public UserCode[] newArray(int size) {
            return new UserCode[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Map<String, List<? extends Service>> getServices() {
        return services;
    }

    public void setServices(Map<String, List<? extends Service>> services) {
        this.services = services;
    }

}
