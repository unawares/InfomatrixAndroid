package com.example.infomatrix.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    public enum Role {

        @SerializedName("1")
        ADMIN(1),
        @SerializedName("2")
        VOLUNTEER(2),
        @SerializedName("3")
        SUPERVISOR(3),
        @SerializedName("4")
        STUDENT(4),
        @SerializedName("5")
        GUEST(5);

        public static Role get(int identifier) {
            switch (identifier) {
                case 1: return ADMIN;
                case 2: return VOLUNTEER;
                case 3: return SUPERVISOR;
                case 4: return STUDENT;
                case 5: return GUEST;
                default: return null;
            }
        }

        private int identifier;

        Role(int identifier) {
            this.identifier = identifier;
        }

        public int getIdentifier() {
            return identifier;
        }

        public String toDisplayString() {
            switch (this) {
                case ADMIN: return "Admin";
                case VOLUNTEER: return "Volunteer";
                case SUPERVISOR: return "Supervisor";
                case STUDENT: return "Student";
                case GUEST: return "Guest";
                default: return "";
            }
        }

    }

    @SerializedName("fullname")
    @Expose
    private String fullName;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("role")
    @Expose
    private Role role;

    @SerializedName("is_food")
    @Expose
    private boolean isFood;

    @SerializedName("is_transport")
    @Expose
    private boolean isTransport;

    public User() {

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isFood() {
        return isFood;
    }

    public void setFood(boolean food) {
        isFood = food;
    }

    public boolean isTransport() {
        return isTransport;
    }

    public void setTransport(boolean transport) {
        isTransport = transport;
    }

    protected User(Parcel in) {
        fullName = in.readString();
        code = in.readString();
        role = Role.get(in.readInt());
        isFood = in.readByte() != 0;
        isTransport = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(code);
        dest.writeInt(role.getIdentifier());
        dest.writeByte((byte) (isFood ? 1 : 0));
        dest.writeByte((byte) (isTransport ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
